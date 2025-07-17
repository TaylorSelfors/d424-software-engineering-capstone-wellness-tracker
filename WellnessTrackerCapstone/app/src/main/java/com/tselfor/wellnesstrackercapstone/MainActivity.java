package com.tselfor.wellnesstrackercapstone;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tselfor.wellnesstrackercapstone.adapters.CalendarAdapter;
import com.tselfor.wellnesstrackercapstone.database.AppDatabase;
import com.tselfor.wellnesstrackercapstone.database.DatabaseClient;
import com.tselfor.wellnesstrackercapstone.data.DayEntry;
import com.tselfor.wellnesstrackercapstone.data.ExerciseEntry;
import com.tselfor.wellnesstrackercapstone.data.MealEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private int currentMonth;
    private int currentYear;
    private TextView monthYearText;
    private GridView calendarGridView;
    private final String[] monthNames = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

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
        Button btnGenerate = findViewById(R.id.btnGenerateReport);

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

        btnGenerate.setOnClickListener(v -> {
            String report = generateMonthlyReport(currentMonth, currentYear);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Monthly Wellness Report");
            builder.setMessage(report);
            builder.setPositiveButton("OK", null);
            builder.setNegativeButton("Export to PDF", (dialog, which) -> exportReportToPDF(report));
            builder.show();
        });

        // Initial load
        loadCalendar();
    }

    private void loadCalendar() {
        ArrayList<String> daysInMonth = new ArrayList<>();
        Map<Integer, String> moodMap = new HashMap<>();

        Calendar tempCal = Calendar.getInstance();
        tempCal.set(Calendar.YEAR, currentYear);
        tempCal.set(Calendar.MONTH, currentMonth);
        tempCal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1;
        int maxDay = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            daysInMonth.add("");
        }

        int startOffset = daysInMonth.size();

        AppDatabase db = DatabaseClient.getInstance(this).getAppDatabase();
        List<DayEntry> entries = db.dayEntryDao().getAllEntries();

        Map<String, Integer> moodCountMap = new HashMap<>();

        for (int day = 1; day <= maxDay; day++) {
            daysInMonth.add(String.valueOf(day));
            String dateStr = String.format("%02d/%02d/%04d", currentMonth + 1, day, currentYear);

            for (DayEntry entry : entries) {
                if (entry.date.equals(dateStr)) {
                    moodMap.put(startOffset + (day - 1), entry.mood);

                    // Count mood occurrences
                    if (!entry.mood.equals("None")) {
                        moodCountMap.put(entry.mood, moodCountMap.getOrDefault(entry.mood, 0) + 1);
                    }
                }
            }
        }

        while (daysInMonth.size() % 7 != 0) {
            daysInMonth.add("");
        }

        CalendarAdapter adapter = new CalendarAdapter(this, daysInMonth, moodMap);
        calendarGridView.setAdapter(adapter);

        // Update title
        monthYearText.setText(monthNames[currentMonth] + " " + currentYear);

        // Show most frequent mood (only if 7+ days have data)
        TextView tvAverageMood = findViewById(R.id.tvAverageMood);

        if (moodCountMap.isEmpty()) {
            tvAverageMood.setText("There isn't enough data to give an average yet.");
        } else {
            int totalTrackedDays = 0;
            for (int count : moodCountMap.values()) {
                totalTrackedDays += count;
            }

            if (totalTrackedDays < 7) {
                tvAverageMood.setText("There isn't enough data to give an average yet.");
            } else {
                String mostFrequentMood = null;
                int maxCount = 0;

                for (Map.Entry<String, Integer> entry : moodCountMap.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        mostFrequentMood = entry.getKey();
                        maxCount = entry.getValue();
                    }
                }

                if (mostFrequentMood != null) {
                    tvAverageMood.setText("Most Frequent Mood: " + mostFrequentMood);
                } else {
                    tvAverageMood.setText("There isn't enough data to give an average yet.");
                }
            }
        }

        // Click listener for calendar days
        calendarGridView.setOnItemClickListener((parent, view, position, id) -> {
            String dayText = ((TextView) view.findViewById(R.id.calendarDay)).getText().toString();
            if (!dayText.isEmpty()) {
                String date = String.format("%02d/%02d/%04d", currentMonth + 1, Integer.parseInt(dayText), currentYear);
                Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                intent.putExtra("selectedDate", date);
                startActivity(intent);
            }
        });
    }

    private String generateMonthlyReport(int month, int year) {
        AppDatabase db = DatabaseClient.getInstance(this).getAppDatabase();
        List<DayEntry> entries = db.dayEntryDao().getAllEntries();

        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        StringBuilder report = new StringBuilder();
        report.append("Wellness Report - ").append(monthNames[month]).append(" ").append(year).append("\n\n");

        for (DayEntry entry : entries) {
            try {
                String[] parts = entry.date.split("/");
                int entryMonth = Integer.parseInt(parts[0]) - 1;
                int entryYear = Integer.parseInt(parts[2]);

                if (entryMonth == month && entryYear == year) {
                    report.append("📅 ").append(entry.date).append("\n");
                    report.append("• Mood: ").append(entry.mood).append("\n");
                    report.append("• Sleep: ").append(entry.sleepHours).append("h ").append(entry.sleepMinutes).append("m\n");
                    report.append("• Water: ").append(entry.waterOunces).append(" oz\n");

                    // Add Journal
                    if (entry.journal != null && !entry.journal.trim().isEmpty()) {
                        report.append("• Journal: ").append(entry.journal).append("\n");
                    }

                    // Meals
                    List<MealEntry> meals = db.mealEntryDao().getMealsForDate(entry.date);
                    if (!meals.isEmpty()) {
                        report.append("• Meals:\n");
                        for (MealEntry meal : meals) {
                            report.append("   - ").append(meal.mealType).append(" at ").append(meal.timeEaten)
                                    .append(": ").append(meal.calories).append(" cal\n");
                        }
                    }

                    // Exercises
                    List<ExerciseEntry> exercises = db.exerciseEntryDao().getExercisesForDate(entry.date);
                    if (!exercises.isEmpty()) {
                        report.append("• Exercises:\n");
                        for (ExerciseEntry ex : exercises) {
                            report.append("   - ").append(ex.duration).append(" mins (").append(ex.intensity).append(")\n");
                        }
                    }

                    report.append("\n"); // spacing between days
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return report.toString();
    }

    private void exportReportToPDF(String reportContent) {
        String fileName = "Wellness_Report_" + (currentMonth + 1) + "_" + currentYear + ".pdf";

        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int x = 40, y = 50;
        for (String line : reportContent.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += 20;
            if (y > 800) break; // Simple 1-page overflow check
        }

        document.finishPage(page);

        File path = new File(getExternalFilesDir(null), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(path);
            document.writeTo(fos);
            Toast.makeText(this, "PDF saved: " + path.getAbsolutePath(), Toast.LENGTH_LONG).show();

            // Optionally open the PDF
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(FileProvider.getUriForFile(this, getPackageName() + ".provider", path), "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF", Toast.LENGTH_SHORT).show();
        }

        document.close();
    }
}
