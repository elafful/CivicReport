package com.civicreportgh.app

import android.app.Application
import org.osmdroid.config.Configuration
import java.io.File

class CivicReportApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Map configuration
        val osmConfig = Configuration.getInstance()
        val prefs = getSharedPreferences("${packageName}_preferences", MODE_PRIVATE)
        osmConfig.load(this, prefs)
        
        // Cache configuration
        val cacheDir = externalCacheDir?.let { File(it, "osmdroid_tiles") }
        if (cacheDir != null) {
            osmConfig.osmdroidTileCache = cacheDir
        }
        osmConfig.tileFileSystemCacheMaxBytes = 100L * 1024 * 1024 // 100 megabytes
    }
}
