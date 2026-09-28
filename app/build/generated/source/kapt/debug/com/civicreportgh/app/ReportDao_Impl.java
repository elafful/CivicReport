package com.civicreportgh.app;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ReportDao_Impl implements ReportDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<LocalReport> __insertAdapterOfLocalReport;

  public ReportDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfLocalReport = new EntityInsertAdapter<LocalReport>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `reports` (`id`,`imageUrl`,`category`,`description`,`location`,`timestamp`,`institution`,`isSynced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final LocalReport entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getImageUrl() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getImageUrl());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getCategory());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getDescription());
        }
        if (entity.getLocation() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getLocation());
        }
        statement.bindLong(6, entity.getTimestamp());
        if (entity.getInstitution() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getInstitution());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
  }

  @Override
  public Object insert(final LocalReport report, final Continuation<? super Unit> $completion) {
    if (report == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __insertAdapterOfLocalReport.insert(_connection, report);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Flow<List<LocalReport>> getAllReports() {
    final String _sql = "SELECT * FROM reports ORDER BY timestamp DESC";
    return FlowUtil.createFlow(__db, false, new String[] {"reports"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfImageUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "imageUrl");
        final int _columnIndexOfCategory = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "category");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfLocation = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "location");
        final int _columnIndexOfTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "timestamp");
        final int _columnIndexOfInstitution = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "institution");
        final int _columnIndexOfIsSynced = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isSynced");
        final List<LocalReport> _result = new ArrayList<LocalReport>();
        while (_stmt.step()) {
          final LocalReport _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpImageUrl;
          if (_stmt.isNull(_columnIndexOfImageUrl)) {
            _tmpImageUrl = null;
          } else {
            _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl);
          }
          final String _tmpCategory;
          if (_stmt.isNull(_columnIndexOfCategory)) {
            _tmpCategory = null;
          } else {
            _tmpCategory = _stmt.getText(_columnIndexOfCategory);
          }
          final String _tmpDescription;
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null;
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription);
          }
          final String _tmpLocation;
          if (_stmt.isNull(_columnIndexOfLocation)) {
            _tmpLocation = null;
          } else {
            _tmpLocation = _stmt.getText(_columnIndexOfLocation);
          }
          final long _tmpTimestamp;
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp);
          final String _tmpInstitution;
          if (_stmt.isNull(_columnIndexOfInstitution)) {
            _tmpInstitution = null;
          } else {
            _tmpInstitution = _stmt.getText(_columnIndexOfInstitution);
          }
          final boolean _tmpIsSynced;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfIsSynced));
          _tmpIsSynced = _tmp != 0;
          _item = new LocalReport(_tmpId,_tmpImageUrl,_tmpCategory,_tmpDescription,_tmpLocation,_tmpTimestamp,_tmpInstitution,_tmpIsSynced);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Object delete(final long reportId, final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM reports WHERE id = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, reportId);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
