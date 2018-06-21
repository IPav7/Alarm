package com.example.pavin.alarm.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.presenter.MainPresenter;

public class AlarmViewHolder extends RecyclerView.ViewHolder implements IViewHolder{

    private TextView tvTime, tvSound;
    private Switch swEnabled;
    private MainPresenter presenter;

    AlarmViewHolder(View view, MainPresenter mainPresenter){
        super(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAlarmItemClick(getLayoutPosition());
            }
        });
        tvTime = view.findViewById(R.id.tvTime);
        tvSound = view.findViewById(R.id.tvSound);
        swEnabled = view.findViewById(R.id.swEnabled);
        swEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.stateChanged(swEnabled.isChecked(), getLayoutPosition());
            }
        });
        presenter = mainPresenter;
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
