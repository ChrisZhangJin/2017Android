package chris.zhang.mywidgets;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Administrator on 2017/12/16.
 */

public class CalendarPager extends ViewPager {

    private HashMap<Integer, CalendarView> views = new HashMap<>();
    private CalendarAdapter calendarAdapter;
    private CalendarView.OnItemClickListener onItemClickListener;
    private LinkedList<CalendarView> cache = new LinkedList();
    private PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            CalendarView view;
            if (cache.isEmpty()) {
                view = new CalendarView(container.getContext(), CalendarMonth.getCurrentMonth());
            } else {
                view = cache.removeFirst();
            }

            view.setAdapter(calendarAdapter);
            view.setData(CalendarMonth.getCurrentMonth().addMonth(position - Integer.MAX_VALUE / 2).getMonthDays(true), position == Integer.MAX_VALUE / 2);
            container.addView(view);
            views.put(position, view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            cache.addLast((CalendarView) object);
            views.remove(object);
        }
    };

    public CalendarPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public void setCalendarAdapter(CalendarAdapter adapter) {
        this.calendarAdapter = adapter;
        initData();
    }

    private void init() {
        setAdapter(pagerAdapter);

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (onItemClickListener != null) {
                    CalendarView view = views.get(position);
                    onItemClickListener.onItemClick(view, position, view.getSelected());
                }
            }
        });
    }

    private void initData() {
        setCurrentItem(Integer.MAX_VALUE / 2, false);
        getAdapter().notifyDataSetChanged();
    }
}
