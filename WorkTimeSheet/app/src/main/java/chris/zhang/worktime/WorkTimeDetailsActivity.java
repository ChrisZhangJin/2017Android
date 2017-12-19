package chris.zhang.worktime;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chris.zhang.mywidgets.CalendarDay;
import chris.zhang.worktime.db.ClockinBean;

public class WorkTimeDetailsActivity extends AppCompatActivity {

    @BindView(R.id.duty_on_time_hh)
    EditText dutyOnTimeHH;
    @BindView(R.id.duty_on_time_mm)
    EditText dutyOnTimeMM;
    @BindView(R.id.duty_on_time_ss)
    EditText dutyOnTimeSS;

    @BindView(R.id.duty_off_time_hh)
    EditText dutyOffTimeHH;
    @BindView(R.id.duty_off_time_mm)
    EditText dutyOffTimeMM;
    @BindView(R.id.duty_off_time_ss)
    EditText dutyOffTimeSS;


    @BindView(R.id.details_edit)
    Button edit;

    @BindView(R.id.details_save)
    Button save;

    private ClockinBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_time_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        CalendarDay cday = intent.getParcelableExtra(IntentConstants.EXTRA_CLOCKIN_DATE);
        if (cday == null) {
            return;
        }

        Cursor cursor = null;
        bean = null;
        try {
            cursor = getContentResolver().query(Uri.parse("content://chris.zhang.worktimeprovider/clockin/" + cday.toString()), null, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                bean = new ClockinBean(Integer.parseInt(cday.toString()));
            } else {
                bean = new ClockinBean(cursor);
                setTimeInUi(bean);
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

    private void setTimeInUi(ClockinBean bean) {
        final String[] array1 = bean.getOnDutyTimeStr().split(":");
        dutyOnTimeHH.setText(array1[0]);
        dutyOnTimeMM.setText(array1[1]);
        dutyOnTimeSS.setText(array1[2]);
        final String[] array2 = bean.getOffDutyTimeStr().split(":");
        dutyOffTimeHH.setText(array2[0]);
        dutyOffTimeMM.setText(array2[1]);
        dutyOffTimeSS.setText(array2[2]);
    }

    private void fillFromUi(ClockinBean bean) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(bean.getOnDutyTime()*1000);

        c.set(Calendar.HOUR, Integer.parseInt(dutyOnTimeHH.getText().toString()));
        c.set(Calendar.MINUTE, Integer.parseInt(dutyOnTimeMM.getText().toString()));
        c.set(Calendar.SECOND, Integer.parseInt(dutyOnTimeSS.getText().toString()));
        bean.setOnDutyTime(c.getTime());

        c.setTimeInMillis(bean.getOffDutyTime()*1000);
        c.set(Calendar.HOUR, Integer.parseInt(dutyOffTimeHH.getText().toString()));
        c.set(Calendar.MINUTE, Integer.parseInt(dutyOffTimeMM.getText().toString()));
        c.set(Calendar.SECOND, Integer.parseInt(dutyOffTimeSS.getText().toString()));
        bean.setOffDutyTime(c.getTime());

    }

    @OnClick(R.id.details_edit)
    void onEdit() {
        dutyOnTimeHH.setEnabled(true);
        dutyOnTimeMM.setEnabled(true);
        dutyOnTimeSS.setEnabled(true);
        dutyOffTimeHH.setEnabled(true);
        dutyOffTimeMM.setEnabled(true);
        dutyOffTimeSS.setEnabled(true);
        edit.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.details_save)
    void onSave() {
        dutyOnTimeHH.setEnabled(false);
        dutyOnTimeMM.setEnabled(false);
        dutyOnTimeSS.setEnabled(false);
        dutyOffTimeHH.setEnabled(false);
        dutyOffTimeMM.setEnabled(false);
        dutyOffTimeSS.setEnabled(false);
        save.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);

        fillFromUi(bean);
        getContentResolver().update(Uri.parse("content://chris.zhang.worktimeprovider/clockin/" + bean.getDateId()), bean.toContentValues(), null, null);
        finish();
    }
}
