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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.presenter.AlarmPresenter;

public class AlarmActivity extends AppCompatActivity implements DialogSound.OnSoundChooseListener, AlarmView {

    private static final String TAG_SOUND = "TAG_SOUND";
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
        tvSoundName = findViewById(R.id.tvSoundName);
        picker = findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                alarmPresenter.changeTime(i, i1);
            }
        });
        attachPresenter();
    }

    public void onDayClick(View view){
        alarmPresenter.onDayClick(view);
    }

    @Override
    public void setDaysImages(boolean[] days) {
        changeDayImage(findViewById(R.id.imgMon), days[Alarm.MONDAY]);
        changeDayImage(findViewById(R.id.imgTue), days[Alarm.TUESDAY]);
        changeDayImage(findViewById(R.id.imgWed), days[Alarm.WEDNESDAY]);
        changeDayImage(findViewById(R.id.imgThu), days[Alarm.THURSDAY]);
        changeDayImage(findViewById(R.id.imgFri), days[Alarm.FRIDAY]);
        changeDayImage(findViewById(R.id.imgSat), days[Alarm.SATURDAY]);
        changeDayImage(findViewById(R.id.imgSun), days[Alarm.SUNDAY]);
    }

    @Override
    public void changeDayImage(View view, boolean enabledInDay) {
        if (enabledInDay)
            ((ImageView)view).setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
        else ((ImageView)view).setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
    }


    @Override
    public void setTimeToPicker(int hours, int mins) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.setHour(hours);
            picker.setMinute(mins);
        }
        else{
            picker.setCurrentHour(hours);
            picker.setCurrentMinute(mins);
        }
    }

    @Override
    public void attachPresenter() {
        alarmPresenter = (AlarmPresenter)getLastCustomNonConfigurationInstance();
        if(alarmPresenter == null){
            if(getIntent().getExtras() == null) {
                alarmPresenter = new AlarmPresenter();
            }
            else{
                alarmPresenter = new AlarmPresenter((Alarm)getIntent().getExtras().getSerializable("ALARM"));
            }
        }
        alarmPresenter.bindView(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return alarmPresenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmPresenter.viewIsReady();
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
        alarmPresenter.submitChanges();
        return true;
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
