package com.civicreportgh.app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<LocalReport>>

    @Insert
    suspend fun insert(report: LocalReport): Long

    @Update
    suspend fun update(report: LocalReport)

    @Query("DELETE FROM reports WHERE id = :reportId")
    suspend fun delete(reportId: Long)

    @Query("SELECT * FROM reports WHERE id = :reportId")
    suspend fun getReportById(reportId: Long): LocalReport?
}
