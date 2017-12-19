package chris.zhang.worktime.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "worktime.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + Database.TableClockin.TABLE_NAME + "( "
            + Database.TableClockin.COLUMN_DATE + " integer primary key, "
            + Database.TableClockin.COLUMN_ON_DUTY_TIME + " integer, "
            + Database.TableClockin.COLUMN_ON_DUTY_LOC + " text, "
            + Database.TableClockin.COLUMN_OFF_DUTY_TIME + " integer, "
            + Database.TableClockin.COLUMN_OFF_DUTY_LOC + " text); ";

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
