package com.example.pavin.alarm.view;

import android.content.ContentResolver;

public interface AlarmView {

    void showSoundDialog();

    void setSoundName(String sound);

    void setTimeToPicker(int hours, int mins);

    ContentResolver getContentResolver();

    void finishActivity();

    void attachPresenter();

}
