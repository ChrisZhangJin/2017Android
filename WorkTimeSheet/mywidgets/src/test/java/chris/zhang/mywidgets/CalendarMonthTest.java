package chris.zhang.mywidgets;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2017/12/16.
 */
public class CalendarMonthTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetDaysCount() {
        CalendarMonth testObj = new CalendarMonth(2017, 11);
        assertEquals(31, testObj.getDaysCount());
    }

    @Test
    public void testGetWeekdayOfFirstDay() {
        CalendarMonth testObj = new CalendarMonth(2017, 11);
        assertEquals(Calendar.FRIDAY, testObj.getWeekdayOfFirstDay());
    }

    @Test
    public void testGetMonthDays() {
        CalendarMonth testObj = new CalendarMonth(2017, 11);

        List<CalendarDay> list = testObj.getMonthDays();
        assertEquals(31, list.size());

        assertEquals("2017/12/1", list.get(0).toString());
        assertEquals("2017/12/31", list.get(list.size() - 1).toString());
    }

    @Test
    public void testGetMonthDaysByWholeWeeks() {
        CalendarMonth testObj = new CalendarMonth(2017, 11);

        List<CalendarDay> list = testObj.getCalendarMonthDays();
        assertEquals(list.size(), 36);

        assertEquals("2017/11/26", list.get(0).toString());
        assertEquals("2017/12/31", list.get(list.size() - 1).toString());
    }

    @Test
    public void testAddMonth() {

        CalendarMonth testObj = new CalendarMonth(2017, 11);
        testObj.addMonth(1);

        List<CalendarDay> list = testObj.getMonthDays();
        assertEquals(2018, testObj.getYear());
        assertEquals(0, testObj.getMonth());
        assertEquals(31, list.size());
    }

    @Test
    public void testGetCurrentMonth() {
        CalendarMonth testObj = CalendarMonth.getCurrentMonth();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        assertEquals(c.get(Calendar.YEAR), testObj.getYear());
        assertEquals(c.get(Calendar.MONTH), testObj.getMonth());
    }
}