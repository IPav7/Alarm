package com.example.pavin.alarm.data;

import android.app.Application;
import android.arch.persistence.room.Room;

public class AppDatabase extends Application {

    private static AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "database";
    private AlarmDatabase alarmDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        alarmDatabase = Room.databaseBuilder(getApplicationContext(), AlarmDatabase.class, DATABASE_NAME)
                .build();
        INSTANCE = this;
    }

    public static AppDatabase getInstance(){
        return INSTANCE;
    }

    public AlarmDatabase getAlarmDatabase() {
        return alarmDatabase;
    }

}
