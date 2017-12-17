package chris.zhang.mywidgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/12/16.
 */

public class CalendarView extends ViewGroup {
    private static final String TAG = "CalendarView";
    private final static int COLUMN_COUNT = 7;

    private CalendarMonth calendarMonth;
    private int row;
    private CalendarAdapter adapter;
    private int selectPosition;
    private boolean isToday;
    private OnItemClickListener listener;
    private List<CalendarDay> data;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, CalendarDay cday);
    }

    public CalendarView(Context context, @NonNull CalendarMonth month) {
        super(context);
        calendarMonth = month;
        this.row = (month.getMonthDays(true).size()-1) / COLUMN_COUNT + 1;
    }

    public void setAdapter(@NonNull CalendarAdapter adapter) {
        this.adapter = adapter;
    }

    public void setData(@NonNull List<CalendarDay> data, boolean isToday) {
        this.data = data;
        this.isToday = isToday;

        initItems(data);
        requestLayout();
    }

    public void setItemClick(final View view, final int position, final CalendarDay cday) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectPosition != -1) {
                    getChildAt(selectPosition).setSelected(false);
                    getChildAt(position).setSelected(true);
                }
                selectPosition = position;

                if (listener != null) {
                    listener.onItemClick(view, position, cday);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        //int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        int itemWidth = parentWidth / COLUMN_COUNT;
        int itemHeight = itemWidth;

        // 如果该Group为空，则直接返回
        View view = getChildAt(0);
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params.height > 0) {
            itemHeight = params.height;
        }
        setMeasuredDimension(parentWidth, itemHeight * row);

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));
        }
        Log.i(TAG, "onMeasure() called with: itemHeight = [" + itemHeight + "], itemWidth = [" + itemWidth + "]");
    }

    public CalendarDay getSelected() {
        return data.get(selectPosition);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            layoutChild(getChildAt(i), i, l, t, r, b);
        }

        Log.i(TAG, "onLayout");
    }

    private void layoutChild(View view, int position, int l, int t, int r, int b) {
        // 通过index计算应该排在哪一个位置
        int colIndex = position % COLUMN_COUNT;
        int rowIndex = position / COLUMN_COUNT;

        int itemWidth = view.getMeasuredWidth();
        int itemHeight = view.getMeasuredHeight();

        l = colIndex * itemWidth;
        t = rowIndex * itemHeight;
        r = l + itemWidth;
        b = t + itemHeight;
        view.layout(l, t, r, b);
    }

    private void initItems(List<CalendarDay> list) {

        selectPosition = -1;
        if (adapter == null) {
            throw new RuntimeException("adapter is null,please setadapter");
        }

        for (int i = 0; i < list.size(); i++) {
            CalendarDay calendarDay = list.get(i);
            View view = getChildAt(i);
            View childView = adapter.getView(view, this, calendarDay);

            if (view == null || view != childView) {
                addViewInLayout(childView, i, childView.getLayoutParams(), true);
            }

            if (calendarDay.isToday() && selectPosition == -1) {
                    selectPosition = i;
            } else {
                if (selectPosition == -1 && calendarDay.getDay() == 1) {
                    selectPosition = i;
                }
            }

            childView.setSelected(selectPosition == i);

            setItemClick(childView, i, calendarDay);
        }
    }
}
