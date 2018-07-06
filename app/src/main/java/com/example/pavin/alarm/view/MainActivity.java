package com.example.pavin.alarm.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
        checkGoogleTTSInstalled();
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

    private void checkGoogleTTSInstalled() {
        PackageManager pm = getPackageManager();
        final String googleTTSPackage = "com.google.android.tts";
        try {
            pm.getPackageInfo(googleTTSPackage, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            Snackbar.make(findViewById(R.id.mainLayout), "For a better experience install Google TTS", Snackbar.LENGTH_LONG)
                    .setAction("INSTALL", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + googleTTSPackage)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + googleTTSPackage)));
                            }
                        }
                    }).show();
        }
    }

    @Override
    public void attachPresenter() {
        presenter = (MainPresenter) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
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
    public void showAlarms() {
        findViewById(R.id.tvNoAlarm).setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.tvToCreate).setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
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
