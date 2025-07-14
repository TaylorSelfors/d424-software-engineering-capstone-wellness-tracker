package com.tselfor.wellnesstrackercapstone.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_entry")
public class ExerciseEntry {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date; // To associate this exercise with a DayEntry

    public int duration;
    public String intensity; // "Low", "Medium", or "High"
}

