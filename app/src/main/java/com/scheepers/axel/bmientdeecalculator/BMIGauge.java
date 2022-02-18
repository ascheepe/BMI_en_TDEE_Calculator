package com.scheepers.axel.bmientdeecalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class BMIGauge extends View {
    private final float bmiGaugeMin = 10;
    private final float bmiGaugeMax = 50;

    private Paint gaugePaint, redPaint, yellowPaint, greenPaint;
    private Paint markerPaint;

    private Rect gaugeRect;
    private Rect wayTooLowRect, tooLowRect, okRect, tooHighRect, wayTooHighRect;

    private float markerPosX, markerPosY;

    private float bmi;

    public BMIGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gaugePaint = new Paint();
        gaugePaint.setColor(0xff000000);
        gaugePaint.setStyle(Paint.Style.FILL);

        redPaint = new Paint();
        redPaint.setColor(0xff7f0000);
        yellowPaint = new Paint();
        yellowPaint.setColor(0xff7f7f00);
        greenPaint = new Paint();
        greenPaint.setColor(0xff007f00);

        markerPaint = new Paint();
        markerPaint.setColor(0xff000000);
        markerPaint.setStyle(Paint.Style.STROKE);

        int strokeWidth = 4;
        markerPaint.setStrokeWidth(strokeWidth);

        bmi = (float) 21.75;
    }

    public void setBmi(float newBmi) {
        /* Do not over or underflow the scale */
        if (newBmi < bmiGaugeMin) {
            bmi = bmiGaugeMin;
        } else {
            bmi = Math.min(newBmi, bmiGaugeMax);
        }
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        float width = w - (float)(getPaddingLeft() + getPaddingRight());
        float height = h - (float)(getPaddingTop() + getPaddingBottom());
        float scale = width / (bmiGaugeMax - bmiGaugeMin);

        int bottom = (int) height - 1;
        int end = (int) width - 1;
        int[] pos = {
                (int) ((17 - bmiGaugeMin) * scale),
                (int) ((18.5 - bmiGaugeMin) * scale),
                (int) ((25 - bmiGaugeMin) * scale),
                (int) ((30 - bmiGaugeMin) * scale),
                (int) ((35 - bmiGaugeMin) * scale),
        };

        gaugeRect = new Rect(0, 0, end, bottom);

        wayTooLowRect = new Rect( 0, 0, pos[0], bottom);
        tooLowRect = new Rect(pos[0], 0, pos[1], bottom);
        okRect = new Rect(pos[1], 0, pos[2], bottom);
        tooHighRect = new Rect(pos[2], 0, pos[3], bottom);
        wayTooHighRect = new Rect(pos[3], 0, end, bottom);

        markerPosX = (bmi - bmiGaugeMin) * scale;
        markerPosY = height - 1;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(gaugeRect, gaugePaint);
        canvas.drawRect(wayTooLowRect, redPaint);
        canvas.drawRect(tooLowRect, yellowPaint);
        canvas.drawRect(okRect, greenPaint);
        canvas.drawRect(tooHighRect, yellowPaint);
        canvas.drawRect(wayTooHighRect, redPaint);
        canvas.drawLine(markerPosX, (float) 0, markerPosX, markerPosY, markerPaint);
    }
}
