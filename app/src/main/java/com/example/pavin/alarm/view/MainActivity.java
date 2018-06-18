package com.example.pavin.alarm.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.presenter.MainPresenter;
import com.example.pavin.alarm.recycler.AlarmRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements FloatingActionButton.OnClickListener, MainView {

    RecyclerView rvAlarms;
    FloatingActionButton fabAdd;
    MainPresenter presenter;
    AlarmRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvAlarms = findViewById(R.id.rvAlarms);
        rvAlarms.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvAlarms.setLayoutManager(llm);
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);
        presenter = new MainPresenter();
        presenter.bindView(this);
        adapter = new AlarmRecyclerAdapter(presenter);
        rvAlarms.setAdapter(adapter);
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
}
