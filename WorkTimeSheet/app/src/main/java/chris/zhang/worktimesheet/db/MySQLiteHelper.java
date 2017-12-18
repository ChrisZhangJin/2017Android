package chris.zhang.worktimesheet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "clockin";
    public static final String COLUMN_DATE = "date_id";
    public static final String COLUMN_ON_DUTY_TIME = "time_on_duty";
    public static final String COLUMN_ON_DUTY_LOC = "loc_on_duty";
    public static final String COLUMN_OFF_DUTY_TIME = "time_off_duty";
    public static final String COLUMN_OFF_DUTY_LOC = "loc_off_duty";

    private static final String DATABASE_NAME = "worktimesheet.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( "
            + COLUMN_DATE + " text primary key, "
            + COLUMN_ON_DUTY_TIME + " text, "
            + COLUMN_ON_DUTY_LOC + " text, "
            + COLUMN_OFF_DUTY_TIME + " text, "
            + COLUMN_OFF_DUTY_LOC + " text); ";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
