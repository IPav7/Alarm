package com.example.pavin.alarm.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.model.Sound;
import com.example.pavin.alarm.presenter.AlarmPresenter;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity implements DialogSound.OnSoundChooseListener, AlarmView, SeekBar.OnSeekBarChangeListener {

    private static final String TAG_SOUND = "TAG_SOUND";
    private static final int REQUEST_CODE_READ_STORAGE = 100;
    private AlarmPresenter alarmPresenter;
    private TextView tvSoundName;
    private TimePicker picker;
    private RadioGroup radioGroup;
    private CheckedTextView checkedTextView;
    private TextView tvVolume;
    private SeekBar seekBarVolume;
    //private String googleTTSPackage = "com.google.android.tts";

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
        checkedTextView = findViewById(R.id.checkedTV);
        radioGroup = findViewById(R.id.radioGroup);
        picker = findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                alarmPresenter.changeTime(i, i1);
            }
        });
        tvVolume = findViewById(R.id.tvVolume);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarVolume.setOnSeekBarChangeListener(this);
        attachPresenter();
    }

    public void onDayClick(View view) {
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
            ((ImageView) view).setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
        else ((ImageView) view).setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
    }


    @Override
    public void setTimeToPicker(int hours, int mins) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.setHour(hours);
            picker.setMinute(mins);
        } else {
            picker.setCurrentHour(hours);
            picker.setCurrentMinute(mins);
        }
    }

    @Override
    public void attachPresenter() {
        alarmPresenter = (AlarmPresenter) getLastCustomNonConfigurationInstance();
        if (alarmPresenter == null) {
            if (getIntent().getExtras() == null) {
                alarmPresenter = new AlarmPresenter();
            } else {
                alarmPresenter = new AlarmPresenter((Alarm) getIntent().getExtras().getSerializable("ALARM"));
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
    public void setVolume(int volume) {
        seekBarVolume.setProgress(volume);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            showDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDialog();
                } else {
                    showAccessDenied();
                }
                break;
        }
    }

    private void showAccessDenied() {
        Snackbar.make(findViewById(R.id.llAlarm), "Permission to read storage denied", Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + getPackageName()));
                        startActivity(settingsIntent);
                    }
                }).show();
    }

    private void showDialog() {
        DialogFragment dialogSound = new DialogSound();
        dialogSound.show(getSupportFragmentManager(), TAG_SOUND);
    }

    @Override
    public ArrayList<Sound> getAdapterData() {
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
        switch (item.getItemId()) {
            case R.id.addMenu:
                if (checkedTextView.isChecked())
                    alarmPresenter.changePhrase(getPhrase());
                alarmPresenter.submitChanges();
                break;
            case R.id.delAlarm:
                alarmPresenter.deleteAlarm();
                break;
        }
        return true;
    }

    private String getPhrase() {
        String phrase = "";
        if (radioGroup.getCheckedRadioButtonId() == R.id.rbPhrase)
            phrase = "Text to check";
        return phrase;
    }

    @Override
    public Sound getSelectedAudio() {
        return alarmPresenter.getSelectedAudio();
    }

    @Override
    public void finishActivity() {
        finish();
    }

//    private boolean checkGoogleTTS() {
//        try {
//            getPackageManager().getPackageInfo(googleTTSPackage, PackageManager.GET_ACTIVITIES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
//    }

    public void onTTSClick(View view) {
        boolean checked = !((CheckedTextView) view).isChecked();
        ((CheckedTextView) view).setChecked(checked);
        if (checked)
            ((CheckedTextView) view).setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
        else ((CheckedTextView) view).setCheckMarkDrawable(android.R.color.transparent);
        alarmPresenter.changeTTS(checked);
        radioGroup.setEnabled(checked);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        alarmPresenter.setVolume(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
