package com.example.pavin.alarm.view;

import android.content.ContentResolver;
import android.view.View;

public interface AlarmView {

    void showSoundDialog();

    void setSoundName(String sound);

    void setTimeToPicker(int hours, int mins);

    ContentResolver getContentResolver();

    void finishActivity();

    void attachPresenter();

    void changeDayImage(View view, boolean enabledInDay);

    void setDaysImages(boolean[] days);
}
