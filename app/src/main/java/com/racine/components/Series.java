package com.racine.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunrx on 2016/8/29.
 */
public abstract class Series<T,V> {
    protected List<T> xValues;
    protected List<V> yValues;

    protected V minYValue;
    protected V maxYValue;

    public Series() {
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
    }

    public void add(T x, V y) {
        xValues.add(x);
        yValues.add(y);
    }

    public void remove(int index) {
        xValues.remove(index);
        yValues.remove(index);
    }

    public T getXValue(int index) {
        return xValues.get(index);
    }

    public V getYValue(int index) {
        return yValues.get(index);
    }

    public List<T> getXValues(){
        return xValues;
    }

    public List<V> getYValues(){
        return yValues;
    }

    public void setMinYValue(V minYValue) {
        this.minYValue = minYValue;
    }

    public V getMinYValue() {
        return minYValue;
    }

    public void setMaxYValue(V maxYValue) {
        this.maxYValue = maxYValue;
    }

    public V getMaxYValue() {
        return maxYValue;
    }

    public int size() {
        return yValues.size();
    }
}
