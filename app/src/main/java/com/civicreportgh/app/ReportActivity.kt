package com.civicreportgh.app

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.civicreportgh.app.databinding.ActivityReportBinding
import kotlinx.coroutines.launch

class ReportActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_SUGGESTED_CATEGORY = "extra_suggested_category"
        const val EXTRA_MEDIA_TYPE = "extra_media_type"
        const val EXTRA_VIDEO_URI = "extra_video_uri"
    }

    private lateinit var binding: ActivityReportBinding
    private val viewModel: ReportViewModel by viewModels()

    private val requestLocationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.fetchLocation {
            Toast.makeText(this, R.string.location_unavailable, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        initData()
        setupUI()
        observeViewModel()
    }

    private fun initData() {
        val uri = intent.getParcelableExtra<Uri>(EXTRA_IMAGE_URI)!!
        val suggested = intent.getStringExtra(EXTRA_SUGGESTED_CATEGORY)
            ?.let { runCatching { IssueCategory.valueOf(it) }.getOrNull() } ?: IssueCategory.OTHER
        val mediaType = intent.getStringExtra(EXTRA_MEDIA_TYPE) ?: "IMAGE"
        val videoUri = intent.getParcelableExtra<Uri>(EXTRA_VIDEO_URI)
        viewModel.initReport(uri, suggested, mediaType, videoUri)
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val categories = IssueCategory.entries.map { it.displayName }
        binding.spinnerCategory.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, categories
        )

        binding.spinnerCategory.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: android.widget.AdapterView<*>?, v: View?, pos: Int, id: Long) {
                viewModel.updateCategory(IssueCategory.entries[pos])
            }
            override fun onNothingSelected(p: android.widget.AdapterView<*>?) {}
        }

        binding.editDescription.addTextChangedListener {
            viewModel.updateDescription(it?.toString().orEmpty())
        }

        binding.buttonAddLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.fetchLocation {
                    Toast.makeText(this, R.string.location_unavailable, Toast.LENGTH_SHORT).show()
                }
            } else {
                requestLocationPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        binding.buttonSendReport.setOnClickListener {
            if (viewModel.uiState.value.description.isBlank()) {
                binding.editDescription.error = getString(R.string.description_required)
            } else {
                viewModel.submitReport()
            }
        }

        binding.buttonSendEmail.setOnClickListener {
            viewModel.saveReportLocally()
            sendEmailReport()
        }

        val playVideoListener = View.OnClickListener {
            val state = viewModel.uiState.value
            if (state.mediaType == "VIDEO" && state.videoUri != null) {
                playVideoSafely(state.videoUri)
            }
        }
        binding.iconVideoBadge.setOnClickListener(playVideoListener)
        binding.imagePreview.setOnClickListener(playVideoListener)
    }

    private fun playVideoSafely(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "video/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, "Play Video"))
        } catch (e: Exception) {
            Toast.makeText(this, "No video player available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmailReport() {
        if (viewModel.uiState.value.description.isBlank()) {
            binding.editDescription.error = getString(R.string.description_required)
            return
        }

        val data = viewModel.prepareEmailData(
            subjectTemplate = getString(R.string.email_subject),
            bodyIntro = getString(R.string.email_body_intro),
            categoryLabel = getString(R.string.email_body_category),
            descLabel = getString(R.string.email_body_description),
            locLabel = getString(R.string.email_body_location),
            footer = getString(R.string.email_body_footer)
        )

        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(data.recipient))
            putExtra(android.content.Intent.EXTRA_SUBJECT, data.subject)
            putExtra(android.content.Intent.EXTRA_TEXT, data.body)
            putExtra(android.content.Intent.EXTRA_STREAM, data.imageUri)
            addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(android.content.Intent.createChooser(intent, getString(R.string.send_report_via)))
        } catch (e: Exception) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.imagePreview.setImageURI(state.imageUri)
                    binding.iconVideoBadge.visibility = if (state.mediaType == "VIDEO") View.VISIBLE else View.GONE
                    
                    val currentPos = IssueCategory.entries.indexOf(state.selectedCategory)
                    if (binding.spinnerCategory.selectedItemPosition != currentPos) {
                        binding.spinnerCategory.setSelection(currentPos)
                    }

                    val institution = state.institution
                    binding.textVerifiedBadge.visibility = if (institution.verifiedContact) View.VISIBLE else View.GONE
                    
                    val note = if (!institution.verifiedContact || institution.email.isBlank())
                        getString(R.string.contact_unverified_note) else ""
                    binding.textInstitution.text = getString(R.string.will_be_sent_to, institution.name, note)

                    if (state.locationText != null) {
                        val displayLoc = state.readableAddress ?: state.locationText
                        binding.textLocation.text = getString(R.string.location_attached, displayLoc)
                    }
                    
                    binding.buttonAddLocation.isEnabled = !state.isLocationLoading && !state.isSubmitting
                    binding.buttonSendReport.isEnabled = !state.isSubmitting
                    binding.buttonSendEmail.isEnabled = !state.isSubmitting
                    binding.uploadProgress.visibility = if (state.isSubmitting) View.VISIBLE else View.GONE
                    binding.textUploadStatus.visibility = if (state.isSubmitting) View.VISIBLE else View.GONE
                    
                    if (state.submissionSuccess) {
                        Toast.makeText(this@ReportActivity, R.string.report_success, Toast.LENGTH_LONG).show()
                        finish()
                    }
                    
                    if (state.submissionError != null) {
                        Toast.makeText(this@ReportActivity, getString(R.string.report_error, state.submissionError), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
