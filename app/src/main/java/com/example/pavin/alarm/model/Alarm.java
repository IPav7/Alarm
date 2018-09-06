package com.example.pavin.alarm.model;

import android.app.AlarmManager;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
    @Embedded(prefix = "sound_")
    private Sound sound;
    private boolean enabled;
    private int hours, mins;
    private boolean[] days;
    private boolean ttsEnabled;
    private boolean sayTime;
    private String phrase;
    private float volume;
    private boolean snooze;
    private int minToSnooze;

    public Alarm() {
        sound = new Sound("Standard", null);
        enabled = true;
        hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        mins = Calendar.getInstance().get(Calendar.MINUTE);
        days = new boolean[7];
        ttsEnabled = false;
        volume = 1;
        sayTime = false;
        snooze = false;
        minToSnooze = 5;
    }

    public Alarm(int id, Sound sound, boolean enabled, int hours, int mins, boolean[] days, boolean ttsEnabled, float volume, boolean sayTime, boolean snooze, int minToSnooze) {
        this.id = id;
        this.sound = sound;
        this.enabled = enabled;
        this.hours = hours;
        this.mins = mins;
        this.days = days;
        this.ttsEnabled = ttsEnabled;
        this.volume = volume;
        this.sayTime = sayTime;
        this.snooze = snooze;
        this.minToSnooze = minToSnooze;
    }

    public Alarm(int id, Sound sound, boolean enabled, int hours, int mins, boolean[] days, boolean ttsEnabled, String phrase, float volume, boolean sayTime, boolean snooze, int minToSnooze) {
        this(id, sound, enabled, hours, mins, days, ttsEnabled, volume, sayTime, snooze, minToSnooze);
        this.phrase = phrase;
    }

    public boolean isTtsEnabled() {
        return ttsEnabled;
    }

    public void setTtsEnabled(boolean ttsEnabled) {
        this.ttsEnabled = ttsEnabled;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
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

    public boolean isEnabledInDay(int day) {
        return days[day];
    }

    public void changeDayState(int day) {
        days[day] = !days[day];
    }


    public long getNextTrigger() {
        Calendar calendar = Calendar.getInstance();
        if (snooze) {
            calendar.set(Calendar.SECOND, 0);
            calendar.setTimeInMillis(calendar.getTimeInMillis() + minToSnooze * 60 * 1000);
            return calendar.getTimeInMillis();
        }
        Calendar cal;
        if (isOneTime()) {
            cal = initCalendar(calendar.get(Calendar.DAY_OF_WEEK));
            if (calendar.compareTo(cal) > 0)
                cal.setTimeInMillis(cal.getTimeInMillis() + AlarmManager.INTERVAL_DAY);
            return cal.getTimeInMillis();
        } else {
            List<Calendar> list = new LinkedList<>();
            for (int i = 0; i < days.length; i++) {
                if (days[i]) {
                    cal = initCalendar(i + 2);
                    if (calendar.compareTo(cal) > 0) {
                        cal.setTimeInMillis(cal.getTimeInMillis() + AlarmManager.INTERVAL_DAY * 7);
                    }
                    list.add(cal);
                }
            }
            Collections.sort(list);
            return list.get(0).getTimeInMillis();
        }
    }

    private Calendar initCalendar(int i) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, i);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, mins);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }

    public boolean isOneTime() {
        for (boolean b :
                days) {
            if (b) return false;
        }
        return true;
    }

    public boolean isSayTime() {
        return sayTime;
    }

    public void setSayTime(boolean sayTime) {
        this.sayTime = sayTime;
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

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isSnooze() {
        return snooze;
    }

    public void setSnooze(boolean snooze) {
        this.snooze = snooze;
    }

    public int getMinToSnooze() {
        return minToSnooze;
    }

    public void setMinToSnooze(int minToSnooze) {
        this.minToSnooze = minToSnooze;
    }
}
