package com.racine.components;

import android.graphics.RectF;

/**
 * Created by sunrx on 2016/9/2.
 */
public class YAxis extends Axis<Float> {
    private float layerTop;
    private float layerBottom;

    public YAxis() {
        super();
    }

    @Override
    public float getLocation(Float value) {
        float yValueRange = getMaxValue() - getMinValue();

        float yLocationRange = layerBottom - layerTop;

        float yLocation = yLocationRange * (getMaxValue() - value) / yValueRange + layerTop;

        return yLocation;
    }

    @Override
    public void setLocationRange(RectF ranges) {
        this.layerTop = ranges.top;
        this.layerBottom = ranges.bottom;
    }
}
