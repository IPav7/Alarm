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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pavin.alarm.App;
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
    private Switch swTTS;
    private SeekBar seekBarVolume;
    private ImageView[] imgDays;
    private EditText etPhrase;
    private SeekBar seekBarSnooze;
    private TextView tvSnoozeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RelativeLayout rlSound = findViewById(R.id.rlSound);
        rlSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmPresenter.onClickChooseSound();
            }
        });
        initializeSeekBarSnooze();
        tvSoundName = findViewById(R.id.tvSoundName);
        etPhrase = findViewById(R.id.etPhrase);
        swTTS = findViewById(R.id.swTTS);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                alarmPresenter.setSayTime(i == R.id.rbTime);
            }
        });
        picker = findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                alarmPresenter.changeTime(i, i1);
            }
        });
        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarVolume.setOnSeekBarChangeListener(this);
        imgDays = new ImageView[]{
                findViewById(R.id.imgMon),
                findViewById(R.id.imgTue),
                findViewById(R.id.imgWed),
                findViewById(R.id.imgThu),
                findViewById(R.id.imgFri),
                findViewById(R.id.imgSat),
                findViewById(R.id.imgSun)
        };
        attachPresenter();
    }

    private void initializeSeekBarSnooze() {
        tvSnoozeTime = findViewById(R.id.tvSnoozeTime);
        seekBarSnooze = findViewById(R.id.seekBarSnooze);
        seekBarSnooze.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                alarmPresenter.setSnoozeTime(i + 5);
                String text = "Snooze " + (i + 5) + "m";
                tvSnoozeTime.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void setSnoozeTime(int min) {
        String text = "Snooze " + min + "m";
        tvSnoozeTime.setText(text);
        seekBarSnooze.setProgress(min - 5);
    }

    public void onDayClick(View view) {
        alarmPresenter.onDayClick(view);
    }

    @Override
    public void setDaysImages(boolean[] days) {
        for (int i = 0; i < days.length; i++) {
            changeDayImage(i, days[i]);
        }
    }

    @Override
    public void changeDayImage(int pos, boolean enabledInDay) {
        imgDays[pos].setSelected(enabledInDay);
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
                alarmPresenter.changePhrase(getPhrase());
                alarmPresenter.submitChanges();
                break;
            default:
                finishActivity();
        }
        return true;
    }

    private String getPhrase() {
        return etPhrase.getText().toString();
    }

    @Override
    public Sound getSelectedAudio() {
        return alarmPresenter.getSelectedAudio();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    public void onTTSClick(View view) {
        if (view.getId() == R.id.tvTTS)
            swTTS.setChecked(!swTTS.isChecked());
        boolean checked = swTTS.isChecked();
        alarmPresenter.changeTTS(checked);
        radioGroup.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTTSSwitch(boolean enabled, boolean isTime) {
        radioGroup.check(isTime ? R.id.rbTime : R.id.rbPhrase);
        radioGroup.setVisibility(enabled ? View.VISIBLE : View.GONE);
        swTTS.setChecked(enabled);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        alarmPresenter.setVolume(i);
    }

    @Override
    public void setPhraseToET(String phrase) {
        etPhrase.setText(phrase);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void onPreviewClick(View view) {
        alarmPresenter.changePhrase(etPhrase.getText().toString());
        alarmPresenter.previewAlarm();
    }

    @Override
    public void startPreviewActivity(Alarm alarm) {
        Intent intent = new Intent(this, AlarmClockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(App.KEY_ALARM, alarm);
        bundle.putBoolean(AlarmClockActivity.ALARM_PREVIEW, true);
        intent.putExtra(App.KEY_BUNDLE, bundle);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        alarmPresenter.changePhrase(etPhrase.getText().toString());
    }
}
