package com.example.pavin.alarm.view;

import android.content.ContentResolver;
import android.view.View;

public interface AlarmView extends BaseView{

    void showSoundDialog();

    void setSoundName(String sound);

    void setTimeToPicker(int hours, int mins);

    ContentResolver getContentResolver();

    void finishActivity();

    void changeDayImage(View view, boolean enabledInDay);

    void setDaysImages(boolean[] days);
}
