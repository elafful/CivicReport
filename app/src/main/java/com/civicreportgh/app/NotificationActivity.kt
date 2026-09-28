package com.civicreportgh.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.civicreportgh.app.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadNotifications()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.recyclerNotifications.layoutManager = LinearLayoutManager(this)
    }

    private fun loadNotifications() {
        // Mock data for now
        val mockNotifications = listOf(
            AppNotification("1", "Welcome to CivicReport GH", "Thank you for joining our community to improve our cities.", System.currentTimeMillis()),
            AppNotification("2", "Report Received", "Your report about 'Road damage' in Accra has been successfully received.", System.currentTimeMillis() - 3600000)
        )

        if (mockNotifications.isEmpty()) {
            binding.textEmpty.visibility = View.VISIBLE
            binding.recyclerNotifications.visibility = View.GONE
        } else {
            binding.textEmpty.visibility = View.GONE
            binding.recyclerNotifications.visibility = View.VISIBLE
            binding.recyclerNotifications.adapter = NotificationAdapter(mockNotifications)
        }
    }
}
