package chris.zhang.worktime;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import chris.zhang.mywidgets.CalendarAdapter;
import chris.zhang.mywidgets.CalendarDay;
import chris.zhang.mywidgets.CalendarMonth;
import chris.zhang.mywidgets.CalendarPager;
import chris.zhang.mywidgets.CalendarView;
import chris.zhang.worktime.db.WorkTimeProvider;

public class MainActivity extends AppCompatActivity {

    private CalendarPager calendarPager;
    private TextView dateTitle;

    public static int px(float dipValue) {
        Resources r = Resources.getSystem();
        final float scale = r.getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateTitle = findViewById(R.id.date_title);

        HashMap<Integer, Integer> map = getWorkTimeMap(CalendarMonth.getCurrentMonth());

        calendarPager = (CalendarPager) findViewById(R.id.calendar_pager);
        calendarPager.setCalendarAdapter(new MyCalendarAdapter(map));
        calendarPager.setOnCalendarClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CalendarDay cday) {
                dateTitle.setText(cday.toString());

                if (cday.isFeature()) {
                    return;
                }

                if (cday.isToday()) {
                    startActivity(new Intent(IntentConstants.ACTION_CLOCKIN));
                } else {
                    // past days
                    Intent intent = new Intent(MainActivity.this, WorkTimeDetailsActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_CLOCKIN_DATE, cday);
                    startActivity(intent);
                }
            }
        });
        calendarPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private HashMap<Integer, Integer> getWorkTimeMap(CalendarMonth month) {
        Bundle extras = new Bundle();
        extras.putParcelable(WorkTimeProvider.EXTRA_MONTH, month);
        final Bundle bundle = getContentResolver().call(
                Uri.parse("content://chris.zhang.worktimeprovider/clockin"),
                WorkTimeProvider.METHOD_QUERY_MONTH_WORK_TIME,
                "",
                extras);

        if (bundle == null) return null;

        return (HashMap<Integer, Integer>)bundle.getSerializable(WorkTimeProvider.EXTRA_MAP_WORK_TIME);
    }

    class MyCalendarAdapter implements CalendarAdapter {
        private final HashMap<Integer, Integer> map;

        public MyCalendarAdapter(HashMap<Integer, Integer> map) {
            this.map = map;
        }

        @Override
        public View getView(View convertView, ViewGroup parentView, CalendarDay cDay, CalendarMonth cMonth) {

            if (convertView == null) {
                convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.calendar_item, null);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(px(56), px(56));
                convertView.setLayoutParams(lp);
            }

            TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
            text1.setText(String.valueOf(cDay.getDay()));

            if (cDay.isInThisMonth(cMonth)) {
                text1.setTextColor(0xffffffff);
            } else {
                text1.setTextColor(0xff9299a1);
            }

            TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
            if (map != null) {
                final int id = Integer.parseInt(cDay.toString());
                if (map.containsKey(id)) {
                    int result = map.get(id);
                    text2.setText(String.valueOf(result));
                    text2.setTextColor(Color.GREEN);
                }

            }

            return convertView;
        }

    }
}
