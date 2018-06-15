package com.example.pavin.alarm.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.presenter.MainPresenter;

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private MainPresenter mainPresenter;

    public AlarmRecyclerAdapter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AlarmViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_alarm_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder alarmViewHolder, int i) {
        mainPresenter.onBindViewHolder(alarmViewHolder, i);
    }

    @Override
    public int getItemCount() {
        return mainPresenter.getItemCount();
    }
}
