package com.google.developer.bugmaster.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.developer.bugmaster.R;

//TODO: This class should be used in the insect list to display danger level
public class DangerLevelView extends TextView {

    private int mDangerLevel;

    public DangerLevelView(Context context) {
        super(context);
    }

    public DangerLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DangerLevelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setDangerLevel(int dangerLevel) {
        //TODO: Update the view appropriately based on the level input
        mDangerLevel = dangerLevel;
        setTextColor(getResources().getColor(R.color.dangerColor_text));
        if (dangerLevel < 1) {
            dangerLevel = 1;
        } else if (dangerLevel > 10) {
            dangerLevel = 10;
        }
        int color = Color.parseColor(getResources().getStringArray(R.array.dangerColors)[dangerLevel-1]);
        setBackgroundTintList(ColorStateList.valueOf(color));
        setText(String.valueOf(dangerLevel));
    }

    public int getDangerLevel() {
        //TODO:
        return mDangerLevel;
    }

}
