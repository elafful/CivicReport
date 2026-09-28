package com.civicreportgh.app

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.civicreportgh.app.databinding.ItemReportBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private val onItemClick: (LocalReport, ImageView) -> Unit) : 
    ListAdapter<LocalReport, HistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemReportBinding, 
        private val onItemClick: (LocalReport, ImageView) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

        fun bind(report: LocalReport) {
            binding.root.setOnClickListener { onItemClick(report, binding.imageReport) }
            binding.textCategory.text = report.category
            binding.textDate.text = dateFormat.format(Date(report.timestamp))
            binding.textInstitution.text = report.institution
            
            binding.textStatus.text = when {
                report.isSynced -> "Submitted to Cloud"
                report.status == "PENDING" -> "Cloud Sync Pending"
                else -> "Sent via Email"
            }
            
            binding.imageReport.transitionName = "report_image_${report.id}"
            
            binding.imageReport.load(report.imageUrl) {
                crossfade(true)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<LocalReport>() {
        override fun areItemsTheSame(oldItem: LocalReport, newItem: LocalReport): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: LocalReport, newItem: LocalReport): Boolean = oldItem == newItem
    }
}
