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

    @Delete
    void delete(ExerciseEntry entry);

    @Query("SELECT * FROM ExerciseEntry WHERE date = :date")
    List<ExerciseEntry> getExercisesByDate(String date);
}
