package com.racine.renderer.series;

import android.graphics.*;
import com.racine.components.Series;
import com.racine.utils.ViewportHandler;
import com.racine.renderer.axis.AxisRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunrx on 2016/10/14.
 */
public abstract class SeriesRenderer {
    protected ViewportHandler viewportHandler;
    protected AxisRenderer xAxisRenderer;
    protected AxisRenderer yAxisRenderer;

    private List<Paint> seriesPaintList;
    private Paint gridPaint;

    public SeriesRenderer(ViewportHandler viewportHandler, AxisRenderer xAxisRenderer, AxisRenderer yAxisRenderer) {
        this.viewportHandler = viewportHandler;
        this.xAxisRenderer = xAxisRenderer;
        this.yAxisRenderer = yAxisRenderer;

        seriesPaintList = new ArrayList<>();
        gridPaint = new Paint();

        renderSeriesPaint(seriesPaintList);
        renderGridPaint(gridPaint);
    }

    protected void renderSeriesPaint(List<Paint> paintList) {
        Paint seriesPaint = new Paint();
        seriesPaint.setColor(Color.BLUE);
        seriesPaint.setStyle(Paint.Style.STROKE);
        seriesPaint.setStrokeWidth(5f);
        seriesPaint.setAntiAlias(true);

        paintList.add(seriesPaint);
    }

    protected void renderGridPaint(Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1f);
    }

    protected Paint getGridPaint() {
        return gridPaint;
    }

    public void drawDataSeriesUnClipped(Canvas canvas, List<Series> seriesList) {
        for (int j = 0; j < seriesList.size(); j++) {
            Paint seriesPaint;
            try {
                seriesPaint = seriesPaintList.get(j);
            } catch (Exception e) {
                seriesPaint = seriesPaintList.get(0);
            }

            Series series = seriesList.get(j);

            int c = canvas.saveLayer(viewportHandler.layerRect(), null, Canvas.ALL_SAVE_FLAG);

            Path path = new Path();

            int size = series.size();

            for (int i = 0; i < size; i++) {
                String xValue = series.getXValue(i);
                float xLocation = xAxisRenderer.getLocation(xValue);

                float yValue = series.getYValue(i);
                float yLocation = yAxisRenderer.getLocation(yValue);

                // previous point
                float preXLocation = i == 0 ? xLocation : xAxisRenderer.getLocation(series.getXValue(i - 1));
                float preYLocation = i == 0 ? yLocation : yAxisRenderer.getLocation(series.getYValue(i - 1));
                // next point
                float nextXLocation = i + 1 >= size ? xLocation : xAxisRenderer.getLocation(series.getXValue(i + 1));
                float nextYLocation = i + 1 >= size ? yLocation : yAxisRenderer.getLocation(series.getYValue(i + 1));
                //
                drawLinePath(path, preXLocation, preYLocation, xLocation, yLocation, nextXLocation, nextYLocation);

                drawNode(canvas, xLocation, yLocation, seriesPaint);

                drawGrid(canvas, xLocation);
                //
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

    protected abstract void drawGrid(Canvas canvas, float xLocation);
}
