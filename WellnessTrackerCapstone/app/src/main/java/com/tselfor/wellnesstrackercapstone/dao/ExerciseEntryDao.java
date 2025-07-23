package com.tselfor.wellnesstrackercapstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.tselfor.wellnesstrackercapstone.data.ExerciseEntry;

import java.util.List;

@Dao
public interface ExerciseEntryDao {

    @Insert
    void insert(ExerciseEntry entry);

    @Query("SELECT * FROM exercise_entry WHERE date = :selectedDate")
    List<ExerciseEntry> getExercisesForDate(String selectedDate);

    @Query("DELETE FROM exercise_entry WHERE date = :selectedDate")
    void deleteExercisesForDate(String selectedDate);
}
