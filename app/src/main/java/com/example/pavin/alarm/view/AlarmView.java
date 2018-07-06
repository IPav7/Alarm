package com.example.pavin.alarm.view;

import android.content.ContentResolver;
import android.view.View;

import com.example.pavin.alarm.model.Alarm;

public interface AlarmView extends BaseView{

    void showSoundDialog();

    void setSoundName(String sound);

    void setVolume(int volume);

    void setTimeToPicker(int hours, int mins);

    ContentResolver getContentResolver();

    void finishActivity();

    void changeDayImage(int pos, boolean enabledInDay);

    void setDaysImages(boolean[] days);

    void setTTSSwitch(boolean enabled, boolean isTime);

    void setPhraseToET(String phrase);

    void startPreviewActivity(Alarm alarm);

    void setSnoozeTime(int min);
}
