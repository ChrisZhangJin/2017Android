package chris.zhang.worktime.db;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public final class Database {
    public static class TableClockin {
        public static final String TABLE_NAME = "clockin";
        public static final String COLUMN_DATE = "date_id";
        public static final String COLUMN_ON_DUTY_TIME = "time_on_duty";
        public static final String COLUMN_ON_DUTY_LOC = "loc_on_duty";
        public static final String COLUMN_OFF_DUTY_TIME = "time_off_duty";
        public static final String COLUMN_OFF_DUTY_LOC = "loc_off_duty";
        public static final String[] TABLE_CLOCKIN_COLUMNS = new String[]{
                COLUMN_DATE,
                COLUMN_ON_DUTY_TIME,
                COLUMN_ON_DUTY_LOC,
                COLUMN_ON_DUTY_LOC,
                COLUMN_OFF_DUTY_TIME,
                COLUMN_OFF_DUTY_LOC};
    }
}
