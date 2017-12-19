package chris.zhang.mywidgets;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/12/16.
 */

public class CalendarMonth implements Parcelable {

    /**
     * 年月对应的一个月的所有日子（缓存）
     * HashMap 中key:年月字符串 value:一个月的所有日子
     */
    protected static HashMap<String, CalendarMonth> months = new HashMap<>();

    protected int year;
    protected int month;
    protected List<CalendarDay> calendarMonthDays = new ArrayList<>();
    protected List<CalendarDay> monthDays = new ArrayList<>();

    public static Parcelable.Creator<CalendarMonth> CREATOR = new Parcelable.Creator<CalendarMonth>() {
        @Override
        public CalendarMonth createFromParcel(Parcel source) {
            return new CalendarMonth(source);
        }

        @Override
        public CalendarMonth[] newArray(int size) {
            return new CalendarMonth[size];
        }
    };

    public CalendarMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);

        initMonthDays();
    }

    public CalendarMonth(int year, int month) {
        this.year = year;
        // valid range: 0~11
        if (month >= 0 && month <= 11) {
            this.month = month;
        } else {
            this.month = 0;
        }
        initMonthDays();
    }

    public CalendarMonth(CalendarDay cday) {
        this.year = cday.getYear();
        this.month = cday.getMonth();
        initMonthDays();
    }

    public CalendarMonth(Parcel source) {
        this.year = source.readInt();
        this.month = source.readInt();
        initMonthDays();
    }

    public CalendarMonth(CalendarMonth copy) {
        this.year = copy.year;
        this.month = copy.month;
        this.calendarMonthDays = copy.calendarMonthDays;
        this.monthDays = copy.monthDays;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
    }

    public static CalendarMonth getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        return new CalendarMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
    }

    public CalendarMonth addMonth(int month) {
        if (month == 0) return this;

        int y = month / 12;
        int m = month % 12;

        this.month += m;
        this.year += y;
        if (this.month >= 12) {
            this.month -= 12;
            this.year++;
        }
        initMonthDays();
        return this;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getWeekdayOfFirstDay() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);

        return c.get(Calendar.DAY_OF_WEEK);
    }

    public int getDaysCount() {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);

        return c.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取一个月中的所有日子
     *
     * @return 所有列表
     */
    public List<CalendarDay> getCalendarMonthDays() {
        return calendarMonthDays;
    }

    public List<CalendarDay> getMonthDays() {
        return monthDays;
    }

    @Override
    public String toString() {
        return this.year + "" + (this.month + 1);
    }

    protected void initMonthDays() {
        final String key = toString();
        if (months.containsKey(key)) {
            CalendarMonth calendarMonth = months.get(key);
            this.year = calendarMonth.year;
            this.month = calendarMonth.month;
            this.calendarMonthDays = calendarMonth.calendarMonthDays;
            this.monthDays = calendarMonth.monthDays;
            return;
        } else {
            calendarMonthDays = new ArrayList<>();
            monthDays = new ArrayList<>();
        }

        final int days = CalendarDay.getDaysByWeekday(getWeekdayOfFirstDay());
        for (int i = days; i > 0; i--) {
            CalendarDay lastMonthDay = new CalendarDay(year, month - 1, -i);
            calendarMonthDays.add(lastMonthDay);
        }

        final int totalDays = getDaysCount();
        for (int i = 1; i <= totalDays; i++) {
            CalendarDay cday = new CalendarDay(year, month, i);
            calendarMonthDays.add(cday);
            monthDays.add(cday);
        }

        months.put(key, this);
    }
}
