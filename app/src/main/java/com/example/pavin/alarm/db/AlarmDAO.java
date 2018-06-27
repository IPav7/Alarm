package com.example.pavin.alarm.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pavin.alarm.model.Alarm;

import java.util.List;

@Dao
public interface AlarmDAO {

    @Query("SELECT * FROM Alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM Alarm WHERE enabled = 1")
    List<Alarm> getEnabled();

    @Query("SELECT id from Alarm ORDER BY id DESC LIMIT 1")
    int getMaxID();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Alarm alarm);

    @Update
    void update(Alarm alarm);

    @Delete
    void delete(Alarm alarm);

    @Query("DELETE FROM Alarm")
    void deleteAll();


}
