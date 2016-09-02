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

    public void setScaleNum(int scaleNum) {
        this.scaleNum = scaleNum;

        step = Math.abs(contentRect.bottom - contentRect.top) / (scaleNum - 1);
    }

    public int getScaleNum() {
        if (scaleNum == 0) {
            scaleNum = (int) (Math.abs(contentRect.bottom - contentRect.top) / getStep()) + 1;
        }
        return scaleNum;
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

    public void setCurrentMinValue(float currentMinYValue) {
        this.currentMinValue = currentMinYValue;
    }

    public float getCurrentMinValue() {
        if (currentMinValue == 0) {
            currentMinValue = getMinValue();
        }
        return currentMinValue;
    }

    public void setCurrentMaxValue(float currentMaxYValue) {
        this.currentMaxValue = currentMaxYValue;
    }

    public float getCurrentMaxValue() {
        if (currentMaxValue == 0) {
            currentMaxValue = getMaxValue();
        }
        return currentMaxValue;
    }

    public float getCurrentValueStep() {
        currentValueStep = (getCurrentMaxValue() - getCurrentMinValue()) / (getScaleNum() - 1);
        return currentValueStep;
    }

    public float getCurrentValue(int index) {
        currentValue = getCurrentMinValue() + index * getCurrentValueStep();
        return currentValue;
    }
}
