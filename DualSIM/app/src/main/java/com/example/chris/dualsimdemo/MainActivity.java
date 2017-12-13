package com.example.chris.dualsimdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private Button checkSimSlots;
    private TextView simSlotsNumber;
    private TextView sim1Imei;
    private TextView sim2Imei;
    private TextView sim1State;
    private TextView sim2State;
    private TextView sim1Number;
    private TextView sim2Number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSimSlots = (Button) findViewById(R.id.check_sim_slots);
        checkSimSlots.setOnClickListener(this);

        simSlotsNumber = (TextView) findViewById(R.id.sim_slots_number);
        sim1Imei = (TextView) findViewById(R.id.sim1_imei);
        sim1State = (TextView) findViewById(R.id.sim1_ready);
        sim2Imei = (TextView) findViewById(R.id.sim2_imei);
        sim2State = (TextView) findViewById(R.id.sim2_ready);
        sim1Number = (TextView) findViewById(R.id.sim1_number);
        sim2Number = (TextView) findViewById(R.id.sim2_number);
    }

    @Override
    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
        } else {
            TelephonyInfoCompat telephonyInfoCompat = TelephonyInfoCompat.getInstace(this);
            initUI(telephonyInfoCompat);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            TelephonyInfoCompat telephonyInfoCompat = TelephonyInfoCompat.getInstace(this);
            initUI(telephonyInfoCompat);
        }
    }

    private void initUI(TelephonyInfoCompat telephonyInfoCompat) {
        simSlotsNumber.setText(String.valueOf(telephonyInfoCompat.getSlotCount()));
        sim1Imei.setText(telephonyInfoCompat.getImei1());
        sim1State.setText(telephonyInfoCompat.getSIM1State());
        sim2Imei.setText(telephonyInfoCompat.getImei2());
        sim2State.setText(telephonyInfoCompat.getSIM2State());
        sim1Number.setText(telephonyInfoCompat.getSim1Number());
        sim2Number.setText(telephonyInfoCompat.getSim2Number());
    }
}
