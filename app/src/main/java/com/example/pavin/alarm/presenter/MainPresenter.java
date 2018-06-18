package com.example.pavin.alarm.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.recycler.AlarmViewHolder;
import com.example.pavin.alarm.view.MainView;

import java.util.Calendar;
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

    public void stateChanged(boolean b, final int position) {
        alarms.get(position).setEnabled(b);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAlarmDAO().update(alarms.get(position));
            }
        }).start();
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
                Log.e("MAIN ALARMS", alarms.size() + " size");
                if (isViewReady())
                    getView().showAlarms();
            }
        }
    }

}
