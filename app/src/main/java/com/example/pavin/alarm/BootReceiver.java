package com.example.pavin.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.pavin.alarm.data.AlarmDAO;
import com.example.pavin.alarm.data.App;
import com.example.pavin.alarm.model.Alarm;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmDAO alarmDAO = App.getInstance().getAlarmDatabase().alarmDAO();
        List<Alarm> alarms = alarmDAO.getAll();
        if(alarms != null) {
            for (Alarm alarm : alarms) {
                App.setAlarm(alarm);
            }
        }
    }
}
