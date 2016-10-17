package com.racine;

import android.content.Context;
import android.util.AttributeSet;
import com.racine.components.Axis;

/**
 * Created by sunrx on 2016/10/11.
 */
public class LineGraph extends GraphView {

    // multiple of 10.
    private int precisionFormat = 10;

    public LineGraph(Context context) {
        super(context);
    }

    public LineGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void assembleYValues(Axis yAxis) {
        // adjust minYValue and maxYValue.
        float min = (float) Math.floor(yAxis.getMinValue());
        float max = (float) Math.ceil(yAxis.getMaxValue());

        max = max + (precisionFormat - max % precisionFormat);

        min = min - min % precisionFormat;
        if (min < 0) {
            min = 0;
        }

        float range = max - min;
        float precision = (yAxis.getSize() - 1) * precisionFormat;
        float mod = range % precision;
        float expansion = precision - mod;

        float top = max + expansion / 2;
        float bottom = min - expansion / 2;
        // end of adjustment.

        yAxis.setMinValue(bottom);
        yAxis.setMaxValue(top);

        //
        float yRange = yAxis.getMaxValue() - yAxis.getMinValue();
        float unit = yRange / (yAxis.getSize() - 1);
        for (int i = 0; i < yAxis.getSize(); i++) {
            yAxis.addValue(i, yAxis.getMinValue() + i * unit);
        }
    }

}
