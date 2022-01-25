package com.scheepers.axel.bmientdeecalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

public class BMIGauge extends View {
    private final float perfectBmi = (float) 21.75;
    private final float bmiGaugeMin = 10;
    private final float bmiGaugeMax = 50;
    private final float scale = (float) 1.0 / (bmiGaugeMax - bmiGaugeMin);
    private float bmiScale;

    private float width;
    private float height;

    private Paint gaugePaint;
    private Paint gradientPaint;
    private Paint markerPaint;
    private Paint textPaint;

    private final int strokeWidth = 4;
    private Rect gaugeRect;
    private Rect innerGaugeRect;
    private Rect textBounds;
    private float markerPosX, markerPosY;

    private float bmi;

    private boolean validSize;

    public BMIGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        gaugePaint = new Paint();
        gaugePaint.setColor(0xff000000);
        gaugePaint.setStyle(Paint.Style.FILL);

        gradientPaint = new Paint();
        gradientPaint.setStyle(Paint.Style.FILL);

        markerPaint = new Paint();
        markerPaint.setColor(0xff000000);
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setStrokeWidth(strokeWidth);

        textPaint = new Paint();
        textPaint.setColor(0xff000000);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(5);
        textPaint.setTextSize(25);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textBounds = new Rect();

        bmi = perfectBmi;
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
        width = w - (float)(getPaddingLeft() + getPaddingRight());
        height = h - (float)(getPaddingTop() + getPaddingBottom());

        if (width < 150 || height < 75) {
            validSize = false;
            return;
        }

        validSize = true;
        bmiScale = width / (bmiGaugeMax - bmiGaugeMin);

        int[] gradientColors = {
                0xffff0000,
                0xffffff00,
                0xff00ff00,
                0xffffff00,
                0xffff0000
        };

        float[] gradientPositions = {
                0,
                (float) (((17 + 18.5) / 2.0 - bmiGaugeMin) * scale),
                (float) (21.75 - bmiGaugeMin) * scale,
                (float) (((25 + 30) / 2.0 - bmiGaugeMin) * scale),
                1
        };

        LinearGradient gradient = new LinearGradient((float) 0, (float) 0, width, (float) 0,
                gradientColors, gradientPositions, Shader.TileMode.CLAMP);
        gradientPaint.setShader(gradient);

        gaugeRect = new Rect(0, 0, (int) width - 1, (int) height - 1);
        //noinspection SuspiciousNameCombination
        innerGaugeRect = new Rect(strokeWidth, strokeWidth, (int) width - strokeWidth - 1,
                (int) height - strokeWidth - 1);

        markerPosX = (bmi - bmiGaugeMin) * bmiScale;
        markerPosY = height - 1;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!validSize) {
            return;
        }

        canvas.drawRect(gaugeRect, gaugePaint);
        canvas.drawRect(innerGaugeRect, gradientPaint);
        canvas.drawLine(markerPosX, (float) 0, markerPosX, markerPosY, markerPaint);

        for (int i = (int) bmiGaugeMin + 5; i < (int) bmiGaugeMax - 5; ++i) {
            if (i % 10 == 0) {
                canvas.drawLine((i - bmiGaugeMin) * bmiScale, height - 1,
                        (i - bmiGaugeMin) * bmiScale,
                        height - 21, markerPaint);
                canvas.drawLine((i - bmiGaugeMin) * bmiScale, 0,
                        (i - bmiGaugeMin) * bmiScale,
                        20, markerPaint);
                String text = String.format(Locale.getDefault(), "%d", i);

                textPaint.getTextBounds(text, 0, text.length(), textBounds);
                canvas.drawText(text, (i - bmiGaugeMin) * bmiScale,
                        height / (float) 2.0 + textBounds.height() / (float) 2.0,
                        textPaint);
            } else if (i % 5 == 0) {
                canvas.drawLine(i * bmiScale, height - 1, i * bmiScale,
                        height - 11, markerPaint);
                canvas.drawLine(i * bmiScale, 0, i * bmiScale,
                        10, markerPaint);
            }
        }
    }
}
