package com.civicreportgh.app

import android.content.Context
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class UploadResult(
    val imageUrl: String,
    val videoUrl: String?
)

object FirebaseRepository {

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    suspend fun submitReport(
        imageUri: Uri,
        videoUri: Uri? = null,
        mediaType: String = "IMAGE",
        category: IssueCategory,
        description: String,
        locationText: String?
    ): Result<UploadResult> = runCatching {
        // 1. Ensure anonymous sign-in
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }

        // 2. Upload image/thumbnail to Storage
        val fileName = "reports/${UUID.randomUUID()}.jpg"
        val storageRef = storage.reference.child(fileName)
        storageRef.putFile(imageUri).await()
        val downloadUrl = storageRef.downloadUrl.await().toString()

        // 3. Upload video to Storage if provided
        var downloadVideoUrl: String? = null
        if (videoUri != null) {
            val videoFileName = "reports/videos/${UUID.randomUUID()}.mp4"
            val videoRef = storage.reference.child(videoFileName)
            videoRef.putFile(videoUri).await()
            downloadVideoUrl = videoRef.downloadUrl.await().toString()
        }

        val institution = InstitutionRepository.institutionFor(category)

        // 4. Save metadata to Firestore
        val reportMap = hashMapOf(
            "userId" to auth.currentUser?.uid,
            "imageUrl" to downloadUrl,
            "videoUrl" to downloadVideoUrl,
            "mediaType" to mediaType,
            "category" to category.name,
            "categoryDisplay" to category.displayName,
            "description" to description,
            "location" to locationText,
            "timestamp" to Timestamp.now(),
            "institution" to institution.name,
            "status" to "PENDING"
        )

        db.collection("reports").add(reportMap).await()
        UploadResult(downloadUrl, downloadVideoUrl)
    }

    suspend fun saveLocally(
        context: Context,
        imageUrl: String,
        videoUrl: String? = null,
        mediaType: String = "IMAGE",
        category: IssueCategory,
        description: String,
        locationText: String?,
        institutionName: String,
        isSynced: Boolean,
        status: String = "PENDING"
    ): Long {
        val dao = ReportDatabase.getDatabase(context).reportDao()
        val localReport = LocalReport(
            imageUrl = imageUrl,
            videoUrl = videoUrl,
            mediaType = mediaType,
            category = category.displayName,
            description = description,
            location = locationText,
            timestamp = System.currentTimeMillis(),
            institution = institutionName,
            isSynced = isSynced,
            status = status
        )
        return dao.insert(localReport)
    }

    suspend fun updateLocalSyncStatus(
        context: Context,
        reportId: Long,
        cloudImageUrl: String,
        cloudVideoUrl: String? = null,
        status: String = "PENDING"
    ) {
        val dao = ReportDatabase.getDatabase(context).reportDao()
        val existing = dao.getReportById(reportId) ?: return
        val updated = existing.copy(
            imageUrl = cloudImageUrl,
            videoUrl = cloudVideoUrl ?: existing.videoUrl,
            isSynced = true,
            status = status
        )
        dao.update(updated)
    }
}
