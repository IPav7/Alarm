package com.example.pavin.alarm.presenter;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.pavin.alarm.view.AlarmView;

import java.util.ArrayList;

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
        String[] proj = { MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME};
        String selection = MediaStore.Audio.Media.MIME_TYPE + " = 'audio/mpeg'";
        Cursor audioCursor = getView().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, selection, null, null);
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
}
