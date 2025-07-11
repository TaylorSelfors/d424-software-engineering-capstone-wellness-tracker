package com.tselfor.wellnesstrackercapstone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    }
}
