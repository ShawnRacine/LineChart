package com.racine.components;

import android.graphics.RectF;

/**
 * Created by sunrx on 2016/9/2.
 */
public class XAxis extends Axis<String> {
    private float unclipLeft;

    public XAxis() {
        super();
        setMode(Mode.FIXED_STEP);
    }

    @Override
    public float getStep() {
        if (step == 0) {
            step = labelWidth * 2;
        }
        return step;
    }

    @Override
    public float getLocation(String value) {
        return unclipLeft + getIndex(value) * getStep();
    }

    @Override
    public void setLocationRange(RectF ranges) {
        this.unclipLeft = ranges.left;
    }
}
