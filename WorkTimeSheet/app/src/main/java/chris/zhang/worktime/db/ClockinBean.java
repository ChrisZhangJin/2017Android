package chris.zhang.worktime.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class ClockinBean {

    //public static final

    private int dateId;
    private long onDutyTime; // keep the seconds of time
    private String onDutyLocation = "";
    private long offDutyTime; // keep the seconds of time
    private String offDutyLocation = "";

    public ClockinBean(int dateId) {
        this.dateId = dateId;
    }

    public ClockinBean(Cursor cursor) {
        this.dateId = cursor.getInt(cursor.getColumnIndex(Database.TableClockin.COLUMN_DATE));
        this.onDutyTime = cursor.getLong(cursor.getColumnIndex(Database.TableClockin.COLUMN_ON_DUTY_TIME));
        this.onDutyLocation = cursor.getString(cursor.getColumnIndex(Database.TableClockin.COLUMN_ON_DUTY_LOC));
        this.offDutyTime = cursor.getLong(cursor.getColumnIndex(Database.TableClockin.COLUMN_OFF_DUTY_TIME));
        this.offDutyLocation = cursor.getString(cursor.getColumnIndex(Database.TableClockin.COLUMN_OFF_DUTY_LOC));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Database.TableClockin.COLUMN_DATE, dateId);
        values.put(Database.TableClockin.COLUMN_ON_DUTY_TIME, onDutyTime);
        values.put(Database.TableClockin.COLUMN_ON_DUTY_LOC, onDutyLocation);
        values.put(Database.TableClockin.COLUMN_OFF_DUTY_TIME, offDutyTime);
        values.put(Database.TableClockin.COLUMN_OFF_DUTY_LOC, offDutyLocation);

        return values;
    }

    public int getDateId() {
        return dateId;
    }

    public String getOnDutyTimeStr() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(onDutyTime * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(c.getTime());
    }

    public long getOnDutyTime() {
        return this.onDutyTime;
    }

    public void setOnDutyTime(long onDutyTime) {
        this.onDutyTime = onDutyTime;
    }

    public void setOnDutyTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        this.onDutyTime = c.getTimeInMillis() / 1000;
    }

    public String getOnDutyLocation() {
        return onDutyLocation;
    }

    public void setOnDutyLocation(String onDutyLocation) {
        this.onDutyLocation = onDutyLocation;
    }

    public String getOffDutyTimeStr() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(offDutyTime * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(c.getTime());
    }

    public long getOffDutyTime() {
        return this.offDutyTime;
    }

    public void setOffDutyTime(long offDutyTime) {
        this.offDutyTime = offDutyTime;
    }

    public void setOffDutyTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        this.offDutyTime = c.getTimeInMillis() / 1000;
    }

    public String getOffDutyLocation() {
        return offDutyLocation;
    }

    public void setOffDutyLocation(String offDutyLocation) {
        this.offDutyLocation = offDutyLocation;
    }

}
