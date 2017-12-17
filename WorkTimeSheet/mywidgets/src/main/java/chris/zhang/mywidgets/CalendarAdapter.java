package chris.zhang.mywidgets;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/12/16.
 */

public interface CalendarAdapter {
    View getView(View convertView, ViewGroup parentView, CalendarDay cday);
}
