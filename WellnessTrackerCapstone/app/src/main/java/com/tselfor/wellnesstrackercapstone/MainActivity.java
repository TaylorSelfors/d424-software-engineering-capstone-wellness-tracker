package com.tselfor.wellnesstrackercapstone;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GridView calendarGridView = findViewById(R.id.calendarGridView);
        TextView monthYearText = findViewById(R.id.tvMonthYear);

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

// Get days
        ArrayList<String> daysInMonth = new ArrayList<>();
        Calendar tempCal = Calendar.getInstance();
        tempCal.set(Calendar.YEAR, currentYear);
        tempCal.set(Calendar.MONTH, currentMonth);
        tempCal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
        int maxDay = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            daysInMonth.add(""); // Blank cells for alignment
        }

// ✅ THIS WAS MISSING:
        for (int i = 1; i <= maxDay; i++) {
            daysInMonth.add(String.valueOf(i));
        }

// Add trailing blanks
        int totalCells = daysInMonth.size();
        while (totalCells % 7 != 0) {
            daysInMonth.add("");
            totalCells++;
        }

        // Simulate edited days for now
        ArrayList<Integer> editedPositions = new ArrayList<>();
        editedPositions.add(3);  // Example: day 3
        editedPositions.add(10); // Example: day 10

        CalendarAdapter adapter = new CalendarAdapter(this, daysInMonth, editedPositions);
        calendarGridView.setAdapter(adapter);

        // Update the month-year text
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        monthYearText.setText(monthNames[currentMonth] + " " + currentYear);
    }
}