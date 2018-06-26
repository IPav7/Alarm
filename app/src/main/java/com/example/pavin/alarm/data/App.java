package com.example.pavin.alarm.data;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.view.AlarmClockActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class App extends Application {

    private static App INSTANCE;
    private static final String DATABASE_NAME = "database";
    private AlarmDatabase alarmDatabase;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        alarmDatabase = Room.databaseBuilder(getApplicationContext(), AlarmDatabase.class, DATABASE_NAME)
                .build();
        INSTANCE = this;
        context = getApplicationContext();
    }

    public static void setAlarm(Alarm alarm){
        Intent intent = new Intent(context, AlarmClockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ALARM", alarm);
        intent.putExtra("bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null) {
            if(alarm.isEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.getNextTrigger(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getNextTrigger(), pendingIntent);
                }
            }
            else alarmManager.cancel(pendingIntent);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        Log.e("SETTING UP ALARM №" + alarm.getId() + " AT: ", sdf.format(new Date(alarm.getNextTrigger())));
    }

    public static App getInstance(){
        return INSTANCE;
    }

    public AlarmDatabase getAlarmDatabase() {
        return alarmDatabase;
    }

}
