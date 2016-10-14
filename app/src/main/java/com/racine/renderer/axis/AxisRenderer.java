package com.racine.renderer.axis;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import com.racine.components.Axis;

/**
 * Created by sunrx on 2016/10/14.
 */
public abstract class AxisRenderer<T> {
    protected RectF displayRectF;
    protected Axis axis;
    private Paint axisPaint;
    private Paint labelPaint;

    public AxisRenderer(RectF displayRectF, Axis axis) {
        this.displayRectF = displayRectF;
        this.axis = axis;

        axisPaint = new Paint();
        labelPaint = new Paint();

        renderAxisPaint(axisPaint);
        renderLabelPaint(labelPaint);
    }

    protected void renderAxisPaint(Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
    }

    protected void renderLabelPaint(Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(26);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    protected Paint getAxisPaint() {
        return axisPaint;
    }

    protected Paint getLabelPaint() {
        return labelPaint;
    }

    public abstract void drawAxisLine(Canvas canvas);

    public abstract void drawAxisLabel(Canvas canvas);

    /**
     * get location by value.
     *
     * @param value value from datasource.
     * @return location.
     */
    public abstract float getLocation(T value);
}
