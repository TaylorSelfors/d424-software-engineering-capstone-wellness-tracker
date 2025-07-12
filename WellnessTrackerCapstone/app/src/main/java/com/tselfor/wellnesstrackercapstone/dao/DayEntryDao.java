package com.tselfor.wellnesstrackercapstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tselfor.wellnesstrackercapstone.data.DayEntry;

@Dao
public interface DayEntryDao {

    @Insert
    void insert(DayEntry entry);

    @Update
    void update(DayEntry entry);

    @Query("SELECT * FROM DayEntry WHERE date = :date LIMIT 1")
    DayEntry getEntryByDate(String date);
}

