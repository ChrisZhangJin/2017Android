package chris.zhang.worktimesheet.db;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class WorkTimeProvider extends ContentProvider {
    private static final int URI_TYPE_NEW_CLOCKIN = 1;
    private static final int URI_TYPE_CLOCKIN = 2;
    private static final int URI_TYPE_QUERY_CLOCKIN = 3;

//    public static final String AUTHORITY = "chris.zhang.worktimeprovider";

    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

//    static {
//        uriMatcher.addURI(AUTHORITY, "clockin/#", URI_TYPE_CLOCKIN);
//        uriMatcher.addURI(AUTHORITY, "clockin", URI_TYPE_NEW_CLOCKIN);
//    }

    private ContentResolver cr;
    private SQLiteDatabase db;

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        super.attachInfo(context, info);

        uriMatcher.addURI(info.authority, "clockin/#", URI_TYPE_CLOCKIN);
        uriMatcher.addURI(info.authority, "clockin", URI_TYPE_NEW_CLOCKIN);
        uriMatcher.addURI(info.authority, "query/clockin", URI_TYPE_QUERY_CLOCKIN);
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
        if (uriMatcher.match(uri) == URI_TYPE_QUERY_CLOCKIN) {
            cursor = db.query(MySQLiteHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(cr, uri);
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
        if (uriMatcher.match(uri) == URI_TYPE_CLOCKIN) {
            long d = db.insert(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_DATE, values);
            String id = (String) values.get(MySQLiteHelper.COLUMN_DATE);
            u = Uri.withAppendedPath(uri, id);
            cr.notifyChange(u, null);
        }
        return u;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int id = 0;
        if (uriMatcher.match(uri) == URI_TYPE_CLOCKIN) {
            id = db.delete(MySQLiteHelper.TABLE_NAME, selection, selectionArgs);
            cr.notifyChange(uri, null);
        }
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int d = 0;
        if (uriMatcher.match(uri) == URI_TYPE_CLOCKIN) {
            d = db.update(MySQLiteHelper.TABLE_NAME, values, selection, selectionArgs);
            cr.notifyChange(uri, null);
        }
        return d;
    }
}
