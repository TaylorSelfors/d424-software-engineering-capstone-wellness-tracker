package com.tselfor.wellnesstrackercapstone;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.tselfor.wellnesstrackercapstone.adapters.MoodAdapter;
import com.tselfor.wellnesstrackercapstone.data.DayEntry;
import com.tselfor.wellnesstrackercapstone.data.ExerciseEntry;
import com.tselfor.wellnesstrackercapstone.data.MealEntry;
import com.tselfor.wellnesstrackercapstone.database.AppDatabase;
import com.tselfor.wellnesstrackercapstone.database.DatabaseClient;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;


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

        btnAddMeal.setOnClickListener(v -> {
            LinearLayout mealRow = new LinearLayout(SummaryActivity.this);
            mealRow.setOrientation(LinearLayout.HORIZONTAL);
            mealRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            mealRow.setPadding(0, 8, 0, 8);

            // Spinner: Meal Type
            Spinner spinnerType = new Spinner(SummaryActivity.this);
            ArrayAdapter<String> mealTypeAdapter = new ArrayAdapter<>(SummaryActivity.this,
                    android.R.layout.simple_spinner_item,
                    new String[]{"Breakfast", "Lunch", "Dinner", "Snack", "Beverage"});
            mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(mealTypeAdapter);
            spinnerType.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            // EditText: Calories
            EditText etCalories = new EditText(SummaryActivity.this);
            etCalories.setHint("Calories");
            etCalories.setInputType(InputType.TYPE_CLASS_NUMBER);
            etCalories.setTextSize(12);
            etCalories.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            // EditText: Time
            EditText etTime = new EditText(SummaryActivity.this);
            etTime.setHint("Select Time");
            etTime.setFocusable(false);
            etTime.setTextSize(12);
            etTime.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            etTime.setOnClickListener(view -> {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(SummaryActivity.this,
                        (view1, hourOfDay, minute1) -> {
                            Calendar selectedTime = Calendar.getInstance();
                            selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            selectedTime.set(Calendar.MINUTE, minute1);

                            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                            etTime.setText(format.format(selectedTime.getTime()));
                        }, hour, minute, false);
                timePickerDialog.show();
            });


            // Button: Delete
            Button btnDelete = new Button(SummaryActivity.this);
            btnDelete.setText("✖");
            btnDelete.setTextSize(10);
            btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            btnDelete.setOnClickListener(delView -> ((LinearLayout) mealRow.getParent()).removeView(mealRow));

            // 🧠 Add views in exact order for save logic
            mealRow.addView(spinnerType);    // index 0
            mealRow.addView(etCalories);     // index 1
            mealRow.addView(etTime);         // index 2
            mealRow.addView(btnDelete);      // index 3

            mealListLayout.addView(mealRow);
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
            btnDelete.setText("✖");
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

        // Dialog box for mood wheel
        Button btnShowWheel = findViewById(R.id.btnShowEmotionsWheel);

        btnShowWheel.setOnClickListener(v -> {
            Dialog fullscreenDialog = new Dialog(SummaryActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            fullscreenDialog.setContentView(R.layout.dialog_emotions_wheel);

            ImageView ivWheel = fullscreenDialog.findViewById(R.id.ivWheelFullscreen);
            ivWheel.setScaleType(ImageView.ScaleType.FIT_CENTER); // Ensures proper scaling
            ivWheel.setAdjustViewBounds(true); // Keeps aspect ratio

            Button btnClose = fullscreenDialog.findViewById(R.id.btnCloseDialog);
            btnClose.setOnClickListener(d -> fullscreenDialog.dismiss());

            fullscreenDialog.show();
        });


        // Load previously saved data for selectedDate in background
        AppDatabase db = DatabaseClient.getInstance(this).getAppDatabase();
        EditText etJournal = findViewById(R.id.etJournal);

        Executors.newSingleThreadExecutor().execute(() -> {
            DayEntry existingEntry = db.dayEntryDao().getEntryByDate(selectedDate);
            List<MealEntry> meals = db.mealEntryDao().getMealsForDate(selectedDate);
            List<ExerciseEntry> exercises = db.exerciseEntryDao().getExercisesForDate(selectedDate);

            runOnUiThread(() -> {
                if (existingEntry != null) {
                    etHours.setText(String.valueOf(existingEntry.sleepHours));
                    etMinutes.setText(String.valueOf(existingEntry.sleepMinutes));
                    etWater.setText(String.valueOf(existingEntry.waterOunces));
                    etJournal.setText(existingEntry.journal);

                    int moodPosition = moodList.indexOf(existingEntry.mood);
                    if (moodPosition >= 0) {
                        spinnerMood.setSelection(moodPosition);
                    }

                    // Load meals
                    Log.d("DB_TEST", "Loading meals for " + selectedDate);
                    Log.d("DB_TEST", "Found " + meals.size() + " meals");
                    for (MealEntry meal : meals) {
                        Log.d("DB_TEST", "Meal loaded: " + meal.mealType + ", " + meal.timeEaten + ", " + meal.date);

                        LinearLayout mealRow = new LinearLayout(SummaryActivity.this);
                        mealRow.setOrientation(LinearLayout.HORIZONTAL);
                        mealRow.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        Spinner spinnerType = new Spinner(SummaryActivity.this);
                        ArrayAdapter<String> mealTypeAdapter = new ArrayAdapter<>(SummaryActivity.this,
                                android.R.layout.simple_spinner_item,
                                new String[]{"Breakfast", "Lunch", "Dinner", "Snack", "Beverage"});
                        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerType.setAdapter(mealTypeAdapter);
                        spinnerType.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        spinnerType.setSelection(mealTypeAdapter.getPosition(meal.mealType));

                        EditText etCalories = new EditText(SummaryActivity.this);
                        etCalories.setHint("Calories");
                        etCalories.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                        etCalories.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        etCalories.setText(String.valueOf(meal.calories));

                        EditText etTime = new EditText(SummaryActivity.this);
                        etTime.setHint("Select Time");
                        etTime.setFocusable(false);
                        etTime.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        etTime.setText(meal.timeEaten);

                        etTime.setOnClickListener(view -> {
                            Calendar calendar = Calendar.getInstance();
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);

                            TimePickerDialog timePickerDialog = new TimePickerDialog(SummaryActivity.this,
                                    (view1, hourOfDay, minute1) -> {
                                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                                        etTime.setText(formattedTime);
                                    }, hour, minute, false);
                            timePickerDialog.show();
                        });

                        Button btnDelete = new Button(SummaryActivity.this);
                        btnDelete.setText("Delete");
                        btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        btnDelete.setOnClickListener(delView -> ((LinearLayout) mealRow.getParent()).removeView(mealRow));

                        mealRow.addView(spinnerType);
                        mealRow.addView(etCalories);
                        mealRow.addView(etTime);
                        mealRow.addView(btnDelete);

                        mealListLayout.addView(mealRow);
                    }

                    // Load exercises
                    Log.d("DB_TEST", "Found " + exercises.size() + " exercises");
                    for (ExerciseEntry ex : exercises) {
                        Log.d("DB_TEST", "Exercise loaded: " + ex.duration + " mins, " + ex.intensity + ", " + ex.date);

                        LinearLayout exerciseRow = new LinearLayout(SummaryActivity.this);
                        exerciseRow.setOrientation(LinearLayout.HORIZONTAL);
                        exerciseRow.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        exerciseRow.setPadding(0, 8, 0, 8);

                        EditText etDuration = new EditText(SummaryActivity.this);
                        etDuration.setHint("Minutes");
                        etDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
                        etDuration.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        etDuration.setText(String.valueOf(ex.duration));

                        Spinner spinnerIntensity = new Spinner(SummaryActivity.this);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SummaryActivity.this,
                                android.R.layout.simple_spinner_item,
                                new String[]{"Low Intensity", "Moderate Intensity", "High Intensity"});
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerIntensity.setAdapter(adapter);
                        spinnerIntensity.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        spinnerIntensity.setSelection(adapter.getPosition(ex.intensity));

                        Button btnDelete = new Button(SummaryActivity.this);
                        btnDelete.setText("Delete");
                        btnDelete.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        btnDelete.setOnClickListener(delView -> ((LinearLayout) exerciseRow.getParent()).removeView(exerciseRow));

                        exerciseRow.addView(etDuration);
                        exerciseRow.addView(spinnerIntensity);
                        exerciseRow.addView(btnDelete);

                        exerciseListLayout.addView(exerciseRow);
                    }
                }

                // Save summary
                Button btnSave = findViewById(R.id.btnSaveSummary);
                btnSave.setOnClickListener(v -> {
                    int sleepHrs = 0;
                    int sleepMin = 0;
                    int water = 0;

                    try {
                        sleepHrs = Integer.parseInt(etHours.getText().toString());
                    } catch (NumberFormatException ignored) {}

                    try {
                        sleepMin = Integer.parseInt(etMinutes.getText().toString());
                    } catch (NumberFormatException ignored) {}

                    try {
                        water = Integer.parseInt(etWater.getText().toString());
                    } catch (NumberFormatException ignored) {}

                    String mood = spinnerMood.getSelectedItem().toString();
                    String journal = etJournal.getText().toString();

                    DayEntry entry = new DayEntry();
                    entry.date = selectedDate;
                    entry.sleepHours = sleepHrs;
                    entry.sleepMinutes = sleepMin;
                    entry.waterOunces = water;
                    entry.mood = mood;
                    entry.journal = journal;

                    Executors.newSingleThreadExecutor().execute(() -> {

                        db.dayEntryDao().deleteEntryByDate(selectedDate);
                        db.dayEntryDao().insert(entry);

                        db.mealEntryDao().deleteMealsForDate(selectedDate);
                        db.exerciseEntryDao().deleteExercisesForDate(selectedDate);

                        // Save meals
                        for (int i = 0; i < mealListLayout.getChildCount(); i++) {
                            LinearLayout row = (LinearLayout) mealListLayout.getChildAt(i);
                            Spinner type = (Spinner) row.getChildAt(0);
                            EditText cal = (EditText) row.getChildAt(1);
                            EditText time = (EditText) row.getChildAt(2);

                            MealEntry meal = new MealEntry();
                            meal.date = selectedDate;
                            meal.mealType = type.getSelectedItem().toString();

                            try {
                                meal.calories = Integer.parseInt(cal.getText().toString());
                            } catch (NumberFormatException e) {
                                meal.calories = 0;
                            }

                            String rawTime = time.getText().toString().trim();

                            if (!rawTime.matches("^(0?[1-9]|1[0-2]):[0-5][0-9]\\s?(AM|PM|am|pm)?$")) {
                                runOnUiThread(() -> Toast.makeText(SummaryActivity.this,
                                        "Invalid time format. Use h:mm AM/PM (e.g., 8:30 AM)",
                                        Toast.LENGTH_SHORT).show());
                                return;
                            }

                            meal.timeEaten = rawTime;
                            db.mealEntryDao().insert(meal);
                        }

                        // Save exercises
                        for (int i = 0; i < exerciseListLayout.getChildCount(); i++) {
                            LinearLayout row = (LinearLayout) exerciseListLayout.getChildAt(i);
                            EditText duration = (EditText) row.getChildAt(0);
                            Spinner intensity = (Spinner) row.getChildAt(1);

                            ExerciseEntry ex = new ExerciseEntry();
                            ex.date = selectedDate;

                            try {
                                ex.duration = Integer.parseInt(duration.getText().toString());
                            } catch (NumberFormatException e) {
                                ex.duration = 0;
                            }

                            ex.intensity = intensity.getSelectedItem().toString();
                            db.exerciseEntryDao().insert(ex);
                        }

                        runOnUiThread(() -> {
                            Toast.makeText(SummaryActivity.this, "Summary saved!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        });
                    });
                });

            // Delete summary
                Button btnDelete = findViewById(R.id.btnDeleteSummary);
                btnDelete.setOnClickListener(v -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        db.dayEntryDao().deleteByDate(selectedDate);
                        db.mealEntryDao().deleteMealsForDate(selectedDate);
                        db.exerciseEntryDao().deleteExercisesForDate(selectedDate);

                        runOnUiThread(() -> {
                            Toast.makeText(SummaryActivity.this, "Summary deleted.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        });
                    });
                });
            });
        });
        //Share Summary Button
        // Share summary
        Button btnShare = findViewById(R.id.btnShareSummary);
        btnShare.setOnClickListener(v -> {
            StringBuilder summary = new StringBuilder();
            summary.append("Wellness Summary for: ").append(selectedDate).append("\n");

            summary.append("Sleep: ")
                    .append(etHours.getText().toString()).append(" hrs ")
                    .append(etMinutes.getText().toString()).append(" mins\n");

            summary.append("Water: ").append(etWater.getText().toString()).append(" oz\n");

            summary.append("Meals:\n");
            for (int i = 0; i < mealListLayout.getChildCount(); i++) {
                LinearLayout row = (LinearLayout) mealListLayout.getChildAt(i);
                Spinner type = (Spinner) row.getChildAt(0);
                EditText cal = (EditText) row.getChildAt(1);
                EditText time = (EditText) row.getChildAt(2);
                summary.append("- ").append(type.getSelectedItem().toString())
                        .append(" at ").append(time.getText().toString())
                        .append(" (").append(cal.getText().toString()).append(" cal)\n");
            }

            summary.append("Exercise:\n");
            for (int i = 0; i < exerciseListLayout.getChildCount(); i++) {
                LinearLayout row = (LinearLayout) exerciseListLayout.getChildAt(i);
                EditText duration = (EditText) row.getChildAt(0);
                Spinner intensity = (Spinner) row.getChildAt(1);
                summary.append("- ").append(duration.getText().toString())
                        .append(" min, ").append(intensity.getSelectedItem().toString()).append("\n");
            }

            summary.append("Mood: ").append(spinnerMood.getSelectedItem().toString());

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, summary.toString());

            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // Back to Home Button
        Button btnBack = findViewById(R.id.btnBackHome);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}