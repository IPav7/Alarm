package com.example.pavin.alarm.view;

import com.example.pavin.alarm.model.Alarm;

public interface MainView extends BaseView{

    void startAddActivity();

    void showAlarms();

    void startEditActivity(Alarm alarm);

}
