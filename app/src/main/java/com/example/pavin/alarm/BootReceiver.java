package com.example.pavin.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pavin.alarm.db.AlarmDAO;
import com.example.pavin.alarm.model.Alarm;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final AlarmDAO alarmDAO = App.getInstance().getAlarmDatabase().alarmDAO();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Alarm> alarms = alarmDAO.getAll();
                if(alarms != null) {
                    for (Alarm alarm : alarms) {
                        App.setAlarm(alarm);
                    }
                }
            }
        }).start();
    }
}
