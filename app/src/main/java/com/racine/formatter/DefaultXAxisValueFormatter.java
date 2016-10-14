package com.racine.formatter;

import java.text.DecimalFormat;

/**
 * Created by sunrx on 2016/10/14.
 */
public class DefaultXAxisValueFormatter implements ValueFormatter<String> {

    public DefaultXAxisValueFormatter() {
    }

    @Override
    public String getFormattedValue(String value) {
        return value;
    }
}
