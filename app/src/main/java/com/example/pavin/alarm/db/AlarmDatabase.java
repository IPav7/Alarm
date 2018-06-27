package com.example.pavin.alarm.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.pavin.alarm.db.Converters;
import com.example.pavin.alarm.model.Alarm;

@Database(entities = Alarm.class, version = 1)
@TypeConverters({Converters.class})
public abstract class AlarmDatabase extends RoomDatabase {
    public abstract AlarmDAO alarmDAO();
}
