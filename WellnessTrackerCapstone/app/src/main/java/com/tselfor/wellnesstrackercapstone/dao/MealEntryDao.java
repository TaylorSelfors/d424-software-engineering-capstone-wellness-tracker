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
    void delete(MealEntry entry);

    @Query("SELECT * FROM MealEntry WHERE date = :date")
    List<MealEntry> getMealsByDate(String date);
}

