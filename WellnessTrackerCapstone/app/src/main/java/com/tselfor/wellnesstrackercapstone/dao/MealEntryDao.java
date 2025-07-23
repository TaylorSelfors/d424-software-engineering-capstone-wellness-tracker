package com.tselfor.wellnesstrackercapstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.tselfor.wellnesstrackercapstone.data.MealEntry;

import java.util.List;

@Dao
public interface MealEntryDao {

    @Insert
    void insert(MealEntry entry);

    @Delete
    void delete(MealEntry meal);

    @Query("SELECT * FROM meal_entry WHERE date = :selectedDate")
    List<MealEntry> getMealsForDate(String selectedDate);

    @Query("DELETE FROM meal_entry WHERE date = :selectedDate")
    void deleteMealsForDate(String selectedDate);
}
