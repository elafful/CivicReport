package com.civicreportgh.app

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.civicreportgh.app.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var pendingPhotoUri: Uri? = null
    private var pendingVideoUri: Uri? = null
    private var pendingAction: String? = null // "PHOTO" or "VIDEO"
    private var preSelectedCategory: IssueCategory? = null

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) pendingPhotoUri?.let { viewModel.handleImage(it) }
    }

    private val recordVideo = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            pendingVideoUri?.let { processVideo(it) }
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val type = contentResolver.getType(it)
            if (type?.startsWith("video") == true) {
                processVideo(it)
            } else {
                viewModel.handleImage(it)
            }
        }
    }

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            if (pendingAction == "VIDEO") {
                launchVideoCamera(preSelectedCategory)
            } else {
                launchCamera(preSelectedCategory)
            }
        } else {
            Toast.makeText(this, R.string.camera_permission_needed, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.buttonReportIssue.setOnClickListener {
            showImageSourceDialog()
        }

        binding.cardRoads.setOnClickListener { startReportFlow(IssueCategory.ROAD) }
        binding.cardSanitation.setOnClickListener { startReportFlow(IssueCategory.SANITATION) }
        binding.cardStreetlights.setOnClickListener { startReportFlow(IssueCategory.ELECTRICITY) }
        binding.cardWater.setOnClickListener { startReportFlow(IssueCategory.WATER_SUPPLY) }
        binding.cardSafety.setOnClickListener { startReportFlow(IssueCategory.SAFETY) }
        binding.cardOther.setOnClickListener { startReportFlow(IssueCategory.OTHER) }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_reports -> {
                    startActivity(android.content.Intent(this, HistoryActivity::class.java))
                    false
                }
                R.id.nav_notifications -> {
                    startActivity(android.content.Intent(this, NotificationActivity::class.java))
                    false
                }
                R.id.nav_profile -> {
                    startActivity(android.content.Intent(this, ProfileActivity::class.java))
                    false
                }
                else -> false
            }
        }
    }

    private fun startReportFlow(category: IssueCategory) {
        showImageSourceDialog(category)
    }

    private fun showImageSourceDialog(category: IssueCategory? = null) {
        preSelectedCategory = category
        val options = arrayOf("Take Photo", "Record Short Video (Max 15s)", "Choose Photo/Video from Gallery")
        AlertDialog.Builder(this)
            .setTitle("Select Media Source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        pendingAction = "PHOTO"
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            launchCamera(category)
                        } else {
                            requestCameraPermission.launch(android.Manifest.permission.CAMERA)
                        }
                    }
                    1 -> {
                        pendingAction = "VIDEO"
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            launchVideoCamera(category)
                        } else {
                            requestCameraPermission.launch(Manifest.permission.CAMERA)
                        }
                    }
                    2 -> pickMedia.launch("*/*")
                }
            }
            .show()
    }

    private fun launchCamera(category: IssueCategory? = null) {
        try {
            preSelectedCategory = category
            val photoFile = File(getExternalFilesDir("images"), "issue_${System.currentTimeMillis()}.jpg")
            photoFile.parentFile?.mkdirs()
            val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
            pendingPhotoUri = uri
            takePicture.launch(uri)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to launch camera: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun launchVideoCamera(category: IssueCategory? = null) {
        try {
            preSelectedCategory = category
            val videoFile = File(getExternalFilesDir("videos"), "issue_vid_${System.currentTimeMillis()}.mp4")
            videoFile.parentFile?.mkdirs()
            val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", videoFile)
            pendingVideoUri = uri

            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
                putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15) // Max 15 seconds
                putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)   // Compressed SD quality (~1-3MB)
                putExtra(MediaStore.EXTRA_SIZE_LIMIT, 5242880L) // 5MB size limit
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            recordVideo.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to launch video recorder: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun processVideo(videoUri: Uri) {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val thumbUri = generateVideoThumbnail(this@MainActivity, videoUri)
            withContext(Dispatchers.Main) {
                if (thumbUri != null) {
                    viewModel.handleVideo(videoUri, thumbUri)
                } else {
                    // Fallback to video Uri if thumbnail failed
                    viewModel.handleVideo(videoUri, videoUri)
                }
            }
        }
    }

    private fun generateVideoThumbnail(context: Context, videoUri: Uri): Uri? {
        var retriever: MediaMetadataRetriever? = null
        return try {
            retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, videoUri)
            val bitmap = retriever.frameAtTime ?: retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)

            if (bitmap != null) {
                val thumbFile = File(context.getExternalFilesDir("images"), "thumb_${System.currentTimeMillis()}.jpg")
                thumbFile.parentFile?.mkdirs()
                FileOutputStream(thumbFile).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                }
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", thumbFile)
            } else null
        } catch (e: Exception) {
            null
        } finally {
            try {
                retriever?.release()
            } catch (_: Exception) {}
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is MainUiState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        is MainUiState.Processing -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MainUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            openReportScreen(state.uri, state.suggestedCategory, state.mediaType, state.videoUri)
                            viewModel.resetState()
                        }
                    }
                }
            }
        }
    }

    private fun openReportScreen(
        imageUri: Uri,
        suggestedCategory: IssueCategory,
        mediaType: String = "IMAGE",
        videoUri: Uri? = null
    ) {
        val finalCategory = preSelectedCategory ?: suggestedCategory
        val intent = Intent(this, ReportActivity::class.java).apply {
            putExtra(ReportActivity.EXTRA_IMAGE_URI, imageUri)
            putExtra(ReportActivity.EXTRA_SUGGESTED_CATEGORY, finalCategory.name)
            putExtra(ReportActivity.EXTRA_MEDIA_TYPE, mediaType)
            if (videoUri != null) {
                putExtra(ReportActivity.EXTRA_VIDEO_URI, videoUri)
            }
        }
        startActivity(intent)
        preSelectedCategory = null
    }
}
