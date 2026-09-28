package com.civicreportgh.app

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ReportDao_Impl(
  __db: RoomDatabase,
) : ReportDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfLocalReport: EntityInsertAdapter<LocalReport>
  init {
    this.__db = __db
    this.__insertAdapterOfLocalReport = object : EntityInsertAdapter<LocalReport>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `reports` (`id`,`imageUrl`,`category`,`description`,`location`,`timestamp`,`institution`,`isSynced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: LocalReport) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.imageUrl)
        statement.bindText(3, entity.category)
        statement.bindText(4, entity.description)
        val _tmpLocation: String? = entity.location
        if (_tmpLocation == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpLocation)
        }
        statement.bindLong(6, entity.timestamp)
        statement.bindText(7, entity.institution)
        val _tmp: Int = if (entity.isSynced) 1 else 0
        statement.bindLong(8, _tmp.toLong())
      }
    }
  }

  public override suspend fun insert(report: LocalReport): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfLocalReport.insert(_connection, report)
  }

  public override fun getAllReports(): Flow<List<LocalReport>> {
    val _sql: String = "SELECT * FROM reports ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("reports")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _columnIndexOfInstitution: Int = getColumnIndexOrThrow(_stmt, "institution")
        val _columnIndexOfIsSynced: Int = getColumnIndexOrThrow(_stmt, "isSynced")
        val _result: MutableList<LocalReport> = mutableListOf()
        while (_stmt.step()) {
          val _item: LocalReport
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpImageUrl: String
          _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpLocation: String?
          if (_stmt.isNull(_columnIndexOfLocation)) {
            _tmpLocation = null
          } else {
            _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          val _tmpInstitution: String
          _tmpInstitution = _stmt.getText(_columnIndexOfInstitution)
          val _tmpIsSynced: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsSynced).toInt()
          _tmpIsSynced = _tmp != 0
          _item = LocalReport(_tmpId,_tmpImageUrl,_tmpCategory,_tmpDescription,_tmpLocation,_tmpTimestamp,_tmpInstitution,_tmpIsSynced)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(reportId: Long) {
    val _sql: String = "DELETE FROM reports WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, reportId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
