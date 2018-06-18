package com.example.pavin.alarm.view;

import android.content.ContentResolver;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.presenter.AlarmPresenter;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements DialogSound.OnSoundChooseListener, AlarmView {

    private static final String TAG_SOUND = "TAG_SOUND";
    private static final String VAL_HOURS = "VAL_HOURS";
    private static final String VAL_MINS = "VAL_MINS";
    private static final String VAL_SOUND = "VAL_SOUND";
    private AlarmPresenter alarmPresenter;
    private TextView tvSoundName;
    private TimePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        RelativeLayout rlSound = findViewById(R.id.rlSound);
        rlSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmPresenter.onClickChooseSound();
            }
        });
        attachPresenter();
        tvSoundName = findViewById(R.id.tvSoundName);
        picker = findViewById(R.id.timePicker);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(VAL_SOUND, tvSoundName.getText().toString());
        int hours, mins;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hours = picker.getHour();
            mins = picker.getMinute();
        }
        else {
            hours = picker.getCurrentHour();
            mins = picker.getCurrentMinute();
        }
        outState.putInt(VAL_HOURS, hours);
        outState.putInt(VAL_MINS, mins);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvSoundName.setText(savedInstanceState.getString(VAL_SOUND));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.setHour(savedInstanceState.getInt(VAL_HOURS));
            picker.setMinute(savedInstanceState.getInt(VAL_MINS));
        }
        else{
            picker.setCurrentHour(savedInstanceState.getInt(VAL_HOURS));
            picker.setCurrentMinute(savedInstanceState.getInt(VAL_MINS));
        }
    }

    @Override
    public void attachPresenter() {
        alarmPresenter = (AlarmPresenter)getLastCustomNonConfigurationInstance();
        if(alarmPresenter == null){
            alarmPresenter = new AlarmPresenter();
        }
        alarmPresenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        alarmPresenter.unbindView();
        super.onDestroy();
    }

    @Override
    public void onSoundSelected(int position) {
        alarmPresenter.onSoundSelected(position);
    }

    @Override
    public void setSoundName(String sound) {
        tvSoundName.setText(sound);
    }

    @Override
    public void showSoundDialog() {
        DialogFragment dialogSound = new DialogSound();
        dialogSound.show(getSupportFragmentManager(), TAG_SOUND);
    }

    @Override
    public String[] getAdapterData() {
        return alarmPresenter.getAdapterData();
    }

    @Override
    public ContentResolver getContentResolver() {
        return super.getContentResolver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
            calendar.set(Calendar.MINUTE, picker.getMinute());
        }
        else {
            calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
            calendar.set(Calendar.MINUTE, picker.getCurrentMinute());
        }
        alarmPresenter.addClicked(tvSoundName.getText().toString(), calendar.getTimeInMillis());
        return true;
    }

    @Override
    public void finishActivity() {
        finish();
    }
}