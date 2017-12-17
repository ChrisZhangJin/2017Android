package chris.zhang.mywidgets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/12/16.
 */

public class CalendarMonth {

    /**
     * 年月对应的一个月的所有日子（缓存）
     * HashMap 中key:年月字符串 value:一个月的所有日子
     */
    private static HashMap<String, CalendarMonth> months = new HashMap<>();

    private int year;
    private int month;
    private List<CalendarDay> calendarMonthDays = new ArrayList<>();
    private List<CalendarDay> monthDays = new ArrayList<>();

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

    public CalendarMonth(CalendarMonth copy) {
        this.year = copy.year;
        this.month = copy.month;
        this.calendarMonthDays = copy.calendarMonthDays;
        this.monthDays = copy.monthDays;
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
     * @param wholeWeek 是否按整周计算（日历模式）
     * @return 所有列表
     */
    public List<CalendarDay> getMonthDays(boolean wholeWeek) {
        return wholeWeek ? calendarMonthDays : monthDays;
    }

    public List<CalendarDay> getMonthDays() {
        return getMonthDays(false);
    }

    @Override
    public String toString() {
        return this.year + "" + (this.month+1);
    }

    private void initMonthDays() {
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
            CalendarDay lastMonthDay = new CalendarDay(year, month-1, -i);
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
