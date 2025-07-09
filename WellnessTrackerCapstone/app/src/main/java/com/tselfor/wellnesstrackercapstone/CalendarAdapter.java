package com.tselfor.wellnesstrackercapstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<String> days;
    private final ArrayList<Integer> editedPositions;

    public CalendarAdapter(Context context, ArrayList<String> days, ArrayList<Integer> editedPositions) {
        this.context = context;
        this.days = days;
        this.editedPositions = editedPositions;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View dayView = convertView;
        if (dayView == null) {
            dayView = LayoutInflater.from(context).inflate(R.layout.calendar_day, parent, false);
        }

        TextView dayText = dayView.findViewById(R.id.calendarDay);
        String day = days.get(position);
        dayText.setText(day);

        if (day.isEmpty()) {
            // Empty day: transparent box, not clickable
            dayText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            dayText.setEnabled(false);
        } else {
            // Default background (gray)
            dayText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            dayText.setEnabled(true);

            // Highlight edited days in blue
            if (editedPositions.contains(position)) {
                dayText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
            }
        }

        return dayView;
    }
}
