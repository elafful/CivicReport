package com.civicreportgh.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.flow.catch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ReportDatabase.getDatabase(application).reportDao()

    val reports: StateFlow<List<LocalReport>> = dao.getAllReports()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
