package com.racine.renderer.axis;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.racine.components.Axis;
import com.racine.formatter.DefaultYAxisValueFormatter;
import com.racine.formatter.ValueFormatter;

/**
 * Created by sunrx on 2016/10/14.
 */
public class YAxisRenderer extends AxisRenderer<Float> {
    private ValueFormatter valueFormatter;

    public YAxisRenderer(RectF displayRectF, Axis axis) {
        super(displayRectF, axis);
        valueFormatter = new DefaultYAxisValueFormatter("万");
    }

    @Override
    public void drawAxisLine(Canvas canvas) {
        canvas.drawLine(displayRectF.right, displayRectF.top, displayRectF.right, displayRectF.bottom, getAxisPaint());
    }

    @Override
    public void drawAxisLabel(Canvas canvas) {
        getLabelPaint().setTextAlign(Paint.Align.LEFT);

        int ySize = axis.getSize();
        float _xLocation = displayRectF.right + axis.getGap();
        for (int i = 0; i < ySize; i++) {
            float _yLocation = getLocation((Float) axis.getValue(i))
                    + Math.abs(getLabelPaint().getFontMetrics().ascent) / 3;

            canvas.drawText(valueFormatter.getFormattedValue(axis.getValue(i)), _xLocation, _yLocation, getLabelPaint());
        }
        getLabelPaint().setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public float getLocation(Float value) {
        float yValueRange = axis.getMaxValue() - axis.getMinValue();

        float yLocationRange = displayRectF.bottom - displayRectF.top;

        float yLocation = yLocationRange * (axis.getMaxValue() - value) / yValueRange + displayRectF.top;

        return yLocation;
    }
}
