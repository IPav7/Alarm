package com.example.pavin.alarm.presenter;

import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.data.App;
import com.example.pavin.alarm.model.Alarm;
import com.example.pavin.alarm.view.AlarmView;

import java.util.ArrayList;

public class AlarmPresenter extends BasePresenter<AlarmView> {

    private ArrayList<String> soundList;
    private Alarm alarm;

    public AlarmPresenter() {
        alarm = new Alarm();
        new Thread(new Runnable() {
            @Override
            public void run() {
                alarm.setId(App.getInstance().getAlarmDatabase().alarmDAO().getMaxID()+1);
            }
        }).start();
    }

    public AlarmPresenter(Alarm alarm){
        this.alarm = alarm;
    }

    public void viewIsReady(){
        getView().setTimeToPicker(alarm.getHours(), alarm.getMins());
        getView().setDaysImages(alarm.getDays());
        getView().setSoundName(alarm.getSound());
    }

    public void onSoundSelected(int position) {
        alarm.setSound(soundList.get(position));
        getView().setSoundName(alarm.getSound());
    }

    public void onClickChooseSound() {
        getView().showSoundDialog();
    }

    public String[] getAdapterData() {
        if(soundList == null)
            soundList = new ArrayList<>();
        else soundList.clear();
        String[] projection = { MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE};
        String selection = MediaStore.Audio.Media.MIME_TYPE + " = 'audio/mpeg'";
        Cursor audioCursor = getView().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        soundList.add("Standard");
        if(audioCursor != null){
            if(audioCursor.moveToFirst()){
                do{
                    //Uri soundURI = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                      //      audioCursor.getInt(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    int columnIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                    //soundList.add(soundURI + "");
                    soundList.add(audioCursor.getString(columnIndex));
                }while(audioCursor.moveToNext());
            }
            audioCursor.close();
        }
        return soundList.toArray(new String[soundList.size()]);
    }

    public void submitChanges() {
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
        switch (view.getId()){
            case R.id.imgMon:
                alarm.changeDayState(Alarm.MONDAY);
                getView().changeDayImage(view, alarm.isEnabledInDay(Alarm.MONDAY));
                break;
            case R.id.imgTue:
                alarm.changeDayState(Alarm.TUESDAY);
                getView().changeDayImage(view, alarm.isEnabledInDay(Alarm.TUESDAY));
                break;
            case R.id.imgWed:
                alarm.changeDayState(Alarm.WEDNESDAY);
                getView().changeDayImage(view, alarm.isEnabledInDay(Alarm.WEDNESDAY));
                break;
            case R.id.imgThu:
                alarm.changeDayState(Alarm.THURSDAY);
                getView().changeDayImage(view, alarm.isEnabledInDay(Alarm.THURSDAY));
                break;
            case R.id.imgFri:
                alarm.changeDayState(Alarm.FRIDAY);
                getView().changeDayImage(view, alarm.isEnabledInDay(Alarm.FRIDAY));
                break;
            case R.id.imgSat:
                alarm.changeDayState(Alarm.SATURDAY);
                getView().changeDayImage(view, alarm.isEnabledInDay(Alarm.SATURDAY));
                break;
            case R.id.imgSun:
                alarm.changeDayState(Alarm.SUNDAY);
                getView().changeDayImage(view, alarm.isEnabledInDay(Alarm.SUNDAY));
                break;
        }
    }

    public void changeTime(int hours, int mins) {
        alarm.setHours(hours);
        alarm.setMins(mins);
    }
}
