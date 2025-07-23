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

    // No-arg constructor for Room
    public DayEntry() {
    }

    // Full constructor for test and manual use
    public DayEntry(String date, int sleepHours, int sleepMinutes, int waterIntake, String mood, String journal) {
        this.date = date;
        this.sleepHours = sleepHours;
        this.sleepMinutes = sleepMinutes;
        this.waterOunces = waterIntake;
        this.mood = mood;
        this.journal = journal;
    }

    public int getSleepHours() {
        return sleepHours;
    }

    public String getMood() {
        return mood;
    }
}
