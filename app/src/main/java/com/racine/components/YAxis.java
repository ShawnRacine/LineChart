package com.racine.components;

/**
 * Created by sunrx on 2016/9/2.
 */
public class YAxis extends Axis<Float> {
    private int DEFAULT_SIZE = 6;

    public YAxis() {
        super();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        if (size == 0) {
            size = DEFAULT_SIZE;
        }
        return size;
    }
}
