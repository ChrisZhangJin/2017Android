package chris.zhang.worktimesheet;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chris.zhang.mywidgets.CalendarAdapter;
import chris.zhang.mywidgets.CalendarDay;
import chris.zhang.mywidgets.CalendarMonth;
import chris.zhang.mywidgets.CalendarPager;
import chris.zhang.mywidgets.CalendarView;

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

        calendarPager = (CalendarPager) findViewById(R.id.calendar_pager);
        calendarPager.setCalendarAdapter(new CalendarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarDay cDay, CalendarMonth cMonth) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.calendar_item, null);
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(px(48), px(48));
                    convertView.setLayoutParams(lp);
                }

                view = (TextView) convertView.findViewById(android.R.id.text1);
                view.setText(String.valueOf(cDay.getDay()));

                if (cDay.isInThisMonth(cMonth)) {
                    view.setTextColor(0xffffffff);
                } else {
                    view.setTextColor(0xff9299a1);
                }

                return convertView;
            }
        });
        calendarPager.setOnCalendarClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CalendarDay cday) {
                dateTitle.setText(cday.toString());

                if (cday.isFeature()) {
                    return;
                }

                Intent intent = new Intent(IntentConstants.ACTION_CLOCKIN);
                if (cday.isToday()) {
                    startActivity(intent);
                } else {
                    // past days
                    intent.putExtra(IntentConstants.EXTRA_CLOCKIN_DATE, cday.toString());
                    startActivity(intent);
                }
            }
        });
    }
}
