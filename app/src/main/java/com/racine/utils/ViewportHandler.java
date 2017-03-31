package com.racine.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import com.racine.components.*;
import com.racine.exception.UnassignedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shawn Racine on 2016/10/17.
 */
public class ViewportHandler {
    private RectF contentRectF;
    private RectF clipRectF;
    private RectF unclipRectF;

    private View view;

    public Axis xAxis;
    public Axis yAxis;

    private Legend legend;

    private float width;
    private float height;

    private float leftOffset;
    private float topOffset;
    private float rightOffset;
    private float bottomOffset;

    private List<Paint> seriesPaints;

    public ViewportHandler(View view) {
        this.view = view;

        xAxis = new XAxis();
        yAxis = new YAxis();
        legend = new Legend();

        contentRectF = new RectF();
        clipRectF = new RectF();
        unclipRectF = new RectF();

        seriesPaints = new ArrayList<>();
    }

    public void setDimens(float width, float height) {
        this.width = width;
        this.height = height;

        restrainViewport(leftOffset, topOffset, rightOffset, bottomOffset);
    }

    public void restrainViewport(float leftOffset, float topOffset, float rightOffset, float bottomOffset) {
        this.leftOffset = leftOffset;
        this.topOffset = topOffset;
        this.rightOffset = rightOffset;
        this.bottomOffset = bottomOffset;

        if (width == 0 || height == 0) {
            throw new UnassignedException("width or height unassigned! Need to ViewportHandler.setDimens() first.");
        }
        contentRectF.set(leftOffset, topOffset, width - rightOffset, height - bottomOffset);

        clipRectF.set(contentRectF.left + xAxis.getLabelWidth() / 2,
                contentRectF.top + yAxis.getLabelHeight(),
                contentRectF.right - yAxis.getLabelWidth() - yAxis.getGap() - legend.getHorOccupied(),
                contentRectF.bottom - xAxis.getLabelHeight() - xAxis.getGap() - legend.getVerOccupied());

        unclipRectF.set(clipRectF.left, clipRectF.top, clipRectF.left + (xAxis.size() - 1) * xAxis.getStep(), clipRectF.bottom);

        //initialize
        xAxis.setLocationRange(unclipRectF);
        yAxis.setLocationRange(unclipRectF);
    }

    public void assembleAxesValues(List<Series> seriesList) {
        for (int i = 0; i < seriesList.size(); i++) {
            LineSeries series = (LineSeries) seriesList.get(i);
            for (int j = 0; j < series.size(); j++) {
                xAxis.addValue(j, series.getXValue(j));
            }
            if (i == 0) {
                yAxis.setMinValue(series.getMinYValue());
                yAxis.setMaxValue(series.getMaxYValue());
            } else {
                if (yAxis.getMinValue() > series.getMinYValue()) {
                    yAxis.setMinValue(series.getMinYValue());
                }
                if (yAxis.getMaxValue() < series.getMaxYValue()) {
                    yAxis.setMaxValue(series.getMaxYValue());
                }
            }
            assembleYValues(yAxis);
        }
    }

    // multiple of 10.
    private int precisionFormat = 10;

    private void assembleYValues(Axis yAxis) {
        // adjust minYValue and maxYValue.
        float min = (float) Math.floor(yAxis.getMinValue());
        float max = (float) Math.ceil(yAxis.getMaxValue());

        max = max + (precisionFormat - max % precisionFormat);

        min = min - min % precisionFormat;
        if (min < 0) {
            min = 0;
        }

        float range = max - min;
        float precision = (yAxis.getLabelCount() - 1) * precisionFormat;
        float mod = range % precision;
        float expansion = precision - mod;

        float top = max + expansion / 2;
        float bottom = min - expansion / 2;
        // end of adjustment.

        yAxis.setMinValue(bottom);
        yAxis.setMaxValue(top);

        //
        float yRange = yAxis.getMaxValue() - yAxis.getMinValue();
        float unit = yRange / (yAxis.getLabelCount() - 1);
        for (int i = 0; i < yAxis.getLabelCount(); i++) {
            yAxis.addValue(i, yAxis.getMinValue() + i * unit);
        }
    }

    public void setSeriesPaints(List<Paint> seriesPaints) {
        this.seriesPaints = seriesPaints;
    }

    public List<Paint> getSeriesPaints() {
        if (seriesPaints.size() == 0) {
            Paint seriesPaint = new Paint();
            seriesPaint.setColor(Color.BLUE);
            seriesPaint.setStyle(Paint.Style.STROKE);
            seriesPaint.setStrokeWidth(5f);
            seriesPaint.setAntiAlias(true);

            seriesPaints.add(seriesPaint);
        }
        return seriesPaints;
    }

    public void postInvalidate() {
        view.postInvalidate();
    }

    public RectF layerRect() {
        return clipRectF;
    }

    public float layerLeft() {
        return clipRectF.left;
    }

    public float layerTop() {
        return clipRectF.top;
    }

    public float layerRight() {
        return clipRectF.right;
    }

    public float layerBottom() {
        return clipRectF.bottom;
    }

    public float layerWidth() {
        return clipRectF.width();
    }

    public float layerHeight() {
        return clipRectF.height();
    }

    public void moveUnclipRectLeft(float left) {
        unclipRectF.left = left;
        unclipRectF.right = unclipRectF.left + (xAxis.size() - 1) * xAxis.getStep();
        xAxis.setLocationRange(unclipRectF);
    }

    public void moveUnclipRectRight(float right) {
        unclipRectF.right = right;
        unclipRectF.left = clipRectF.right - (xAxis.size() - 1) * xAxis.getStep();
        xAxis.setLocationRange(unclipRectF);
    }

    public float getXLocation(float offsetOrigin) {
        return unclipRectF.left + offsetOrigin;
    }

    public float getYLocation(Float value,float maxValue,float minValue) {
        float yValueRange = maxValue - minValue;

        float yLocationRange = clipRectF.bottom - clipRectF.top;

        float yLocation = yLocationRange * (maxValue - value) / yValueRange + unclipRectF.top;

        return yLocation;
    }

    public int scrollStartX() {
        return (int) unclipRectF.left;
    }

    public int scrollDistanceX() {
        return (int) (clipRectF.right - unclipRectF.right);
    }

    public float scrollerCurrentX(float distanceX) {
        return unclipRectF.left - distanceX;
    }

    public boolean isLeftEdge() {
        return unclipRectF.left >= clipRectF.left;
    }

    public boolean isRightEdge() {
        return unclipRectF.right <= clipRectF.right;
    }

    public int flingStartX() {
        return (int) unclipRectF.left;
    }

    public int flingMinX() {
        return (int) (clipRectF.right - (xAxis.size() - 1) * xAxis.getStep());
    }

    public int flingMaxX() {
        return (int) clipRectF.left;
    }

    public void setXAxisVisible(int index) {
        xAxis.setVisible(index);
    }

    public void setXAxisInVisible(int index) {
        xAxis.setInVisible(index);
    }

}
