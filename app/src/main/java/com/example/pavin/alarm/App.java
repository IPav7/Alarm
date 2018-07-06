package com.example.pavin.alarm;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pavin.alarm.db.AlarmDatabase;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.view.AlarmClockActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class App extends Application {

    private static App INSTANCE;
    private static final String DATABASE_NAME = "database";
    private AlarmDatabase alarmDatabase;
    public static final String KEY_ALARM = "key_alarm", KEY_BUNDLE = "key_bundle";

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        alarmDatabase = Room.databaseBuilder(INSTANCE.getApplicationContext(), AlarmDatabase.class, DATABASE_NAME)
                .build();
    }

    public static void setAlarm(Alarm alarm){
        Intent intent = new Intent(INSTANCE.getApplicationContext(), AlarmClockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ALARM, alarm);
        intent.putExtra(KEY_BUNDLE, bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(INSTANCE.getApplicationContext(), alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) INSTANCE.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null) {
            if(alarm.isEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.getNextTrigger(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getNextTrigger(), pendingIntent);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                Log.e("SETTING UP ALARM №" + alarm.getId() + " AT: ", sdf.format(new Date(alarm.getNextTrigger())));
            }
            else alarmManager.cancel(pendingIntent);
        }
    }

    public static App getInstance(){
        return INSTANCE;
    }

    public AlarmDatabase getAlarmDatabase() {
        return alarmDatabase;
    }

}
