package chris.zhang.worktimesheet;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chris.zhang.worktimesheet.db.ClockinBean;

public class ClockInActivity extends AppCompatActivity {

    @BindView(R.id.clockin)
    Button clockin;

    @BindView(R.id.current_date)
    TextView curDate;

    @BindView(R.id.first_clockin_time)
    TextView firstClockin;

    @BindView(R.id.last_clockin_time)
    TextView lastClockin;

    private boolean hasRecord;
    private ClockinBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);
        ButterKnife.bind(this);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String curDateStr = sdf.format(new Date());
        curDate.setText(curDateStr);

        ContentResolver cr = getContentResolver();
        Cursor cursor = null;
        try {
            cursor = cr.query(Uri.parse("content://chris.zhang.worktimeprovider/clockin/" + curDateStr), null, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                hasRecord = false;
                bean = new ClockinBean(curDateStr);
            } else {
                hasRecord = true;
                bean = new ClockinBean(cursor);
                initUI(bean);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {

                }
            }
        }
    }

    // TODO: to change implementation to asynchronization
    @OnClick(R.id.clockin)
    void doClockin() {
        ContentResolver cr = getContentResolver();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        final String curTimeStr = sdf.format(new Date());
        if (hasRecord) {
            bean.setOffDutyTime(curTimeStr);
            cr.update(Uri.parse("content://chris.zhang.worktimeprovider/query/clockin"), bean.toContentValues(), null, null);
        } else {
            bean.setOnDutyTime(curTimeStr);
            cr.insert(Uri.parse("content://chris.zhang.worktimeprovider/clockin"), bean.toContentValues());
        }

        initUI(bean);
        hasRecord = true;
    }

    private void initUI(ClockinBean bean) {
        firstClockin.setText(bean.getOnDutyTime());
        lastClockin.setText(bean.getOffDutyTime());
    }
}
