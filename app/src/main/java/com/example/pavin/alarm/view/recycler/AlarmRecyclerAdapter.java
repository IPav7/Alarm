package com.example.pavin.alarm.view.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.model.Sound;
import com.example.pavin.alarm.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.AlarmViewHolder> {

    private List<Alarm> alarms;
    private OnAlarmItemClickListener onAlarmItemClickListener;

    public AlarmRecyclerAdapter(List<Alarm> alarms, MainPresenter mainPresenter) {
        this.alarms = alarms;
        if(mainPresenter != null){
            onAlarmItemClickListener = mainPresenter;
        }
        else throw new ClassCastException("MainPresenter should implement OnSoundChooseListener");
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AlarmViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_alarm_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder alarmViewHolder, int i) {
        alarmViewHolder.getSwEnabled().setChecked(alarms.get(i).isEnabled());
        alarmViewHolder.getTvSound().setText(alarms.get(i).getSound().getName());
        String time = alarms.get(i).getHours() + ":" + alarms.get(i).getMins();
        alarmViewHolder.getTvTime().setText(time);
    }

    @Override
    public int getItemCount() {
        if(alarms != null)
        return alarms.size();
        else return 0;
    }

    public interface OnAlarmItemClickListener{
        void onAlarmItemClick(Alarm alarm);
        void stateChanged(Alarm alarm);
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvTime, tvSound;
        private final Switch swEnabled;

        AlarmViewHolder(View view){
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAlarmItemClickListener.onAlarmItemClick(alarms.get(getLayoutPosition()));
                }
            });
            tvTime = view.findViewById(R.id.tvTime);
            tvSound = view.findViewById(R.id.tvSound);
            swEnabled = view.findViewById(R.id.swEnabled);
            swEnabled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAlarmItemClickListener.stateChanged(alarms.get(getLayoutPosition()));
                }
            });
        }

        public Switch getSwEnabled() {
            return swEnabled;
        }

        public TextView getTvTime() {
            return tvTime;
        }

        public TextView getTvSound() {
            return tvSound;
        }
    }
}
