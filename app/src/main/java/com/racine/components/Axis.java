package com.racine.components;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sunrx on 2016/8/30.
 */
public abstract class Axis<T> {
    protected Paint paint;

    protected float labelWidth;
    protected float labelHeight;

    protected float gap;

    protected int size;

    protected float step;

    private float minValue;
    private float maxValue;

    protected List<T> values;

    private List<Boolean> visibleList;

    public Axis() {
        paint = new Paint();
        paint.setTextSize(26);

        labelWidth = paint.measureText("0000万");
        labelHeight = Math.abs(paint.getFontMetrics().top) + paint.getFontMetrics().bottom;

        gap = 10;

        values = new ArrayList<>();
        visibleList = new ArrayList<>();
    }

    public float getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(float labelWidth) {
        this.labelWidth = labelWidth;
    }

    public float getLabelHeight() {
        return labelHeight;
    }

    public void setLabelHeight(float labelHeight) {
        this.labelHeight = labelHeight;
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    public void setStep(float step) {
        this.step = step;
    }

    public float getStep() {
        return step;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void addValue(int index, T value) {
        values.add(index, value);
    }

    public T getValue(int index) {
        return values.get(index);
    }

    public void setMinValue(float minYValue) {
        this.minValue = minYValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMaxValue(float maxYValue) {
        this.maxValue = maxYValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public List<T> getValues() {
        return values;
    }

    public int size() {
        return values.size();
    }

    public int getIndex(String value) {
        return values.indexOf(value);
    }

    public boolean isVisible(int index) {
        if (visibleList.size() > index) {
            return visibleList.get(index);
        } else {
            return false;
        }
    }

    public void setVisible(int index) {
        visibleList.add(index, true);
    }

    public void setInVisible(int index) {
        visibleList.add(index, false);
    }
}
