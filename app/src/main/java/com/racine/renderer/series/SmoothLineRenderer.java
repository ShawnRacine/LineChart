package com.racine.renderer.series;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.racine.utils.ViewportHandler;

/**
 * Created by sunrx on 2016/10/14.
 */
public class SmoothLineRenderer extends SeriesRenderer {
    private static final float SMOOTHNESS = 0.3f; // the higher the smoother, but don't go over 0.5
    private float lX = 0;
    private float lY = 0;

    public SmoothLineRenderer(ViewportHandler viewportHandler) {
        super(viewportHandler);
    }

    @Override
    protected void drawLinePath(Path path, float preXLocation, float preYLocation, float xLocation, float yLocation, float nextXLocation, float nextYLocation) {
        // first control point
        // distance between p and p0
        float d0 = (float) Math.sqrt(Math.pow(xLocation - preXLocation, 2) + Math.pow(yLocation - preYLocation, 2));
        // min is used to avoid going too much right
        float x1 = Math.min(preXLocation + lX * d0, (preXLocation + xLocation) / 2);
        float y1 = preYLocation + lY * d0;

        // second control point
        // distance between p1 and p0 (length of reference line)
        float d1 = (float) Math.sqrt(Math.pow(nextXLocation - preXLocation, 2) + Math.pow(nextYLocation - preYLocation, 2));
        lX = (nextXLocation - preXLocation) / d1 * SMOOTHNESS;// (lX,lY) is the slope of the reference line
        //  lY = (nextYLocation - preYLocation) / d1 * SMOOTHNESS;
        lY = 0;
        //max is used to avoid going too much left
        float x2 = Math.max(xLocation - lX * d0, (preXLocation + xLocation) / 2);
        float y2 = yLocation - lY * d0;

        path.cubicTo(x1, y1, x2, y2, xLocation, yLocation);
    }

    @Override
    protected void drawNode(Canvas canvas, float xLocation, float yLocation, Paint seriesPaint) {
        canvas.drawCircle(xLocation, yLocation, 5, seriesPaint);
    }
}
