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
        tvTime = view.findViewById(R.id.tvTime);
        tvSound = view.findViewById(R.id.tvSound);
        swEnabled = view.findViewById(R.id.swEnabled);
        swEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.stateChanged(b, getLayoutPosition());
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
