package chris.zhang.worktimesheet.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class ClockinBean {

    //public static final

    private String dateId;
    private String onDutyTime;
    private String onDutyLocation;
    private String offDutyTime;
    private String offDutyLocation;

    public ClockinBean(String dateId) {
        this.dateId = dateId;
    }

    public ClockinBean(Cursor cursor) {
        this.dateId = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_DATE));
        this.onDutyTime = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ON_DUTY_TIME));
        this.onDutyLocation = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ON_DUTY_LOC));
        this.offDutyTime = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_OFF_DUTY_TIME));
        this.offDutyLocation = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_OFF_DUTY_LOC));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DATE, dateId);
        values.put(MySQLiteHelper.COLUMN_DATE, onDutyTime);
        values.put(MySQLiteHelper.COLUMN_DATE, onDutyLocation);
        values.put(MySQLiteHelper.COLUMN_DATE, offDutyTime);
        values.put(MySQLiteHelper.COLUMN_DATE, offDutyLocation);

        return values;
    }

    public String getDateId() {
        return dateId;
    }

    public String getOnDutyTime() {
        return onDutyTime;
    }

    public void setOnDutyTime(String onDutyTime) {
        this.onDutyTime = onDutyTime;
    }

    public String getOnDutyLocation() {
        return onDutyLocation;
    }

    public void setOnDutyLocation(String onDutyLocation) {
        this.onDutyLocation = onDutyLocation;
    }

    public String getOffDutyTime() {
        return offDutyTime;
    }

    public void setOffDutyTime(String offDutyTime) {
        this.offDutyTime = offDutyTime;
    }

    public String getOffDutyLocation() {
        return offDutyLocation;
    }

    public void setOffDutyLocation(String offDutyLocation) {
        this.offDutyLocation = offDutyLocation;
    }
}
