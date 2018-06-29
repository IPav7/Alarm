package com.example.pavin.alarm.view;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.pavin.alarm.App;
import com.example.pavin.alarm.R;
import com.example.pavin.alarm.model.Alarm;

import java.io.IOException;
import java.util.Locale;

public class AlarmClockActivity extends AppCompatActivity {

    private Alarm alarm;
    private MediaPlayer mediaPlayer;
    private TextToSpeech textToSpeech;
    private String googleTtsPackage = "com.google.android.tts";
    private final String ENG_TAG = "TTS ENGINE";
    private final int CHECK_TTS = 1;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        wakeUpPhone();
        Bundle bundle = getIntent().getBundleExtra(App.KEY_BUNDLE);
        alarm = (Alarm)bundle.getSerializable(App.KEY_ALARM);
        initializeMediaPlayer();
        mediaPlayer.start();
        handler = new Handler();
        handler.postDelayed(stopPlayer, 20000);
        if(alarm.isTtsEnabled()){
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, CHECK_TTS);
        }
    }

    private void sayPhrase(){
        if(textToSpeech != null) {
            String phrase = alarm.getPhrase();
            if(phrase.length() == 0)
                phrase = alarm.getHours() + " hours " + alarm.getMins() + " minutes";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(phrase, TextToSpeech.QUEUE_ADD, null, ENG_TAG);
            } else {
                textToSpeech.speak(phrase, TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CHECK_TTS){
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                         textToSpeech.setLanguage(Locale.UK);
                    }
                }, googleTtsPackage);
            }
            else Toast.makeText(this, "NO", Toast.LENGTH_SHORT).show();
        }
    }

    Runnable stopPlayer = new Runnable() {
        @Override
        public void run() {
            stopPlayer();
        }
    };

    private void stopPlayer() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        sayPhrase();
        if(!alarm.isOneTime()){
            App.setAlarm(alarm);
        }
    }

    private void wakeUpPhone() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    private void initializeMediaPlayer() {
        if(alarm.getSound().getName().equals("Standard")){
            mediaPlayer = MediaPlayer.create(this, R.raw.zunea_zunea);
            setStreamType();
        }
        else {
            mediaPlayer = new MediaPlayer();
            try {
                setStreamType();
                mediaPlayer.setDataSource(this, Uri.parse(alarm.getSound().getPath()));
                mediaPlayer.prepare();
            }
            catch (IOException e){
                mediaPlayer = MediaPlayer.create(this, R.raw.zunea_zunea);
            }
        }
    }

    private void setStreamType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_ALARM).build());
        }
        else mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
    }

    public void onClick(View view) {
        stopPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(stopPlayer);
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
        if(textToSpeech != null){
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

}
