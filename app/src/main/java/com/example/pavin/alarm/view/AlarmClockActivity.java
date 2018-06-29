package com.example.pavin.alarm.view;

import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.App;
import com.example.pavin.alarm.model.Alarm;

import java.util.Locale;

public class AlarmClockActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Button btnSnooze;
    Alarm alarm;
    TextToSpeech textToSpeech;
    String googleTTSPackage = "com.google.android.tts";
    private final String ENG_TAG = "TTS ENGINE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        alarm = (Alarm) bundle.getSerializable("ALARM");
        try {
            if (alarm.getSound().getName().equals("Standard")) {
                mediaPlayer = MediaPlayer.create(this, R.raw.zunea_zunea);
            } else {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(this, Uri.parse(alarm.getSound().getPath()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_ALARM).build());
                }
                else mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.prepare();
            }
            mediaPlayer.start();
        }catch (Exception e){
            Log.e("EXCEPTION", e.getLocalizedMessage());
        }
        btnSnooze = findViewById(R.id.btnSnooze);
        btnSnooze.setText(String.format("Snooze for %d min", alarm.isSnooze() ? alarm.getMinsForSnooze() : 5));
        Handler handler = new Handler();
        handler.postDelayed(stopPlayer, 20000);
        if(checkGoogleTTS()) {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }, googleTTSPackage);
        }
        else Toast.makeText(this, "VSYO OCHEN' PLOHO", Toast.LENGTH_SHORT).show();
    }

    private void sayPhrase(){
        if(textToSpeech != null) {
            String phrase = alarm.getPhrase();
            if(phrase.isEmpty())
                phrase = alarm.getHours() + " hours " + alarm.getMins() + " minutes";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(phrase, TextToSpeech.QUEUE_ADD, null, ENG_TAG);
            } else {
                textToSpeech.speak(phrase, TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

    private void stopPlayer() {
        if(mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.stop();
        setRepeat();
        if(alarm.isTtsEnabled())
            sayPhrase();
    }

    Runnable stopPlayer = new Runnable() {
        @Override
        public void run() {
            stopPlayer();
        }
    };

    private void setRepeat(){
        if(alarm != null)
            App.setAlarm(alarm);
    }

    private void releaseMP(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        releaseMP();
        if(textToSpeech != null) {
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private boolean checkGoogleTTS() {
        try {
            getPackageManager().getPackageInfo(googleTTSPackage, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCancel:
                alarm.setSnooze(false);
                break;
            case R.id.btnSnooze:
                if(!alarm.isSnooze())
                    alarm.setSnooze(true);
                if(alarm.getMinsForSnooze() == 0)
                    alarm.setMinsForSnooze(5);
                break;
        }
        stopPlayer();
    }
}
