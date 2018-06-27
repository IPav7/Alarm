package com.example.pavin.alarm.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.presenter.MainPresenter;
import com.example.pavin.alarm.view.recycler.AlarmRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener, MainView {

    private MainPresenter presenter;
    private AlarmRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvAlarms = findViewById(R.id.rvAlarms);
        rvAlarms.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvAlarms.setLayoutManager(llm);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);
        attachPresenter();
        adapter = new AlarmRecyclerAdapter(presenter.getAlarms(), presenter);
        rvAlarms.setAdapter(adapter);
    }

    @Override
    public void attachPresenter() {
        presenter = (MainPresenter)getLastCustomNonConfigurationInstance();
        if(presenter == null) {
            presenter = new MainPresenter();
        }
        presenter.bindView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.viewIsReady();
    }

    @Override
    public void onClick(View view) {
        presenter.onClickFAB();
    }

    @Override
    public void showAlarms(){
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        presenter.unbindView();
        super.onDestroy();
    }

    @Override
    public void startAddActivity() {
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    @Override
    public void startEditActivity(Alarm alarm) {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("ALARM", alarm);
        startActivity(intent);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

}
