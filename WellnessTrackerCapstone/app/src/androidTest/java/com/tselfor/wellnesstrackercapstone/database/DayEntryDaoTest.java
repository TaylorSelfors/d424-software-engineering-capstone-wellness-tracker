package com.tselfor.wellnesstrackercapstone.database;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.tselfor.wellnesstrackercapstone.dao.DayEntryDao;
import com.tselfor.wellnesstrackercapstone.dao.MealEntryDao;
import com.tselfor.wellnesstrackercapstone.data.DayEntry;
import com.tselfor.wellnesstrackercapstone.data.MealEntry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DayEntryDaoTest {

    private AppDatabase db;
    private DayEntryDao dayEntryDao;
    private MealEntryDao mealEntryDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        dayEntryDao = db.dayEntryDao();
        mealEntryDao = db.mealEntryDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertDayEntryAndRead() {
        DayEntry entry = new DayEntry("2025-07-23", 7, 30, 80, "Happy", "Felt good today");
        dayEntryDao.insert(entry);
        DayEntry result = dayEntryDao.getEntryByDate("2025-07-23");
        assertNotNull(result);
        assertEquals(7, result.getSleepHours());
    }

    @Test
    public void insertAndDeleteMealEntry() {
        MealEntry meal = new MealEntry("2025-07-23", "Lunch", "12:30", 600);
        mealEntryDao.insert(meal);
        List<MealEntry> meals = mealEntryDao.getMealsForDate("2025-07-23");
        assertEquals(1, meals.size());

        mealEntryDao.delete(meals.get(0));
        List<MealEntry> afterDelete = mealEntryDao.getMealsForDate("2025-07-23");
        assertTrue(afterDelete.isEmpty());
    }

    @Test
    public void getMoodForDateIsCorrect() {
        DayEntry entry = new DayEntry("2025-07-22", 8, 0, 70, "Sad", "Rainy day");
        dayEntryDao.insert(entry);
        DayEntry result = dayEntryDao.getEntryByDate("2025-07-22");
        assertEquals("Sad", result.getMood());
    }
}
