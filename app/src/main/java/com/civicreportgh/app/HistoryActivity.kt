package com.civicreportgh.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.civicreportgh.app.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        setupUI()
        observeViewModel()
        // Toast.makeText(this, "Reports opened", Toast.LENGTH_SHORT).show()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        
        binding.emptyState.btnStartReporting.setOnClickListener {
            finish() // Goes back to MainActivity where reporting starts
        }
    }

    private fun observeViewModel() {
        val adapter = HistoryAdapter { report, imageView ->
            val intent = Intent(this, ReportDetailActivity::class.java).apply {
                putExtra(ReportDetailActivity.EXTRA_REPORT_ID, report.id)
            }
            startActivity(intent)
        }
        binding.recyclerHistory.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reports.collect { reports ->
                    adapter.submitList(reports)
                    binding.emptyState.root.visibility = if (reports.isEmpty()) View.VISIBLE else View.GONE
                    binding.recyclerHistory.visibility = if (reports.isEmpty()) View.GONE else View.VISIBLE
                }
            }
        }
    }
}
