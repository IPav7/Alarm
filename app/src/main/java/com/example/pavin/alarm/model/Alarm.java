package com.example.pavin.alarm.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;

@Entity
public class Alarm implements Serializable {

    public static int MONDAY = 0,
                      TUESDAY = 1,
                      WEDNESDAY = 2,
                      THURSDAY = 3,
                      FRIDAY = 4,
                      SATURDAY = 5,
                      SUNDAY = 6;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String sound;
    private boolean enabled;
    private int hours, mins;
    private boolean[] days;

    public Alarm(){
        sound = "Standard";
        enabled = true;
        hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        mins = Calendar.getInstance().get(Calendar.MINUTE);
        days = new boolean[7];
    }

    public Alarm(String sound, boolean enabled, int hours, int mins, boolean[] days) {
        this.sound = sound;
        this.enabled = enabled;
        this.hours = hours;
        this.mins = mins;
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }

    public boolean isEnabledInDay(int day){
        return days[day];
    }

    public void changeDayState(int day){
        days[day] = !days[day];
    }

    public long getNextTrigger(){
        return System.currentTimeMillis();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

}
