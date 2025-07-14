package com.tselfor.wellnesstrackercapstone.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.tselfor.wellnesstrackercapstone.R;

import java.util.ArrayList;
import java.util.Map;

public class CalendarAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<String> days;
    private final Map<Integer, String> moodMap;  // Mood type for specific positions

    public CalendarAdapter(Context context, ArrayList<String> days, Map<Integer, String> moodMap) {
        this.context = context;
        this.days = days;
        this.moodMap = moodMap;
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
            dayText.setBackgroundColor(Color.TRANSPARENT);
            dayText.setEnabled(false);
        } else {
            dayText.setEnabled(true);

            // Default gray background
            int defaultColor = ContextCompat.getColor(context, android.R.color.darker_gray);
            int colorToApply = defaultColor;

            // Override color if mood exists for that position
            if (moodMap.containsKey(position)) {
                String mood = moodMap.get(position);
                colorToApply = getMoodColor(mood);
            }

            dayText.setBackgroundColor(colorToApply);
        }

        return dayView;
    }

    private int getMoodColor(String mood) {
        switch (mood.toLowerCase()) {
            case "fearful": return Color.parseColor("#00BCD4"); // Cyan
            case "angry": return Color.parseColor("#9C27B0");   // Purple
            case "disgusted": return Color.parseColor("#E91E63"); // Pink
            case "sad": return Color.parseColor("#F44336");     // Red
            case "happy": return Color.parseColor("#FF9800");   // Orange
            case "surprised": return Color.parseColor("#FFEB3B"); // Yellow
            case "bad": return Color.parseColor("#4CAF50");     // Green
            default: return ContextCompat.getColor(context, android.R.color.darker_gray);
        }
    }
}
