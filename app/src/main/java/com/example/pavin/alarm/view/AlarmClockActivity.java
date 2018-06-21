package com.example.pavin.alarm.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.data.App;
import com.example.pavin.alarm.model.Alarm;

public class AlarmClockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        Alarm alarm = (Alarm) bundle.getSerializable("ALARM");
        if(alarm != null) {
            TextView textView = findViewById(R.id.tvAboutAlarm);
            textView.setText(alarm.toString());
            App.setAlarm(alarm);
        }
    }
}
