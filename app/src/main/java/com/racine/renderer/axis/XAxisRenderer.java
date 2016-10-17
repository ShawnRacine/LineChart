package com.racine.renderer.axis;

import android.graphics.Canvas;
import com.racine.components.Axis;
import com.racine.utils.ViewportHandler;
import com.racine.formatter.DefaultXAxisValueFormatter;
import com.racine.formatter.ValueFormatter;

/**
 * Created by sunrx on 2016/10/14.
 */
public class XAxisRenderer extends AxisRenderer<String> {
    private ValueFormatter valueFormatter;

    public XAxisRenderer(ViewportHandler viewportHandler, Axis axis) {
        super(viewportHandler, axis);
        valueFormatter = new DefaultXAxisValueFormatter();
    }

    @Override
    public void drawAxisLine(Canvas canvas) {
        canvas.drawLine(viewportHandler.layerLeft(), viewportHandler.layerBottom(),
                viewportHandler.layerRight(), viewportHandler.layerBottom(), getAxisPaint());
    }

    @Override
    public void drawAxisLabel(Canvas canvas) {
        int xSize = axis.size();
        float yLocation = viewportHandler.layerBottom() + axis.getLabelHeight() + axis.getGap();
        for (int i = 0; i < xSize; i++) {
            if (axis.isVisible(i)) {
                float xLocation = getLocation((String) axis.getValue(i));

                canvas.drawText(valueFormatter.getFormattedValue(axis.getValue(i)), xLocation, yLocation, getLabelPaint());
            }
        }
    }

    @Override
    public float getLocation(String value) {
        return viewportHandler.getWholeLeft() + axis.getIndex(value) * axis.getStep();
    }
}
