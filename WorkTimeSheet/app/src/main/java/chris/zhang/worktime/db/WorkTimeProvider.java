package chris.zhang.worktime.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

import chris.zhang.mywidgets.CalendarDay;
import chris.zhang.mywidgets.CalendarMonth;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class WorkTimeProvider extends ContentProvider {
    public static final String METHOD_QUERY_MONTH_WORK_TIME = "query_month_work_time";
    public static final String EXTRA_MONTH = "month";
    public static final String EXTRA_MAP_WORK_TIME = "mapworktime";

    private static final int URI_TYPE_NEW_CLOCKIN = 1;
    private static final int URI_TYPE_CLOCKIN = 2;

    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private ContentResolver cr;
    private SQLiteDatabase db;

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);

        uriMatcher.addURI(info.authority, "clockin/#", URI_TYPE_CLOCKIN);
        uriMatcher.addURI(info.authority, "clockin", URI_TYPE_NEW_CLOCKIN);
    }

    @Override
    public boolean onCreate() {
        MySQLiteHelper dbHelper = new MySQLiteHelper(getContext());
        db = dbHelper.getReadableDatabase();

        cr = getContext().getContentResolver();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        if (uriMatcher.match(uri) == URI_TYPE_CLOCKIN) {
            if (selection == null) {
                selection = Database.TableClockin.COLUMN_DATE + "=" + ContentUris.parseId(uri);
            }
            try {
                cursor = db.query(Database.TableClockin.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(cr, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri u = null;
        if (uriMatcher.match(uri) == URI_TYPE_NEW_CLOCKIN) {
            long d = db.insert(Database.TableClockin.TABLE_NAME, null, values);
            int id = (int) values.get(Database.TableClockin.COLUMN_DATE);
            u = ContentUris.withAppendedId(uri, id);
            cr.notifyChange(u, null);
        }
        return u;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int id = 0;
        if (uriMatcher.match(uri) == URI_TYPE_CLOCKIN) {
            id = db.delete(Database.TableClockin.TABLE_NAME, selection, selectionArgs);
            cr.notifyChange(uri, null);
        }
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int d = 0;
        if (uriMatcher.match(uri) == URI_TYPE_CLOCKIN) {
            d = db.update(Database.TableClockin.TABLE_NAME, values, selection, selectionArgs);
            cr.notifyChange(uri, null);
        }
        return d;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        switch (method) {
            case METHOD_QUERY_MONTH_WORK_TIME:
                if (extras == null) {
                    return null;
                }

                CalendarMonth month = extras.getParcelable(EXTRA_MONTH);
                if (month == null) {
                    return null;
                }

                return processQueryMonthWorkTime(month);
            default:
                break;
        }
        return super.call(method, arg, extras);
    }

    private Bundle processQueryMonthWorkTime(@NonNull CalendarMonth month) {
        List<CalendarDay> list = month.getCalendarMonthDays();
        final int length = list.size();
        final int fd = Integer.parseInt(list.get(0).toString());
        final int ld = Integer.parseInt(list.get(length - 1).toString());

        final String sql = Database.TableClockin.COLUMN_DATE + " >= " + fd + " and " + Database.TableClockin.COLUMN_DATE + " <= " + ld;
        Cursor cursor = null;
        try {
            cursor = db.query(Database.TableClockin.TABLE_NAME, Database.TableClockin.TABLE_CLOCKIN_COLUMNS, sql, null, null, null, null);
            if (cursor == null || cursor.getCount() == 0) {
                return null;
            }

            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
            while (cursor.moveToNext()) {
                ClockinBean bean = new ClockinBean(cursor);
                if (bean.getOffDutyTime() == 0 || bean.getOnDutyTime() == 0) {
                    map.put(bean.getDateId(), 0);
                } else {
                    final int hours = (int) ((bean.getOffDutyTime() - bean.getOnDutyTime()) / 3600);
                    map.put(bean.getDateId(), hours);
                }
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable(EXTRA_MAP_WORK_TIME, map);
            return bundle;
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception ex) {

                }
            }
        }

        return null;
    }
}
