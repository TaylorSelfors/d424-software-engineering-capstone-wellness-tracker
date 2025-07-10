package com.tselfor.wellnesstrackercapstone;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        TextView summaryDate = findViewById(R.id.tvSummaryDate);

        String selectedDate = getIntent().getStringExtra("selectedDate");
        summaryDate.setText("Summary for: " + selectedDate);
    }
}
