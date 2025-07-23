package com.tselfor.wellnesstrackercapstone.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tselfor.wellnesstrackercapstone.R;

import java.util.List;

public class MoodAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> moods;

    public MoodAdapter(Context context, List<String> moods) {
        super(context, 0, moods);
        this.context = context;
        this.moods = moods;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createMoodView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createMoodView(position, convertView, parent);
    }

    private View createMoodView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.mood_spinner_item, parent, false);

        View colorView = view.findViewById(R.id.moodColor);
        TextView label = view.findViewById(R.id.moodLabel);

        String mood = moods.get(position);
        label.setText(mood);

        int color = getMoodColor(mood);
        colorView.setBackgroundTintList(ColorStateList.valueOf(color)); // ✅ FIXED HERE

        return view;
    }

    private int getMoodColor(String mood) {
        switch (mood.toLowerCase()) {
            case "fearful": return Color.parseColor("#00BCD4");   // Cyan
            case "angry": return Color.parseColor("#9C27B0");     // Purple
            case "disgusted": return Color.parseColor("#E91E63"); // Pink
            case "sad": return Color.parseColor("#F44336");       // Red
            case "happy": return Color.parseColor("#FF9800");     // Orange
            case "surprised": return Color.parseColor("#FFEB3B"); // Yellow
            case "bad": return Color.parseColor("#4CAF50");       // Green
            default: return Color.GRAY;
        }
    }
}
