package com.racine.formatter;

import java.text.DecimalFormat;

/**
 * Created by sunrx on 2016/10/14.
 */
public class DefaultYAxisValueFormatter implements ValueFormatter<Float> {
    private String unit;

    public DefaultYAxisValueFormatter(String unit) {
        this.unit = unit;
    }

    @Override
    public String getFormattedValue(Float value) {
        DecimalFormat df = new DecimalFormat("0");
        return df.format(value) + unit;
    }
}
