package chris.zhang.mywidgets;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/16.
 */

public class CalendarDay {
    private int year;
    private int month;
    private int day;
    private int weekday;

    //-1,0,1
    private int monthFlag;

    //显示
    private String chinaMonth;
    private String chinaDay;

    public CalendarDay(int year, int month, int day) {
        this.year = year;
        this.month = convertMinusMonth(month);
        this.day = convertMinusDay(day);
    }

    //获取一月的第一天是星期几
    public static int getDayOfWeek(int y, int m, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDaysByWeekday(int weekday) {
        switch (weekday) {
            case Calendar.SUNDAY:
                return 7;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
        }
        return 0;
    }

    public static CalendarDay getToday() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        return new CalendarDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getWeekday() {
        return weekday;
    }

    public boolean isToday() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        return this.year == c.get(Calendar.YEAR) && this.month == c.get(Calendar.MONTH) && this.day == c.get(Calendar.DATE);
    }

    public String getNameOfWeekday(Context context) {
        String s = "";
        switch (weekday) {
            case Calendar.SUNDAY:
                s = context.getResources().getString(R.string.weekday_sunday);
                break;
            case Calendar.MONDAY:
                s = context.getResources().getString(R.string.weekday_monday);
                break;
            case Calendar.TUESDAY:
                s = context.getResources().getString(R.string.weekday_tuesday);
                break;
            case Calendar.WEDNESDAY:
                s = context.getResources().getString(R.string.weekday_wednesday);
                break;
            case Calendar.THURSDAY:
                s = context.getResources().getString(R.string.weekday_thursday);
                break;
            case Calendar.FRIDAY:
                s = context.getResources().getString(R.string.weekday_friday);
                break;
            case Calendar.SATURDAY:
                s = context.getResources().getString(R.string.weekday_saturday);
                break;
        }
        return s;
    }

    @Override
    public String toString() {
        String s = year + "/" + (month + 1) + "/" + day;
        return s;
    }

    private int convertMinusMonth(int month) {
        if (month >= 0) return month;

        if (month <= -12) return 0;

        return 12 + month;
    }

    private int convertMinusDay(int day) {
        if (day > 0) return day;

        if (day == 0) return 1;

        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        final int total = c.getActualMaximum(Calendar.DATE);

        if (total + day + 1 <= 0) return 1;

        return total + day + 1;
    }
}
