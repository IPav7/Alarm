package com.example.pavin.alarm.presenter;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.App;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.model.Sound;
import com.example.pavin.alarm.view.AlarmView;

import java.util.ArrayList;

public class AlarmPresenter extends BasePresenter<AlarmView> {

    private ArrayList<Sound> soundList;
    private Alarm alarm;

    public AlarmPresenter() {
        alarm = new Alarm();
        new Thread(new Runnable() {
            @Override
            public void run() {
                alarm.setId(App.getInstance().getAlarmDatabase().alarmDAO().getMaxID() + 1);
            }
        }).start();
    }

    public AlarmPresenter(Alarm alarm) {
        this.alarm = alarm;
    }

    public void viewIsReady() {
        getView().setTimeToPicker(alarm.getHours(), alarm.getMins());
        getView().setDaysImages(alarm.getDays());
        getView().setSoundName(alarm.getSound().getName());
        getView().setVolume((int) (alarm.getVolume() * 10));
        getView().setTTSSwitch(alarm.isTtsEnabled(), alarm.isSayTime());
        getView().setPhraseToET(alarm.getPhrase());
        getView().setSnoozeTime(alarm.getMinToSnooze());
    }

    public void onSoundSelected(int position) {
        if (position != -1) {
            alarm.setSound(soundList.get(position));
            getView().setSoundName(alarm.getSound().getName());
        }
    }

    public void setSnoozeTime(int mins) {
        alarm.setMinToSnooze(mins);
    }

    public void onClickChooseSound() {
        getView().showSoundDialog();
    }

    public ArrayList<Sound> getAdapterData() {
        if (soundList == null)
            soundList = new ArrayList<>();
        else soundList.clear();
        soundList.add(new Sound("Standard", null));
        String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE};
        String selection = MediaStore.Audio.Media.MIME_TYPE + " = 'audio/mpeg'";
        Cursor audioCursor = getView().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, MediaStore.Audio.Media.TITLE);
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    Uri soundURI = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            audioCursor.getInt(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    int columnIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    soundList.add(new Sound(audioCursor.getString(columnIndex), soundURI.toString()));
                } while (audioCursor.moveToNext());
            }
            audioCursor.close();
        }
        return soundList;
    }

    public void submitChanges() {
        alarm.setSnooze(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAlarmDAO().insert(alarm);
            }
        }).start();
        App.setAlarm(alarm);
        getView().finishActivity();
    }

    public void onDayClick(View view) {
        switch (view.getId()) {
            case R.id.imgMon:
                alarm.changeDayState(Alarm.MONDAY);
                getView().changeDayImage(Alarm.MONDAY, alarm.isEnabledInDay(Alarm.MONDAY));
                break;
            case R.id.imgTue:
                alarm.changeDayState(Alarm.TUESDAY);
                getView().changeDayImage(Alarm.TUESDAY, alarm.isEnabledInDay(Alarm.TUESDAY));
                break;
            case R.id.imgWed:
                alarm.changeDayState(Alarm.WEDNESDAY);
                getView().changeDayImage(Alarm.WEDNESDAY, alarm.isEnabledInDay(Alarm.WEDNESDAY));
                break;
            case R.id.imgThu:
                alarm.changeDayState(Alarm.THURSDAY);
                getView().changeDayImage(Alarm.THURSDAY, alarm.isEnabledInDay(Alarm.THURSDAY));
                break;
            case R.id.imgFri:
                alarm.changeDayState(Alarm.FRIDAY);
                getView().changeDayImage(Alarm.FRIDAY, alarm.isEnabledInDay(Alarm.FRIDAY));
                break;
            case R.id.imgSat:
                alarm.changeDayState(Alarm.SATURDAY);
                getView().changeDayImage(Alarm.SATURDAY, alarm.isEnabledInDay(Alarm.SATURDAY));
                break;
            case R.id.imgSun:
                alarm.changeDayState(Alarm.SUNDAY);
                getView().changeDayImage(Alarm.SUNDAY, alarm.isEnabledInDay(Alarm.SUNDAY));
                break;
        }
    }

    public void setSayTime(boolean isSayTime) {
        alarm.setSayTime(isSayTime);
    }

    public void changeTime(int hours, int mins) {
        alarm.setHours(hours);
        alarm.setMins(mins);
    }

    public Sound getSelectedAudio() {
        return alarm.getSound();
    }

    public void changeTTS(boolean checked) {
        alarm.setTtsEnabled(checked);
    }

    public void changePhrase(String phrase) {
        alarm.setPhrase(phrase);
    }

    public void setVolume(int volume) {
        alarm.setVolume(0.1f * volume);
    }

    public void previewAlarm() {
        getView().startPreviewActivity(alarm);
    }
}
