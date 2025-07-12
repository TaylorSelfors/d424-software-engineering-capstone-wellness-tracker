package com.tselfor.wellnesstrackercapstone.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DayEntry {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date; // Format: MM/DD/YYYY

    public int sleepHours;
    public int sleepMinutes;

    public int waterOunces;

    public String mood;     // e.g. "Happy"
    public String journal;  // Optional
}

