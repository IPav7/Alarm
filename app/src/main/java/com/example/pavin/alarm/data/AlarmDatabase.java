package com.example.pavin.alarm.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.pavin.alarm.model.Alarm;

@Database(entities = Alarm.class, version = 1)
public abstract class AlarmDatabase extends RoomDatabase {
    public abstract AlarmDAO alarmDAO();
}
