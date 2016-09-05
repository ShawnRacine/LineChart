package com.racine.interfaces;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunrx on 2016/8/30.
 */
public abstract class Axis<T> {
    protected Rect contentRect;

    protected Paint paint;

    protected float labelWidth;
    protected float labelHeight;

    protected float gap;

    protected int stopsNum;

    protected float step;

    protected List<T> values;

    public Axis() {
        paint = new Paint();
        paint.setTextSize(26);

        labelWidth = paint.measureText("0000万");
        labelHeight = Math.abs(paint.getFontMetrics().top) + paint.getFontMetrics().bottom;

        gap = 5;

        values = new ArrayList<>();
    }

    public void setContentRect(Rect contentRect) {
        this.contentRect = contentRect;
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

    public void setStopsNum(int stopsNum) {
        this.stopsNum = stopsNum;
    }

    public int getStopsNum() {
        return stopsNum;
    }

    public void addValue(int index, T value) {
        values.add(index, value);
    }

    public T getValue(int index) {
        return values.get(index);
    }

    public List<T> getValues(){
        return values;
    }

    public int size() {
        return values.size();
    }
}
