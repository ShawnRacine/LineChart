package com.racine.renderer.axis;

import android.graphics.Canvas;
import android.graphics.Paint;
import com.racine.components.Axis;
import com.racine.formatter.DefaultYAxisValueFormatter;
import com.racine.formatter.ValueFormatter;
import com.racine.utils.ViewportHandler;

/**
 * Created by sunrx on 2016/10/14.
 */
public class YAxisRenderer extends AxisRenderer<Float> {
    private ValueFormatter valueFormatter;

    public YAxisRenderer(ViewportHandler viewportHandler, Axis axis) {
        super(viewportHandler, axis);
        valueFormatter = new DefaultYAxisValueFormatter("万");
    }

    @Override
    public void drawAxisLine(Canvas canvas) {
        canvas.drawLine(viewportHandler.layerRight(), viewportHandler.layerTop(),
                viewportHandler.layerRight(), viewportHandler.layerBottom(), getAxisPaint());
    }

    @Override
    public void drawAxisLabel(Canvas canvas) {
        getLabelPaint().setTextAlign(Paint.Align.LEFT);

        int ySize = axis.getSize();
        float _xLocation = viewportHandler.layerRight() + axis.getGap();
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

        float yLocationRange = viewportHandler.layerBottom() - viewportHandler.layerTop();

        float yLocation = yLocationRange * (axis.getMaxValue() - value) / yValueRange + viewportHandler.layerTop();

        return yLocation;
    }
}
