package com.civicreportgh.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.civicreportgh.app.databinding.ActivityReportDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_REPORT_ID = "extra_report_id"
    }

    private lateinit var binding: ActivityReportDetailBinding
    private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportPostponeEnterTransition()
        
        setupUI()
        loadReport()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.mapDetail.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapDetail.setMultiTouchControls(true)
    }

    private fun loadReport() {
        val reportId = intent.getLongExtra(EXTRA_REPORT_ID, -1)
        if (reportId == -1L) {
            Toast.makeText(this, "Error: Invalid Report ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val dao = ReportDatabase.getDatabase(this@ReportDetailActivity).reportDao()
                val report = dao.getReportById(reportId)
                withContext(Dispatchers.Main) {
                    if (report != null) {
                        displayReport(report)
                    } else {
                        Toast.makeText(this@ReportDetailActivity, "Error: Report not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ReportDetailActivity, "Database Error: ${e.message}", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun displayReport(report: LocalReport) {
        binding.imageDetail.transitionName = "report_image_${report.id}"
        
        binding.textDetailCategory.text = report.category
        binding.textDetailDate.text = dateFormat.format(Date(report.timestamp))
        binding.textDetailDescription.text = report.description
        binding.textDetailInstitution.text = report.institution
        binding.textDetailLocation.text = report.location ?: "No location provided"
        binding.textStatusCurrent.text = "Status: ${report.status}"
        
        when (report.status) {
            "PENDING" -> binding.textStatusCurrent.setTextColor(getColor(R.color.ghana_red))
            "IN_PROGRESS" -> binding.textStatusCurrent.setTextColor(getColor(R.color.ghana_yellow))
            "RESOLVED" -> binding.textStatusCurrent.setTextColor(getColor(R.color.ghana_green))
        }

        binding.imageDetail.load(report.imageUrl) {
            crossfade(true)
            listener(
                onSuccess = { _, _ -> supportStartPostponedEnterTransition() },
                onError = { _, _ -> supportStartPostponedEnterTransition() }
            )
        }

        if (!report.videoUrl.isNullOrEmpty()) {
            binding.buttonPlayVideo.visibility = View.VISIBLE
            val playAction = View.OnClickListener {
                playVideoSafely(report.videoUrl)
            }
            binding.buttonPlayVideo.setOnClickListener(playAction)
            binding.imageDetail.setOnClickListener {
                if (binding.buttonPlayVideo.visibility == View.VISIBLE) {
                    playAction.onClick(it)
                }
            }
        } else {
            binding.buttonPlayVideo.visibility = View.GONE
            binding.videoDetail.visibility = View.GONE
        }

        report.location?.let { locString ->
            val parts = locString.split(",")
            if (parts.size == 2) {
                val lat = parts[0].trim().toDoubleOrNull()
                val lng = parts[1].trim().toDoubleOrNull()
                if (lat != null && lng != null) {
                    val geoPoint = GeoPoint(lat, lng)
                    val marker = Marker(binding.mapDetail)
                    marker.position = geoPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    binding.mapDetail.overlays.add(marker)
                    binding.mapDetail.controller.setZoom(17.0)
                    binding.mapDetail.controller.setCenter(geoPoint)
                }
            }
        } ?: run {
            binding.mapDetail.visibility = View.GONE
        }
    }

    private fun playVideoSafely(videoUrlString: String?) {
        if (videoUrlString.isNullOrBlank()) {
            Toast.makeText(this, "Video URL is unavailable", Toast.LENGTH_SHORT).show()
            return
        }

        val videoUri: Uri = try {
            when {
                videoUrlString.startsWith("content://") || videoUrlString.startsWith("http://") || videoUrlString.startsWith("https://") -> {
                    Uri.parse(videoUrlString)
                }
                videoUrlString.startsWith("file://") -> {
                    Uri.parse(videoUrlString)
                }
                videoUrlString.startsWith("/") -> {
                    val file = File(videoUrlString)
                    if (file.exists()) Uri.fromFile(file) else Uri.parse(videoUrlString)
                }
                else -> Uri.parse(videoUrlString)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid video path", Toast.LENGTH_SHORT).show()
            return
        }

        binding.imageDetail.visibility = View.GONE
        binding.buttonPlayVideo.visibility = View.GONE
        binding.videoDetail.visibility = View.VISIBLE

        binding.videoDetail.setOnErrorListener { _, _, _ ->
            Toast.makeText(this, "In-app playback failed. Opening external player...", Toast.LENGTH_SHORT).show()
            binding.videoDetail.visibility = View.GONE
            binding.imageDetail.visibility = View.VISIBLE
            binding.buttonPlayVideo.visibility = View.VISIBLE
            launchExternalVideoPlayer(videoUri)
            true
        }

        binding.videoDetail.setOnCompletionListener {
            binding.videoDetail.visibility = View.GONE
            binding.imageDetail.visibility = View.VISIBLE
            binding.buttonPlayVideo.visibility = View.VISIBLE
        }

        try {
            val mediaController = MediaController(this)
            mediaController.setAnchorView(binding.videoDetail)
            binding.videoDetail.setMediaController(mediaController)
            binding.videoDetail.setVideoURI(videoUri)
            binding.videoDetail.requestFocus()
            binding.videoDetail.start()
        } catch (e: Exception) {
            binding.videoDetail.visibility = View.GONE
            binding.imageDetail.visibility = View.VISIBLE
            binding.buttonPlayVideo.visibility = View.VISIBLE
            launchExternalVideoPlayer(videoUri)
        }
    }

    private fun launchExternalVideoPlayer(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "video/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, "Play Video"))
        } catch (e: Exception) {
            Toast.makeText(this, "No video player available to play this file", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapDetail.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapDetail.onPause()
    }
}
