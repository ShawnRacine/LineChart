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
    private Axis yAxis;

    public YAxisRenderer(ViewportHandler viewportHandler) {
        super(viewportHandler);
        valueFormatter = new DefaultYAxisValueFormatter("万");
        yAxis = viewportHandler.yAxis;
    }

    @Override
    public void drawAxisLine(Canvas canvas) {
        canvas.drawLine(viewportHandler.layerRight(), viewportHandler.layerTop(),
                viewportHandler.layerRight(), viewportHandler.layerBottom(), getAxisPaint());
    }

    @Override
    public void drawAxisLabel(Canvas canvas) {
        getLabelPaint().setTextAlign(Paint.Align.LEFT);

        int yAxisLabelCount = yAxis.getLabelCount();
        float _xLocation = viewportHandler.layerRight() + yAxis.getGap();
        for (int i = 0; i < yAxisLabelCount; i++) {
            float yLocation = yAxis.getLocation(yAxis.getValue(i));
            float _yLocation = yLocation + Math.abs(getLabelPaint().getFontMetrics().ascent) / 3;

            canvas.drawText(valueFormatter.getFormattedValue(yAxis.getValue(i)), _xLocation, _yLocation, getLabelPaint());
            //Draw grid lines by default.
            if (drawGridLines) {
                canvas.drawLine(viewportHandler.layerRight(), yLocation, viewportHandler.layerLeft(), yLocation, getGridPaint());
            }
        }
        getLabelPaint().setTextAlign(Paint.Align.CENTER);
    }
}
