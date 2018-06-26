package com.example.pavin.alarm.presenter;

import android.os.AsyncTask;

import com.example.pavin.alarm.data.App;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.recycler.AlarmViewHolder;
import com.example.pavin.alarm.view.MainView;

import java.util.List;

public class MainPresenter extends BasePresenter<MainView>{
    
    private List<Alarm> alarms;

    public void onClickFAB(){
        getView().startAddActivity();
    }

    public void viewIsReady(){
        new LoadAlarms().execute();
    }

    public int getItemCount() {
        if(alarms != null)
        return alarms.size();
        else return 0;
    }

    public void onBindViewHolder(AlarmViewHolder alarmViewHolder, int i) {
        Alarm alarm = alarms.get(i);
        alarmViewHolder.showEnabled(alarm.isEnabled());
        alarmViewHolder.showSound(alarm.getSound().getName());
        alarmViewHolder.showTime(alarm.getHours() + ":" + alarm.getMins());
    }

    public void stateChanged(boolean b, final int position) {
        alarms.get(position).setEnabled(b);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAlarmDAO().update(alarms.get(position));
            }
        }).start();
        App.setAlarm(alarms.get(position));
    }

    public void onAlarmItemClick(int layoutPosition) {
        getView().startEditActivity(alarms.get(layoutPosition));
    }

    class LoadAlarms extends AsyncTask<Void, Void, List<Alarm>> {
        @Override
        protected List<Alarm> doInBackground(Void... voids) {
            return getAlarmDAO().getAll();
        }

        @Override
        protected void onPostExecute(List<Alarm> list) {
            if(list != null) {
                alarms = list;
                if (isViewReady())
                    getView().showAlarms();
            }
        }
    }

}
