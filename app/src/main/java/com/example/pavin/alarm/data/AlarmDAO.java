package com.example.pavin.alarm.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pavin.alarm.model.Alarm;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AlarmDAO {

    @Query("SELECT * FROM Alarm")
    List<Alarm> getAll();

    @Insert
    void insert(Alarm alarm);

    @Update
    void update(Alarm alarm);

    @Delete
    void delete(Alarm alarm);

    @Query("DELETE FROM Alarm")
    void deleteAll();


}
