package com.civicreportgh.app

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "reports")
data class LocalReport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imageUrl: String, // Keeping this for backward compatibility or as the "main" image / thumbnail
    val videoUrl: String? = null,
    val mediaType: String = "IMAGE", // "IMAGE" or "VIDEO"
    val additionalImageUrls: String? = null, // Comma separated list
    val category: String,
    val description: String,
    val location: String?,
    val timestamp: Long,
    val institution: String,
    val isSynced: Boolean = false,
    val status: String = "PENDING" // PENDING, IN_PROGRESS, RESOLVED
)
