package com.example.pavin.alarm.model;

import java.io.Serializable;

public class Sound implements Serializable {

    private String name;
    private String path;

    public Sound(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return name.equals(((Sound)obj).getName());
    }
}
