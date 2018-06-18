package com.example.pavin.alarm.presenter;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.view.AlarmActivity;
import com.example.pavin.alarm.view.AlarmView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AlarmPresenter extends BasePresenter<AlarmView> {

    private ArrayList<String> soundList;

    public void onSoundSelected(int position) {
        getView().setSoundName(soundList.get(position));
    }

    public void onClickChooseSound() {
        getView().showSoundDialog();
    }

    public String[] getAdapterData() {
        if(soundList == null)
            soundList = new ArrayList<>();
        else soundList.clear();
        String[] projection = { MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME};
        String selection = MediaStore.Audio.Media.MIME_TYPE + " = 'audio/mpeg'";
        Cursor audioCursor = getView().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        soundList.add("Standard");
        if(audioCursor != null){
            if(audioCursor.moveToFirst()){
                do{
                    Uri soundURI = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            audioCursor.getInt(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    soundList.add(soundURI + "");
                }while(audioCursor.moveToNext());
            }
            audioCursor.close();
        }
        return soundList.toArray(new String[soundList.size()]);
    }

    public void addClicked(final String sound, final long timeInMillis) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAlarmDAO().insert(new Alarm(sound, timeInMillis, true));
            }
        }).start();
        getView().finishActivity();
    }

    public void editActivityStarted(Bundle extras) {
        Alarm alarm = (Alarm)extras.getSerializable("ALARM");
        if(alarm != null) {
            getView().setSoundName(alarm.getSound());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(alarm.getTime());
            getView().setTimeToPicker(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        }
    }

    public void editAlarmClicked(String sound, long timeInMillis, Bundle bundle) {
        final Alarm alarm = (Alarm)bundle.getSerializable("ALARM");
        if(alarm != null) {
            alarm.setSound(sound);
            alarm.setTime(timeInMillis);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getAlarmDAO().update(alarm);
                }
            }).start();
        }
        getView().finishActivity();
    }
}
