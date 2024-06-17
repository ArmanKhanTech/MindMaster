package com.achievix.passcodeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.Nullable;

public class CircleView extends View {
    private Paint mPaint;
    private int color = Color.BLACK;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        double width = (getWidth() - getPaddingLeft() - getPaddingRight()) * 0.5;
        double height = (getHeight() - getPaddingTop() - getPaddingBottom()) * 0.5;

        int cx = (int) (getPaddingLeft() + width);
        int cy = (int) (getPaddingTop() + height);
        int radius = (int) Math.min(width, height);

        mPaint.setColor(color);
        canvas.drawCircle(cx, cy, radius, mPaint);
    }
}
