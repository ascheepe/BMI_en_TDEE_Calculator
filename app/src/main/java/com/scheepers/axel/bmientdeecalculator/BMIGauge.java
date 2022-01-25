package com.scheepers.axel.bmientdeecalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class BMIGauge extends View {
    private final float perfectBmi = (float) 21.75;
    private final float bmiGaugeMax = perfectBmi * 2;

    private Paint gaugePaint;
    private Paint gradientPaint;
    private Paint markerPaint;

    private final int strokeWidth = 4;
    private Rect gaugeRect;
    private Rect innerGaugeRect;
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

        gradientPaint = new Paint();
        gradientPaint.setStyle(Paint.Style.FILL);

        markerPaint = new Paint();
        markerPaint.setColor(0xff000000);
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setStrokeWidth(strokeWidth);

        bmi = perfectBmi;
    }

    public void setBmi(float newBmi) {
        /* Do not over or underflow the scale */
        if (newBmi < 0) {
            bmi = 0;
        } else {
            bmi = Math.min(newBmi, bmiGaugeMax);
        }
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        float width = w - (float)(getPaddingLeft() + getPaddingRight());
        float height = h - (float)(getPaddingTop() + getPaddingBottom());
        final float gradientScale = (float)(1.0 / bmiGaugeMax);
        float bmiScale = width / bmiGaugeMax;

        int[] gradientColors = {
                0xffff0000,
                0xffffff00,
                0xff00ff00,
                0xffffff00,
                0xffff0000
        };

        float[] gradientPositions = {
                0,
                (float) (((17 + 18.5) / 2.0) * gradientScale),
                (float) (21.75 * gradientScale),
                (float) (((25 + 30) / 2.0) * gradientScale),
                1
        };

        if (width <= (strokeWidth * 2) || height <= (strokeWidth * 2)) {
            return;
        }

        LinearGradient gradient = new LinearGradient((float) 0, (float) 0, width, (float) 0,
                gradientColors, gradientPositions, Shader.TileMode.CLAMP);
        gradientPaint.setShader(gradient);

        gaugeRect = new Rect(0, 0, (int) width - 1, (int) height - 1);
        innerGaugeRect = new Rect(strokeWidth, strokeWidth, (int) width - strokeWidth - 1,
                (int) height - strokeWidth - 1);

        markerPosX = bmi * bmiScale;
        markerPosY = height - 1;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(gaugeRect, gaugePaint);
        canvas.drawRect(innerGaugeRect, gradientPaint);
        canvas.drawLine(markerPosX, (float) 0, markerPosX, markerPosY, markerPaint);
    }
}
