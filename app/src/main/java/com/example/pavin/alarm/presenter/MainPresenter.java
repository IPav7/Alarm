package com.example.pavin.alarm.presenter;

import android.os.AsyncTask;

import com.example.pavin.alarm.App;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.view.recycler.AlarmRecyclerAdapter;
import com.example.pavin.alarm.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter extends BasePresenter<MainView> implements AlarmRecyclerAdapter.OnAlarmItemClickListener {

    private List<Alarm> alarms;

    public MainPresenter() {
        alarms = new ArrayList<>();
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void onClickFAB(){
        getView().startAddActivity();
    }

    public void viewIsReady(){
        new LoadAlarms().execute();
    }

    public void stateChanged(final Alarm alarm) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAlarmDAO().update(alarm);
            }
        }).start();
        App.setAlarm(alarm);
    }

    @Override
    public void onAlarmItemClick(Alarm alarm) {
        getView().startEditActivity(alarm);
    }

    class LoadAlarms extends AsyncTask<Void, Void, List<Alarm>> {
        @Override
        protected List<Alarm> doInBackground(Void... voids) {
            return getAlarmDAO().getAll();
        }

        @Override
        protected void onPostExecute(List<Alarm> list) {
            if(list != null) {
                alarms.clear();
                alarms.addAll(list);
                if (isViewReady())
                    getView().showAlarms();
            }
        }
    }

}
