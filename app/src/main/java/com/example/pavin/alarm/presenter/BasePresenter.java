package com.example.pavin.alarm.presenter;

import com.example.pavin.alarm.data.AlarmDAO;
import com.example.pavin.alarm.data.AppDatabase;

public class BasePresenter<V> {

    private V view;
    private AlarmDAO alarmDAO;

    BasePresenter() {
        alarmDAO = AppDatabase.getInstance().getAlarmDatabase().alarmDAO();
    }

    public void bindView(V view){
        this.view = view;
    }

    public void unbindView(){
        this.view = null;
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
