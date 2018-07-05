package com.example.pavin.alarm.presenter;

import com.example.pavin.alarm.db.AlarmDAO;
import com.example.pavin.alarm.App;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.view.BaseView;

public class BasePresenter<V extends BaseView> {

    private V view;
    private AlarmDAO alarmDAO;

    BasePresenter() {
        alarmDAO = App.getInstance().getAlarmDatabase().alarmDAO();
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

    protected AlarmDAO getAlarmDAO() {
        return alarmDAO;
    }

}
