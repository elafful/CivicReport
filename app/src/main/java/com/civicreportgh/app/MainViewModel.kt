package com.civicreportgh.app

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class MainUiState {
    object Idle : MainUiState()
    object Processing : MainUiState()
    data class Success(
        val uri: Uri,
        val suggestedCategory: IssueCategory,
        val mediaType: String = "IMAGE",
        val videoUri: Uri? = null
    ) : MainUiState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Idle)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        setupRemoteConfig()
    }

    private fun setupRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val json = remoteConfig.getString("institution_mapping")
                if (json.isNotEmpty()) {
                    InstitutionRepository.updateFromRemote(json)
                }
            }
        }
    }

    /** Runs free, on-device ML Kit labeling purely to SUGGEST a starting category. */
    fun handleImage(uri: Uri) {
        _uiState.value = MainUiState.Processing
        try {
            val image = InputImage.fromFilePath(getApplication(), uri)
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    val topLabel = labels.maxByOrNull { it.confidence }?.text ?: ""
                    val suggested = InstitutionRepository.categoryFromLabel(topLabel)
                    _uiState.value = MainUiState.Success(uri, suggested)
                }
                .addOnFailureListener {
                    // Labeling is a nice-to-have; never block the user from reporting.
                    _uiState.value = MainUiState.Success(uri, IssueCategory.OTHER)
                }
        } catch (e: Exception) {
            _uiState.value = MainUiState.Success(uri, IssueCategory.OTHER)
        }
    }

    fun handleVideo(videoUri: Uri, thumbnailUri: Uri) {
        _uiState.value = MainUiState.Processing
        try {
            val image = InputImage.fromFilePath(getApplication(), thumbnailUri)
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    val topLabel = labels.maxByOrNull { it.confidence }?.text ?: ""
                    val suggested = InstitutionRepository.categoryFromLabel(topLabel)
                    _uiState.value = MainUiState.Success(
                        uri = thumbnailUri,
                        suggestedCategory = suggested,
                        mediaType = "VIDEO",
                        videoUri = videoUri
                    )
                }
                .addOnFailureListener {
                    _uiState.value = MainUiState.Success(
                        uri = thumbnailUri,
                        suggestedCategory = IssueCategory.OTHER,
                        mediaType = "VIDEO",
                        videoUri = videoUri
                    )
                }
        } catch (e: Exception) {
            _uiState.value = MainUiState.Success(
                uri = thumbnailUri,
                suggestedCategory = IssueCategory.OTHER,
                mediaType = "VIDEO",
                videoUri = videoUri
            )
        }
    }

    fun resetState() {
        _uiState.value = MainUiState.Idle
    }
}
