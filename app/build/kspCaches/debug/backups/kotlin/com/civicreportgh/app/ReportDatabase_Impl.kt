package com.civicreportgh.app

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ReportDatabase_Impl : ReportDatabase() {
  private val _reportDao: Lazy<ReportDao> = lazy {
    ReportDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(4, "458e0a38d17625869023497abaa9cdf0", "bd98a6091b872846ce90dc4cc2790e58") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `reports` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `imageUrl` TEXT NOT NULL, `videoUrl` TEXT, `mediaType` TEXT NOT NULL, `additionalImageUrls` TEXT, `category` TEXT NOT NULL, `description` TEXT NOT NULL, `location` TEXT, `timestamp` INTEGER NOT NULL, `institution` TEXT NOT NULL, `isSynced` INTEGER NOT NULL, `status` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '458e0a38d17625869023497abaa9cdf0')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `reports`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsReports: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsReports.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("imageUrl", TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("videoUrl", TableInfo.Column("videoUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("mediaType", TableInfo.Column("mediaType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("additionalImageUrls", TableInfo.Column("additionalImageUrls", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("category", TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("description", TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("location", TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("institution", TableInfo.Column("institution", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("isSynced", TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReports.put("status", TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysReports: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesReports: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoReports: TableInfo = TableInfo("reports", _columnsReports, _foreignKeysReports, _indicesReports)
        val _existingReports: TableInfo = read(connection, "reports")
        if (!_infoReports.equals(_existingReports)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |reports(com.civicreportgh.app.LocalReport).
              | Expected:
              |""".trimMargin() + _infoReports + """
              |
              | Found:
              |""".trimMargin() + _existingReports)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "reports")
  }

  public override fun clearAllTables() {
    super.performClear(false, "reports")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ReportDao::class, ReportDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun reportDao(): ReportDao = _reportDao.value
}
