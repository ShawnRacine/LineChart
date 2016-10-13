package com.racine.components;

import com.racine.interfaces.Axis;

import java.util.Collections;

/**
 * Created by sunrx on 2016/9/2.
 */
public class YAxis extends Axis<Float> {

    private float minValue;
    private float maxValue;

    private float currentMinValue;
    private float currentMaxValue;

    private float currentValueStep;

    private float currentValue;

    public YAxis() {
        super();
    }

    public float getStep() {
        if (step == 0) {
            step = labelHeight * 3;
        }
        return step;
    }

    public void setSize(int size) {
        this.size = size;

        step = Math.abs(contentRect.bottom - contentRect.top) / (size - 1);
    }

    public int getSize() {
        if (size == 0) {
            size = (int) (Math.abs(contentRect.bottom - contentRect.top) / getStep()) + 1;
        }
        return size;
    }

    public void setMinValue(float minYValue) {
        this.minValue = minYValue;
    }

    public float getMinValue() {
        if (minValue == 0) {
            minValue = Collections.min(values);
        }
        return minValue;
    }

    public void setMaxValue(float maxYValue) {
        this.maxValue = maxYValue;
    }

    public float getMaxValue() {
        if (maxValue == 0) {
            maxValue = Collections.max(values);
        }
        return maxValue;
    }

    public void setDisplayMinValue(float currentMinYValue) {
        this.currentMinValue = currentMinYValue;
    }

    public float getDisplayMinValue() {
        if (currentMinValue == 0) {
            currentMinValue = getMinValue();
        }
        return currentMinValue;
    }

    public void setDisplayMaxValue(float currentMaxYValue) {
        this.currentMaxValue = currentMaxYValue;
    }

    public float getDisplayMaxValue() {
        if (currentMaxValue == 0) {
            currentMaxValue = getMaxValue();
        }
        return currentMaxValue;
    }

    public float getDisplayValueUnit() {
        currentValueStep = (getDisplayMaxValue() - getDisplayMinValue()) / (getSize() - 1);
        return currentValueStep;
    }

    public float getDisplayValue(int index) {
        currentValue = getDisplayMinValue() + index * getDisplayValueUnit();
        return currentValue;
    }
}
