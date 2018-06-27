package com.example.pavin.alarm.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;

public class Converters {

    @TypeConverter
    public static String boolToString(boolean[] bools){
        return Arrays.toString(bools).replace("[", "").replace("]", "");
    }

    @TypeConverter
    public static boolean[] stringToBool(String string){
        String[] parts = string.split(", ");
        boolean[] bools = new boolean[parts.length];
        for (int i = 0; i < parts.length; i++) {
            bools[i] = Boolean.parseBoolean(parts[i]);
        }
        return bools;
    }

}
