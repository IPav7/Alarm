package com.example.pavin.alarm.view;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
    private Handler handlerPlayer;
    private Handler handlerTTS;
    private boolean TTSinit = false;
    private AudioManager audioManager;
    private int origVolume;
    private int maxVolume;
    private Handler handlerSpeaking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        origVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        wakeUpPhone();
        Bundle bundle = getIntent().getBundleExtra(App.KEY_BUNDLE);
        alarm = (Alarm) bundle.getSerializable(App.KEY_ALARM);
        initializeMediaPlayer();
        mediaPlayer.start();
        handlerPlayer = new Handler();
        handlerPlayer.postDelayed(stopPlayer, 20000);
        if (alarm.isTtsEnabled()) {
            initializeTTS();
        }
    }

    private void sayPhrase() {
        if (textToSpeech != null) {
            String phrase = alarm.getPhrase();
            if (phrase.length() == 0)
                phrase = alarm.getHours() + " hours " + alarm.getMins() + " minutes";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(phrase, TextToSpeech.QUEUE_ADD, null, ENG_TAG);
            } else {
                textToSpeech.speak(phrase, TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

    private void initializeTTS() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(Locale.ENGLISH);
                TTSinit = true;
            }
        }, googleTtsPackage);
    }

    Runnable stopPlayer = new Runnable() {
        @Override
        public void run() {
            stopPlayer();
        }
    };

    private void stopPlayer() {
        handlerPlayer.removeCallbacks(stopPlayer);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (alarm.isTtsEnabled()) {
            handlerTTS = new Handler();
            handlerTTS.postDelayed(checkInit, 1000);
        }
        if (!alarm.isOneTime()) {
            App.setAlarm(alarm);
        }
        if (alarm.isTtsEnabled()) {
            handlerSpeaking = new Handler();
            handlerSpeaking.postDelayed(isSpeaking, 1000);
        } else finishAffinity();
    }

    Runnable isSpeaking = new Runnable() {
        @Override
        public void run() {
            if (textToSpeech != null && !textToSpeech.isSpeaking())
                finishAffinity();
            else handlerSpeaking.postDelayed(isSpeaking, 1000);
        }
    };

    Runnable checkInit = new Runnable() {
        @Override
        public void run() {
            if (TTSinit) {
                sayPhrase();
                handlerTTS.removeCallbacks(checkInit);
            } else {
                handlerTTS.postDelayed(checkInit, 1000);
            }
        }
    };

    private void wakeUpPhone() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    private void initializeMediaPlayer() {
        if (alarm.getSound().getName().equals("Standard")) {
            mediaPlayer = MediaPlayer.create(this, R.raw.zunea_zunea);
            setStreamType();
        } else {
            mediaPlayer = new MediaPlayer();
            try {
                setStreamType();
                mediaPlayer.setDataSource(this, Uri.parse(alarm.getSound().getPath()));
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = MediaPlayer.create(this, R.raw.zunea_zunea);
            }
        }
        mediaPlayer.setLooping(true);
    }

    private void setStreamType() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVolume * alarm.getVolume()), 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build();
            mediaPlayer.setAudioAttributes(attributes);
        } else mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void onClick(View view) {
        stopPlayer();
    }

    @Override
    protected void onDestroy() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, origVolume, 0);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

}
