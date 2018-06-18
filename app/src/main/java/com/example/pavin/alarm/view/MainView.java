package com.example.pavin.alarm.view;

import com.example.pavin.alarm.model.Alarm;

public interface MainView {

    void startAddActivity();

    void showAlarms();

    void attachPresenter();

    void startEditActivity(Alarm alarm);

}
