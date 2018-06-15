package com.example.pavin.alarm.presenter;

import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.recycler.AlarmViewHolder;
import com.example.pavin.alarm.view.MainView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainPresenter extends BasePresenter<MainView>{
    
    private List<Alarm> alarms;

    public void onClickFAB(){
        getView().startAddActivity();
    }

    public void viewIsReady(){
        alarms = new ArrayList<>();
        alarms.add(new Alarm("Zhu-zhu", System.currentTimeMillis(), true));
        alarms.add(new Alarm("ko-ko-ko", System.currentTimeMillis() + 1000*60*5, false));
    }

    public int getItemCount() {
        return alarms.size();
    }

    public void onBindViewHolder(AlarmViewHolder alarmViewHolder, int i) {
        Alarm alarm = alarms.get(i);
        alarmViewHolder.showEnabled(alarm.isEnabled());
        alarmViewHolder.showSound(alarm.getSound());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarm.getTime());
        alarmViewHolder.showTime(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
    }
}
