package com.example.pavin.alarm.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pavin.alarm.R;

public class AlarmViewHolder extends RecyclerView.ViewHolder implements IViewHolder{

    TextView tvTime, tvSound;
    Switch swEnabled;

    public AlarmViewHolder(View view){
        super(view);
        tvTime = view.findViewById(R.id.tvTime);
        tvSound = view.findViewById(R.id.tvSound);
        swEnabled = view.findViewById(R.id.swEnabled);
    }

    @Override
    public void showTime(String time) {
        tvTime.setText(time);
    }

    @Override
    public void showSound(String sound) {
        tvSound.setText(sound);
    }

    @Override
    public void showEnabled(boolean enable) {
        swEnabled.setChecked(enable);
    }
}
