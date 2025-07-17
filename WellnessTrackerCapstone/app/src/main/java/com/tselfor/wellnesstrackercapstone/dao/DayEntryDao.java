package com.tselfor.wellnesstrackercapstone.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import com.tselfor.wellnesstrackercapstone.data.DayEntry;

@Dao
public interface DayEntryDao {

    @Insert
    void insert(DayEntry entry);

    @Update
    void update(DayEntry entry);

    @Query("SELECT * FROM DayEntry WHERE date = :date LIMIT 1")
    DayEntry getEntryByDate(String date);

    @Query("SELECT * FROM DayEntry")
    List<DayEntry> getAllEntries();

    @Query("DELETE FROM DayEntry WHERE date = :date")
    void deleteByDate(String date);

    @Query("DELETE FROM DayEntry WHERE date = :date")
    void deleteEntryByDate(String date);

}

