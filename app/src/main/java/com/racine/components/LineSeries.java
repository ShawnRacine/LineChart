package com.racine.components;

import java.util.Collections;

/**
 * Created by Shawn Racine on 2016/10/18.
 */
public class LineSeries extends Series<String, Float> {

    @Override
    public Float getMinYValue() {
        if (minYValue == null) {
            minYValue = Collections.min(yValues);
        }
        return super.getMinYValue();
    }

    @Override
    public Float getMaxYValue() {
        if (maxYValue == null) {
            maxYValue = Collections.max(yValues);
        }
        return super.getMaxYValue();
    }
}
