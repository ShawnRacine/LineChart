package com.racine.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sunrx on 2016/8/29.
 */
public class Series {
    private List<String> xValues;
    private List<Float> yValues;

    private float minYValue;
    private float maxYValue;

    public Series() {
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
    }

    public void add(String x, float y) {
        xValues.add(x);
        yValues.add(y);
    }

    public void remove(int index) {
        xValues.remove(index);
        yValues.remove(index);
    }

    public String getXValue(int index) {
        return xValues.get(index);
    }

    public float getYValue(int index) {
        return yValues.get(index);
    }

    public void setMinYValue(float minYValue) {
        this.minYValue = minYValue;
    }

    public float getMinYValue() {
        if (minYValue == 0) {
            minYValue = Collections.min(yValues);
        }
        return minYValue;
    }

    public void setMaxYValue(float maxYValue) {
        this.maxYValue = maxYValue;
    }

    public float getMaxYValue() {
        if (maxYValue == 0) {
            maxYValue = Collections.max(yValues);
        }
        return maxYValue;
    }

    public int size() {
        return yValues.size();
    }
}
