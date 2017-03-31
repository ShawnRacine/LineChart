package com.racine.components;

import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunrx on 2016/8/30.
 */
public abstract class Axis<T> {

    private int DEFAULT_LABEL_COUNT = 6;

    protected enum Mode {
        FIXED_STEP, FIXED_COUNT
    }

    protected Mode mode;

    protected Paint paint;

    protected float labelWidth;
    protected float labelHeight;

    protected float gap;

    protected int labelCount;

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

    protected void setMode(Mode mode) {
        this.mode = mode;
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

    /**
     * Fix label count.
     *
     * @return
     */
    public int getLabelCount() {
        if (labelCount == 0) {
            labelCount = DEFAULT_LABEL_COUNT;
        }
        return labelCount;
    }

    /**
     * Fix stride.
     *
     * @return
     */
    public float getStep() {
        return step;
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

    /**
     * get location by value.
     *
     * @param value value from datasource.
     * @return location.
     */
    public abstract float getLocation(T value);

    public abstract void setLocationRange(RectF ranges);
}
