package com.racine.renderer.series;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.racine.utils.ViewportHandler;

/**
 * Created by sunrx on 2016/10/14.
 */
public class BrokenLineRenderer extends SeriesRenderer {

    public BrokenLineRenderer(ViewportHandler viewportHandler) {
        super(viewportHandler);
    }

    @Override
    protected void drawLinePath(Path path, float preXLocation, float preYLocation, float xLocation, float yLocation, float nextXLocation, float nextYLocation) {
        path.lineTo(xLocation, yLocation);
    }

    @Override
    protected void drawNode(Canvas canvas, float xLocation, float yLocation, Paint seriesPaint) {
        canvas.drawCircle(xLocation, yLocation, 5, seriesPaint);
    }
}
