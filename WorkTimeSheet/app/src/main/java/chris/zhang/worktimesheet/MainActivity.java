package chris.zhang.worktimesheet;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chris.zhang.mywidgets.CalendarAdapter;
import chris.zhang.mywidgets.CalendarDay;
import chris.zhang.mywidgets.CalendarPager;

public class MainActivity extends AppCompatActivity {

    private CalendarPager calendarPager;

    public static int px(float dipValue) {
        Resources r = Resources.getSystem();
        final float scale = r.getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarPager = (CalendarPager) findViewById(R.id.calendar_pager);
        calendarPager.setCalendarAdapter(new CalendarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarDay cday) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(android.R.layout.simple_list_item_1, null);
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(px(48), px(48));
                    convertView.setLayoutParams(lp);
                }

                view = (TextView) convertView.findViewById(android.R.id.text1);
                view.setText(String.valueOf(cday.getDay()));

                return convertView;
            }
        });
    }
}
