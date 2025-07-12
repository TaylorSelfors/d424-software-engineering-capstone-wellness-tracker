package com.tselfor.wellnesstrackercapstone;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.GridView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import com.tselfor.wellnesstrackercapstone.adapters.CalendarAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private int currentMonth;
    private int currentYear;
    private TextView monthYearText;
    private GridView calendarGridView;

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

        calendarGridView = findViewById(R.id.calendarGridView);
        monthYearText = findViewById(R.id.tvMonthYear);

        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        Button btnPrev = findViewById(R.id.btnPreviousMonth);
        Button btnNext = findViewById(R.id.btnNextMonth);

        btnPrev.setOnClickListener(v -> {
            currentMonth--;
            if (currentMonth < 0) {
                currentMonth = 11;
                currentYear--;
            }
            loadCalendar();
        });

        btnNext.setOnClickListener(v -> {
            currentMonth++;
            if (currentMonth > 11) {
                currentMonth = 0;
                currentYear++;
            }
            loadCalendar();
        });

        loadCalendar();

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

    private void loadCalendar() {
        ArrayList<String> daysInMonth = new ArrayList<>();

        Calendar tempCal = Calendar.getInstance();
        tempCal.set(Calendar.YEAR, currentYear);
        tempCal.set(Calendar.MONTH, currentMonth);
        tempCal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDay = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            daysInMonth.add("");
        }

        for (int i = 1; i <= maxDay; i++) {
            daysInMonth.add(String.valueOf(i));
        }

        while (daysInMonth.size() % 7 != 0) {
            daysInMonth.add("");
        }

        ArrayList<Integer> editedPositions = new ArrayList<>();
// TODO: Load actual edited dates from Room database later

// Set up the calendar grid with adapter
        CalendarAdapter adapter = new CalendarAdapter(this, daysInMonth, editedPositions);
        calendarGridView.setAdapter(adapter);

        // Update the month/year label
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        monthYearText.setText(monthNames[currentMonth] + " " + currentYear);

        // Handle cell click to open SummaryActivity
        calendarGridView.setOnItemClickListener((parent, view, position, id) -> {
            String dayText = ((TextView) view.findViewById(R.id.calendarDay)).getText().toString();

            if (!dayText.isEmpty()) {
                String date = String.format("%02d/%02d/%04d",
                        currentMonth + 1,
                        Integer.parseInt(dayText),
                        currentYear);

                Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                intent.putExtra("selectedDate", date);
                startActivity(intent);
            }
        });
    }
}