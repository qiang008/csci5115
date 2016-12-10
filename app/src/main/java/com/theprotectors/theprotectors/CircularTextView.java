package com.theprotectors.theprotectors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircularTextView extends View {
    private String MY_TEXT = "";
    private Path mArc;

    private Paint mPaintText;

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularTextView, 0, 0);

        try {
            float left = a.getFloat(R.styleable.CircularTextView_left, 0);
            float top = a.getFloat(R.styleable.CircularTextView_top, 0);
            float right = a.getFloat(R.styleable.CircularTextView_right, 0);
            float bottom = a.getFloat(R.styleable.CircularTextView_bottom, 0);
            float startAngle = a.getFloat(R.styleable.CircularTextView_startAngle, 0);
            float sweepAngle = a.getFloat(R.styleable.CircularTextView_sweepAngle, 0);
            float textSize = a.getDimension(R.styleable.CircularTextView_textSize, 0);
            String text = a.getString(R.styleable.CircularTextView_text);

            Log.i("", Float.toString(left));
            Log.i("", Float.toString(top));
            Log.i("", text);

            mArc = new Path();
            RectF oval = new RectF(left, top, right, bottom);
            Log.i("", "new rectf created");
            mArc.addArc(oval, startAngle, sweepAngle);
            Log.i("", "arc added");
            mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            Log.i("", "new paint created");
            mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
            Log.i("", "style set");
            mPaintText.setColor(Color.WHITE);
            mPaintText.setTextSize(textSize);

            MY_TEXT = text;
//            Log.i("", "text set");

//            mArc = new Path();
//            RectF oval = new RectF(50,100,200,250);;
//            mArc.addArc(oval, -180, 200);
//            mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
//            mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
//            mPaintText.setColor(Color.WHITE);
//            mPaintText.setTextSize(50f);
        }
        finally {
            Log.i("", "try failed");
            a.recycle();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("", "onDraw was called");
        canvas.drawTextOnPath(MY_TEXT, mArc, 0, 20, mPaintText);
        invalidate();
    }
}