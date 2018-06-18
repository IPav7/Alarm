package com.example.pavin.alarm.view;

import android.content.ContentResolver;

public interface AlarmView {

    void showSoundDialog();

    void setSoundName(String sound);

    ContentResolver getContentResolver();

    void finishActivity();

    void attachPresenter();

}
