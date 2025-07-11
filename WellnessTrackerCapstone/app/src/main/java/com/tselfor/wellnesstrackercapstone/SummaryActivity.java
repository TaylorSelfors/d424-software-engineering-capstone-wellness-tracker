package com.tselfor.wellnesstrackercapstone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                } catch (NumberFormatException ignored) {}

                try {
                    minutes = Integer.parseInt(etMinutes.getText().toString());
                } catch (NumberFormatException ignored) {}

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

        EditText etWater = findViewById(R.id.etWater);
        Button btnAddMeal = findViewById(R.id.btnAddMeal);
        LinearLayout mealListLayout = findViewById(R.id.mealListLayout);

        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new input group for a meal
                LinearLayout mealRow = new LinearLayout(SummaryActivity.this);
                mealRow.setOrientation(LinearLayout.HORIZONTAL);
                mealRow.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                EditText etType = new EditText(SummaryActivity.this);
                etType.setHint("Type");
                etType.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                EditText etCalories = new EditText(SummaryActivity.this);
                etCalories.setHint("Calories");
                etCalories.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                etCalories.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                EditText etTime = new EditText(SummaryActivity.this);
                etTime.setHint("Time");
                etTime.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                mealRow.addView(etType);
                mealRow.addView(etCalories);
                mealRow.addView(etTime);

                mealListLayout.addView(mealRow);
            }
        });

    }
}
