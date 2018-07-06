package com.example.pavin.alarm.view.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.model.Sound;
import com.example.pavin.alarm.presenter.MainPresenter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.AlarmViewHolder> {

    private List<Alarm> alarms;
    private OnAlarmItemClickListener onAlarmItemClickListener;

    public AlarmRecyclerAdapter(List<Alarm> alarms, MainPresenter mainPresenter) {
        this.alarms = alarms;
        if (mainPresenter != null) {
            onAlarmItemClickListener = mainPresenter;
        }
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AlarmViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_alarm_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder alarmViewHolder, int i) {
        for (int j = 0; j < alarms.get(i).getDays().length; j++) {
            alarmViewHolder.getImgDays()[j].setVisibility(alarms.get(i).getDays()[j] ? View.VISIBLE : View.GONE);
        }
        alarmViewHolder.getTvOnce().setVisibility(alarms.get(i).isOneTime() ? View.VISIBLE : View.GONE);
        String timeLeft = "Snoozing";
        if(!alarms.get(i).isSnooze()) {
            alarmViewHolder.getSwEnabled().setChecked(alarms.get(i).isEnabled());
            long millsLeft = alarms.get(i).getNextTrigger() - Calendar.getInstance().getTimeInMillis();
            timeLeft = millsLeft / 86400000 + " d " + millsLeft % 86400000 / 3600000 + " h " + millsLeft % 3600000 / 60000 + " m";
        }
        alarmViewHolder.getTvLeft().setText(timeLeft);
        int hours = alarms.get(i).getHours();
        int mins = alarms.get(i).getMins();
        String time = hours + ":";
        if (alarms.get(i).getHours() < 10)
            time = "0" + time;
        if (mins < 10)
            time = time + "0" + mins;
        else time = time + mins;
        alarmViewHolder.getTvTime().setText(time);
    }

    @Override
    public int getItemCount() {
        if (alarms != null)
            return alarms.size();
        else return 0;
    }

    public interface OnAlarmItemClickListener {
        void onAlarmItemClick(Alarm alarm);

        void stateChanged(Alarm alarm);

        void deleteAlarm(Alarm alarm);
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTime, tvLeft, tvOnce;
        private final Switch swEnabled;
        private final ImageView imgRemove;
        private final ImageView[] imgDays;

        AlarmViewHolder(final View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAlarmItemClickListener.onAlarmItemClick(alarms.get(getLayoutPosition()));
                }
            });
            tvOnce = view.findViewById(R.id.tvOnce);
            imgDays = new ImageView[]{
                    view.findViewById(R.id.imgDay1),
                    view.findViewById(R.id.imgDay2),
                    view.findViewById(R.id.imgDay3),
                    view.findViewById(R.id.imgDay4),
                    view.findViewById(R.id.imgDay5),
                    view.findViewById(R.id.imgDay6),
                    view.findViewById(R.id.imgDay7)
            };
            tvLeft = view.findViewById(R.id.tvLeft);
            imgRemove = view.findViewById(R.id.imgRemove);
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onAlarmItemClickListener.deleteAlarm(alarms.get(getLayoutPosition()));
                }
            });
            tvTime = view.findViewById(R.id.tvTime);
            swEnabled = view.findViewById(R.id.swEnabled);
            swEnabled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAlarmItemClickListener.stateChanged(alarms.get(getLayoutPosition()));
                }
            });
        }

        public TextView getTvOnce() {
            return tvOnce;
        }

        ImageView[] getImgDays() {
            return imgDays;
        }

        public TextView getTvLeft() {
            return tvLeft;
        }

        public Switch getSwEnabled() {
            return swEnabled;
        }

        public TextView getTvTime() {
            return tvTime;
        }

    }
}
