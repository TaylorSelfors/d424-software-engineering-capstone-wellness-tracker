package com.tselfor.wellnesstrackercapstone.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal_entry")
public class MealEntry {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date; // Used to associate this meal with a DayEntry

    public String mealType; // Breakfast, Lunch, Dinner, Snack
    public int calories;
    public String timeEaten; // e.g., "12:00 PM"
}
