package com.racine.renderer.axis;

import android.graphics.Canvas;
import android.graphics.RectF;
import com.racine.components.Axis;
import com.racine.formatter.DefaultXAxisValueFormatter;
import com.racine.formatter.ValueFormatter;

/**
 * Created by sunrx on 2016/10/14.
 */
public class XAxisRenderer extends AxisRenderer<String> {
    private RectF unclipRectF;
    private ValueFormatter valueFormatter;

    public XAxisRenderer(RectF displayRectF, Axis axis,RectF unclipRectF) {
        super(displayRectF, axis);
        this.unclipRectF = unclipRectF;
        valueFormatter = new DefaultXAxisValueFormatter();
    }

    @Override
    public void drawAxisLine(Canvas canvas) {
        canvas.drawLine(displayRectF.left, displayRectF.bottom, displayRectF.right, displayRectF.bottom, getAxisPaint());
    }

    @Override
    public void drawAxisLabel(Canvas canvas) {
        int xSize = axis.size();
        float yLocation = displayRectF.bottom + axis.getLabelHeight() + axis.getGap();
        for (int i = 0; i < xSize; i++) {
            if (axis.isVisible(i)) {
                float xLocation = getLocation((String) axis.getValue(i));

                canvas.drawText(valueFormatter.getFormattedValue(axis.getValue(i)), xLocation, yLocation, getLabelPaint());
            }
        }
    }

    @Override
    public float getLocation(String value) {
        return unclipRectF.left + axis.getIndex(value) * axis.getStep();
    }
}
