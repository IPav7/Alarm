package com.example.pavin.alarm.model;

public class Alarm {

    private int id;
    private String sound;
    private long time;
    private boolean enabled;

    public Alarm(String sound, long time, boolean enabled) {
        this.sound = sound;
        this.time = time;
        this.enabled = enabled;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
