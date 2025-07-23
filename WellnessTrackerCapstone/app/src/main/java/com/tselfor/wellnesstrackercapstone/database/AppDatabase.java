package com.tselfor.wellnesstrackercapstone.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tselfor.wellnesstrackercapstone.dao.DayEntryDao;
import com.tselfor.wellnesstrackercapstone.dao.MealEntryDao;
import com.tselfor.wellnesstrackercapstone.dao.ExerciseEntryDao;
import com.tselfor.wellnesstrackercapstone.data.DayEntry;
import com.tselfor.wellnesstrackercapstone.data.MealEntry;
import com.tselfor.wellnesstrackercapstone.data.ExerciseEntry;

@Database(entities = {DayEntry.class, MealEntry.class, ExerciseEntry.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DayEntryDao dayEntryDao();
    public abstract MealEntryDao mealEntryDao();
    public abstract ExerciseEntryDao exerciseEntryDao();
}
