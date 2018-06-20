package com.example.pavin.alarm.presenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.pavin.alarm.data.AlarmDAO;
import com.example.pavin.alarm.data.AppDatabase;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.view.AlarmClockActivity;

public class BasePresenter<V> {

    private V view;
    private Context context;
    private AlarmDAO alarmDAO;

    BasePresenter() {
        alarmDAO = AppDatabase.getInstance().getAlarmDatabase().alarmDAO();
    }

    public void bindView(V view){
        this.view = view;
        context = (Context)view;
    }

    public void setAlarm(Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmClockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ALARM", alarm);
        intent.putExtra("bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.getNextTrigger(), pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getNextTrigger(), pendingIntent);
            }
        }
        /*Intent intent = new Intent(context, AlarmClockActivity.class);
        intent.putExtra("ALAR", alarm);
        context.startActivity(intent);*/
    }

    public void unbindView(){
        this.view = null;
        context = null;
    }

    V getView() {
        return view;
    }

    boolean isViewReady(){
        return view != null;
    }

    AlarmDAO getAlarmDAO() {
        return alarmDAO;
    }
}
