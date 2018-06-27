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

    private float strokeWidth;
    Paint circlePaint;
    int strokeColor, solidColor;
    String Text;
    Float size = -1f;
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
        //this.strokeColor = color;
        //this.solidColor = color;
        setText(String.valueOf(dangerLevel));
        //this.Text = ;
        //this.size = value;
    }

    public int getDangerLevel() {
        //TODO:
        return mDangerLevel;
    }

//    @Override
//    public void draw(Canvas canvas) {
//        final int diameter, radius, h, w;
//
//        circlePaint.setColor(solidColor);
//        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        Paint strokePaint = new Paint();
//        strokePaint.setColor(strokeColor);
//        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//
//        //get Height and Width
//        h = this.getHeight();
//        w = this.getWidth();
//
//        diameter = ((h > w) ? h : w);
//        radius = diameter / 2;
//
//        //setting Height and width
//        this.setHeight(diameter);
//        this.setWidth(diameter);
//        this.setText(Text);
//
//        if (size != -1f) {
//
//            this.setTextSize(size);
//        } else {
//
//            this.setTextSize(diameter / 5);
//        }
//
//        canvas.drawCircle(diameter / 2, diameter / 2, radius - strokeWidth, circlePaint);
//        super.draw(canvas);
//    }


}
