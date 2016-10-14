package com.racine.renderer.series;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.racine.components.Axis;
import com.racine.renderer.axis.AxisRenderer;

/**
 * Created by sunrx on 2016/10/14.
 */
public class BrokenLineRender extends SeriesRenderer {

    public BrokenLineRender(RectF layerRectF, AxisRenderer xAxisRenderer, AxisRenderer yAxisRenderer, Axis xAxis) {
        super(layerRectF, xAxisRenderer, yAxisRenderer, xAxis);
    }

    @Override
    protected void drawLinePath(Path path, float preXLocation, float preYLocation, float xLocation, float yLocation, float nextXLocation, float nextYLocation) {
        path.lineTo(xLocation, yLocation);
    }

    @Override
    protected void drawNode(Canvas canvas, float xLocation, float yLocation, Paint seriesPaint) {
        canvas.drawCircle(xLocation, yLocation, 5, getGridPaint());
    }

    @Override
    protected void drawGrid(Canvas canvas, float xLocation) {
        canvas.drawLine(xLocation, layerRectF.top, xLocation, layerRectF.bottom, getGridPaint());
    }
}
