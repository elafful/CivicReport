package com.civicreportgh.app

import android.app.Application
import android.location.Geocoder
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

data class ReportUiState(
    val imageUri: Uri? = null,
    val videoUri: Uri? = null,
    val mediaType: String = "IMAGE",
    val selectedCategory: IssueCategory = IssueCategory.OTHER,
    val description: String = "",
    val locationText: String? = null,
    val readableAddress: String? = null,
    val isLocationLoading: Boolean = false,
    val institution: Institution = InstitutionRepository.institutionFor(IssueCategory.OTHER),
    val isSubmitting: Boolean = false,
    val submissionSuccess: Boolean = false,
    val submissionError: String? = null
)

data class EmailReportData(
    val recipient: String,
    val subject: String,
    val body: String,
    val imageUri: Uri?
)

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    fun initReport(
        uri: Uri,
        suggestedCategory: IssueCategory,
        mediaType: String = "IMAGE",
        videoUri: Uri? = null
    ) {
        if (_uiState.value.imageUri == null) {
            _uiState.value = _uiState.value.copy(
                imageUri = uri,
                videoUri = videoUri,
                mediaType = mediaType,
                selectedCategory = suggestedCategory,
                institution = InstitutionRepository.institutionFor(suggestedCategory)
            )
        }
    }

    fun updateCategory(category: IssueCategory) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            institution = InstitutionRepository.institutionFor(category)
        )
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun fetchLocation(onFailure: () -> Unit) {
        val client = LocationServices.getFusedLocationProviderClient(getApplication<Application>())
        _uiState.value = _uiState.value.copy(isLocationLoading = true)

        try {
            client.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    val locText = "%.6f, %.6f".format(loc.latitude, loc.longitude)
                    _uiState.value = _uiState.value.copy(
                        locationText = locText,
                        isLocationLoading = false
                    )
                    reverseGeocode(loc.latitude, loc.longitude)
                } else {
                    _uiState.value = _uiState.value.copy(isLocationLoading = false)
                    onFailure()
                }
            }.addOnFailureListener {
                _uiState.value = _uiState.value.copy(isLocationLoading = false)
                onFailure()
            }
        } catch (e: SecurityException) {
            _uiState.value = _uiState.value.copy(isLocationLoading = false)
            onFailure()
        }
    }

    private fun reverseGeocode(lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                if (!addresses.isNullOrEmpty()) {
                    val addressText = addresses[0].getAddressLine(0)
                    _uiState.value = _uiState.value.copy(readableAddress = addressText)
                }
            } catch (e: Exception) {
                // Ignore geocoding errors, locationText still holds coordinates
            }
        }
    }

    fun submitReport() {
        val state = _uiState.value
        val uri = state.imageUri ?: return
        
        _uiState.value = _uiState.value.copy(
            isSubmitting = true,
            submissionError = null,
            submissionSuccess = false
        )

        viewModelScope.launch {
            // 1. Save locally first as "Pending" (isSynced = false)
            val localId = FirebaseRepository.saveLocally(
                context = getApplication(),
                imageUrl = uri.toString(),
                videoUrl = state.videoUri?.toString(),
                mediaType = state.mediaType,
                category = state.selectedCategory,
                description = state.description,
                locationText = state.locationText ?: state.readableAddress,
                institutionName = state.institution.name,
                isSynced = false
            )

            // 2. Attempt cloud upload
            val result = FirebaseRepository.submitReport(
                imageUri = uri,
                videoUri = state.videoUri,
                mediaType = state.mediaType,
                category = state.selectedCategory,
                description = state.description,
                locationText = state.locationText
            )
            
            result.onSuccess { (cloudImageUrl, cloudVideoUrl) ->
                // 3. Update local record on success
                FirebaseRepository.updateLocalSyncStatus(
                    context = getApplication(),
                    reportId = localId,
                    cloudImageUrl = cloudImageUrl,
                    cloudVideoUrl = cloudVideoUrl
                )
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    submissionSuccess = true
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    submissionError = error.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun formatEmailBody(intro: String, categoryLabel: String, descLabel: String, locLabel: String, footer: String): String {
        val state = _uiState.value
        return buildString {
            append(intro).append("\n\n")
            append(categoryLabel.replace("%1\$s", state.selectedCategory.displayName)).append("\n")
            append(descLabel).append("\n").append(state.description).append("\n\n")
            if (state.readableAddress != null) {
                append(locLabel.replace("%1\$s", state.readableAddress)).append("\n")
            }
            if (state.locationText != null) {
                append("Coordinates: ${state.locationText}").append("\n")
            }
            if (state.videoUri != null) {
                append("Media Attached: Short Video\n")
            }
            append("\n").append(footer)
        }
    }

    fun saveReportLocally() {
        val state = _uiState.value
        val uri = state.imageUri ?: return
        viewModelScope.launch {
            FirebaseRepository.saveLocally(
                context = getApplication(),
                imageUrl = uri.toString(),
                videoUrl = state.videoUri?.toString(),
                mediaType = state.mediaType,
                category = state.selectedCategory,
                description = state.description,
                locationText = state.locationText ?: state.readableAddress,
                institutionName = state.institution.name,
                isSynced = false,
                status = "EMAILED"
            )
        }
    }

    fun prepareEmailData(subjectTemplate: String, bodyIntro: String, categoryLabel: String, descLabel: String, locLabel: String, footer: String): EmailReportData {
        val state = _uiState.value
        val subject = subjectTemplate.replace("%1\$s", state.selectedCategory.displayName)
        val body = formatEmailBody(bodyIntro, categoryLabel, descLabel, locLabel, footer)
        return EmailReportData(
            recipient = state.institution.email,
            subject = subject,
            body = body,
            imageUri = state.imageUri
        )
    }
}
