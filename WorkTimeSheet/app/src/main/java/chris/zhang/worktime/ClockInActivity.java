package chris.zhang.worktime;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chris.zhang.mywidgets.CalendarDay;
import chris.zhang.worktime.db.ClockinBean;

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

        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    // TODO: to change implementation to asynchronization
    @OnClick(R.id.clockin)
    void doClockin() {
        ContentResolver cr = getContentResolver();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        final String curTimeStr = sdf.format(new Date());
        if (hasRecord) {
            bean.setOffDutyTime(new Date());
            cr.update(Uri.parse("content://chris.zhang.worktimeprovider/clockin/" + bean.getDateId()), bean.toContentValues(), null, null);
        } else {
            bean.setOnDutyTime(new Date());
            cr.insert(Uri.parse("content://chris.zhang.worktimeprovider/clockin"), bean.toContentValues());
        }

        setClockinTimeInUI(bean);
        hasRecord = true;
    }

    private void setClockinTimeInUI(ClockinBean bean) {
        firstClockin.setText(bean.getOnDutyTimeStr());
        lastClockin.setText(bean.getOffDutyTimeStr());
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            handleDefaultIntent();
            return;
        }

        if (intent.getAction().equals(IntentConstants.ACTION_CLOCKIN)) {
            CalendarDay cDay = intent.getParcelableExtra(IntentConstants.EXTRA_CLOCKIN_DATE);
            initUI(cDay.toString());
        } else {
            handleDefaultIntent();
        }
    }

    private void handleDefaultIntent() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String curDateStr = sdf.format(new Date());

        initUI(curDateStr);
    }

    private void initUI(String curDateStr) {
        curDate.setText(curDateStr);

        ContentResolver cr = getContentResolver();
        Cursor cursor = null;
        try {
            cursor = cr.query(Uri.parse("content://chris.zhang.worktimeprovider/clockin/" + curDateStr), null, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                hasRecord = false;
                bean = new ClockinBean(Integer.parseInt(curDateStr));
            } else {
                hasRecord = true;
                bean = new ClockinBean(cursor);
                setClockinTimeInUI(bean);
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
}
