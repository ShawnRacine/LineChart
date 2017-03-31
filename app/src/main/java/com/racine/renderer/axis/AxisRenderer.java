package com.racine.renderer.axis;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.racine.utils.ViewportHandler;

/**
 * Created by sunrx on 2016/10/14.
 */
public abstract class AxisRenderer<T> {
    protected ViewportHandler viewportHandler;
    private Paint axisPaint;
    private Paint gridPaint;
    private Paint labelPaint;
    protected boolean drawGridLines = true;

    public AxisRenderer(ViewportHandler viewportHandler) {
        this.viewportHandler = viewportHandler;

        axisPaint = new Paint();
        gridPaint = new Paint();
        labelPaint = new Paint();

        renderAxisPaint(axisPaint);
        renderGridPaint(gridPaint);
        renderLabelPaint(labelPaint);
    }

    protected void renderAxisPaint(Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
    }

    protected void renderGridPaint(Paint paint){
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

    protected Paint getGridPaint(){
        return gridPaint;
    }

    protected Paint getLabelPaint() {
        return labelPaint;
    }

    /**
     * @param drawlines
     */
    public void drawGridLines(boolean drawlines) {
        drawGridLines = drawlines;
    }

    public abstract void drawAxisLine(Canvas canvas);

    public abstract void drawAxisLabel(Canvas canvas);
}
