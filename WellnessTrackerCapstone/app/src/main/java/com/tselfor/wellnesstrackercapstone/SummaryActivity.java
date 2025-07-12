package com.tselfor.wellnesstrackercapstone;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;
import android.content.Intent;

import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import com.tselfor.wellnesstrackercapstone.adapters.MoodAdapter;
import com.tselfor.wellnesstrackercapstone.data.DayEntry;
import com.tselfor.wellnesstrackercapstone.database.AppDatabase;
import com.tselfor.wellnesstrackercapstone.database.DatabaseClient;
import com.tselfor.wellnesstrackercapstone.dao.DayEntryDao;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Get selected date from intent
        TextView summaryDate = findViewById(R.id.tvSummaryDate);
        String selectedDate = getIntent().getStringExtra("selectedDate");
        summaryDate.setText("Summary for: " + selectedDate);

        // Sleep section
        EditText etHours = findViewById(R.id.etHours);
        EditText etMinutes = findViewById(R.id.etMinutes);
        Button btnCalculateSleep = findViewById(R.id.btnCalculateSleep);
        TextView tvSleepMessage = findViewById(R.id.tvSleepMessage);

        btnCalculateSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours = 0;
                int minutes = 0;

                try {
                    hours = Integer.parseInt(etHours.getText().toString());
                } catch (NumberFormatException ignored) {
                }

                try {
                    minutes = Integer.parseInt(etMinutes.getText().toString());
                } catch (NumberFormatException ignored) {
                }

                double totalSleep = hours + (minutes / 60.0);

                if (totalSleep >= 8) {
                    tvSleepMessage.setText("Great job! Getting a good night's rest is the best thing you can do for your health!");
                } else if (totalSleep >= 6) {
                    tvSleepMessage.setText("Getting at least 8 hours of sleep is optimal for good health!");
                } else {
                    tvSleepMessage.setText("Getting less than 6 hours of sleep can jeopardize the body’s natural healing mechanisms!");
                }
            }
        });

        // Food and water
        EditText etWater = findViewById(R.id.etWater);
        Button btnAddMeal = findViewById(R.id.btnAddMeal);
        LinearLayout mealListLayout = findViewById(R.id.mealListLayout);

        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout mealRow = new LinearLayout(SummaryActivity.this);
                mealRow.setOrientation(LinearLayout.HORIZONTAL);
                mealRow.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // Spinner for Meal Type
                Spinner spinnerType = new Spinner(SummaryActivity.this);
                ArrayAdapter<String> mealTypeAdapter = new ArrayAdapter<>(SummaryActivity.this,
                        android.R.layout.simple_spinner_item,
                        new String[]{"Breakfast", "Lunch", "Dinner", "Snack", "Beverage"});
                mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerType.setAdapter(mealTypeAdapter);
                spinnerType.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                // EditText for Calories
                EditText etCalories = new EditText(SummaryActivity.this);
                etCalories.setHint("Calories");
                etCalories.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                etCalories.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                // EditText for Time
                EditText etTime = new EditText(SummaryActivity.this);
                etTime.setHint("Time");
                etTime.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                // Delete Button
                Button btnDelete = new Button(SummaryActivity.this);
                btnDelete.setText("Delete");
                btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                btnDelete.setOnClickListener(delView -> ((LinearLayout) mealRow.getParent()).removeView(mealRow));

                // Add all views to row
                mealRow.addView(spinnerType);
                mealRow.addView(etCalories);
                mealRow.addView(etTime);
                mealRow.addView(btnDelete);

                // Add row to layout
                mealListLayout.addView(mealRow);
            }
        });

        // Exercise
        Button btnAddExercise = findViewById(R.id.btnAddExercise);
        LinearLayout exerciseListLayout = findViewById(R.id.exerciseListLayout);

        btnAddExercise.setOnClickListener(v -> {
            LinearLayout exerciseRow = new LinearLayout(SummaryActivity.this);
            exerciseRow.setOrientation(LinearLayout.HORIZONTAL);
            exerciseRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            exerciseRow.setPadding(0, 8, 0, 8); // vertical spacing

            // Duration input
            EditText etDuration = new EditText(SummaryActivity.this);
            etDuration.setHint("Minutes");
            etDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
            etDuration.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            // Intensity spinner
            Spinner spinnerIntensity = new Spinner(SummaryActivity.this);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(SummaryActivity.this,
                    android.R.layout.simple_spinner_item,
                    new String[]{"Low Intensity", "Moderate Intensity", "High Intensity"});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerIntensity.setAdapter(adapter);
            spinnerIntensity.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            // Delete button
            Button btnDelete = new Button(SummaryActivity.this);
            btnDelete.setText("Delete");
            btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            btnDelete.setOnClickListener(delView -> ((LinearLayout) exerciseRow.getParent()).removeView(exerciseRow));

            // Add views to row
            exerciseRow.addView(etDuration);
            exerciseRow.addView(spinnerIntensity);
            exerciseRow.addView(btnDelete);

            // Add row to layout
            exerciseListLayout.addView(exerciseRow);
        });


        // Mood
        Spinner spinnerMood = findViewById(R.id.spinnerMood);

        List<String> moodList = Arrays.asList(
                "None", "Fearful", "Angry", "Disgusted", "Sad", "Happy", "Surprised", "Bad"
        );

        MoodAdapter moodAdapter = new MoodAdapter(this, moodList);
        spinnerMood.setAdapter(moodAdapter);

        // Save summary
        Button btnSave = findViewById(R.id.btnSaveSummary);
        EditText etJournal = findViewById(R.id.etJournal);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sleepHrs = 0;
                int sleepMin = 0;
                int water = 0;

                try {
                    sleepHrs = Integer.parseInt(etHours.getText().toString());
                } catch (NumberFormatException ignored) {
                }

                try {
                    sleepMin = Integer.parseInt(etMinutes.getText().toString());
                } catch (NumberFormatException ignored) {
                }

                try {
                    water = Integer.parseInt(etWater.getText().toString());
                } catch (NumberFormatException ignored) {
                }

                String mood = spinnerMood.getSelectedItem().toString();
                String journal = etJournal.getText().toString();

                DayEntry entry = new DayEntry();
                entry.date = selectedDate;
                entry.sleepHours = sleepHrs;
                entry.sleepMinutes = sleepMin;
                entry.waterOunces = water;
                entry.mood = mood;
                entry.journal = journal;

                AppDatabase db = DatabaseClient.getInstance(SummaryActivity.this).getAppDatabase();
                db.dayEntryDao().insert(entry);

                Toast.makeText(SummaryActivity.this, "Summary saved!", Toast.LENGTH_SHORT).show();
            }
        });

        // Share summary
        Button btnShare = findViewById(R.id.btnShareSummary);
        btnShare.setOnClickListener(v -> {
            String summary = "Wellness Summary for " + selectedDate + ":\n";

            try {
                int h = Integer.parseInt(etHours.getText().toString());
                int m = Integer.parseInt(etMinutes.getText().toString());
                summary += "- Sleep: " + h + "h " + m + "m\n";
            } catch (Exception ignored) {}

            try {
                int water = Integer.parseInt(etWater.getText().toString());
                summary += "- Water: " + water + " oz\n";
            } catch (Exception ignored) {}

            summary += "- Mood: " + spinnerMood.getSelectedItem().toString() + "\n";

            String journal = etJournal.getText().toString().trim();
            if (!journal.isEmpty()) {
                summary += "- Journal: " + journal + "\n";
            }

            summary += "- Meals:\n";
            for (int i = 0; i < mealListLayout.getChildCount(); i++) {
                LinearLayout row = (LinearLayout) mealListLayout.getChildAt(i);
                Spinner type = (Spinner) row.getChildAt(0);
                EditText cal = (EditText) row.getChildAt(1);
                EditText time = (EditText) row.getChildAt(2);
                summary += "  • " + type.getSelectedItem() + " @ " + time.getText().toString()
                        + " — " + cal.getText().toString() + " cal\n";
            }

            summary += "- Exercises:\n";
            for (int i = 0; i < exerciseListLayout.getChildCount(); i++) {
                LinearLayout row = (LinearLayout) exerciseListLayout.getChildAt(i);
                EditText duration = (EditText) row.getChildAt(0);
                Spinner intensity = (Spinner) row.getChildAt(1);
                summary += "  • " + duration.getText().toString() + " mins — " + intensity.getSelectedItem() + " intensity\n";
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Wellness Summary");
            shareIntent.putExtra(Intent.EXTRA_TEXT, summary);

            startActivity(Intent.createChooser(shareIntent, "Share your wellness summary via"));
        });


        Button btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> finish());

    }
}
