package com.racine.renderer.series;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import com.racine.components.Axis;
import com.racine.components.Series;
import com.racine.utils.ViewportHandler;

import java.util.List;

/**
 * Created by sunrx on 2016/10/14.
 */
public abstract class SeriesRenderer {
    protected ViewportHandler viewportHandler;
    private Axis xAxis, yAxis;

    public SeriesRenderer(ViewportHandler viewportHandler) {
        this.viewportHandler = viewportHandler;
        xAxis = viewportHandler.xAxis;
        yAxis = viewportHandler.yAxis;
    }

    public void drawDataSeriesUnClipped(Canvas canvas, List<? extends Series> seriesList) {
        for (int j = 0; j < seriesList.size(); j++) {
            Paint seriesPaint;
            try {
                seriesPaint = viewportHandler.getSeriesPaints().get(j);
            } catch (Exception e) {
                seriesPaint = viewportHandler.getSeriesPaints().get(0);
            }

            Series series = seriesList.get(j);

            int c = canvas.saveLayer(viewportHandler.layerRect(), null, Canvas.ALL_SAVE_FLAG);

            Path path = new Path();

            int size = series.size();

            for (int i = 0; i < size; i++) {
                String xValue = (String) series.getXValue(i);
                float xLocation = xAxis.getLocation(xValue);

                float yValue = (float) series.getYValue(i);
                float yLocation = yAxis.getLocation(yValue);

                // previous point
                float preXLocation = i == 0 ? xLocation : xAxis.getLocation(series.getXValue(i - 1));
                float preYLocation = i == 0 ? yLocation : yAxis.getLocation(series.getYValue(i - 1));
                // next point
                float nextXLocation = i + 1 >= size ? xLocation : xAxis.getLocation(series.getXValue(i + 1));
                float nextYLocation = i + 1 >= size ? yLocation : yAxis.getLocation(series.getYValue(i + 1));
                //
                if (i == 0) {
                    path.moveTo(xLocation, yLocation);
                }

                drawLinePath(path, preXLocation, preYLocation, xLocation, yLocation, nextXLocation, nextYLocation);

                drawNode(canvas, xLocation, yLocation, seriesPaint);
                //
                Log.i("LineC", "drawDataSeriesUnClipped: " + xLocation + " " + viewportHandler.layerLeft() + " " + viewportHandler.layerRight());
                if (xLocation >= viewportHandler.layerLeft() && xLocation <= viewportHandler.layerRight()) {
                    viewportHandler.setXAxisVisible(i);
                } else {
                    viewportHandler.setXAxisInVisible(i);
                }
            }
            canvas.drawPath(path, seriesPaint);

            canvas.restoreToCount(c);
        }
    }

    protected abstract void drawLinePath(Path path, float preXLocation, float preYLocation, float xLocation, float yLocation, float nextXLocation, float nextYLocation);

    protected abstract void drawNode(Canvas canvas, float xLocation, float yLocation, Paint seriesPaint);
}
