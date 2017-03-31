package com.racine.renderer.axis;

import android.graphics.Canvas;
import com.racine.components.Axis;
import com.racine.formatter.DefaultXAxisValueFormatter;
import com.racine.formatter.ValueFormatter;
import com.racine.utils.ViewportHandler;

/**
 * Created by sunrx on 2016/10/14.
 */
public class XAxisRenderer extends AxisRenderer<String> {
    private ValueFormatter valueFormatter;
    private Axis xAxis;

    public XAxisRenderer(ViewportHandler viewportHandler) {
        super(viewportHandler);
        valueFormatter = new DefaultXAxisValueFormatter();
        xAxis = viewportHandler.xAxis;
    }

    @Override
    public void drawAxisLine(Canvas canvas) {
        canvas.drawLine(viewportHandler.layerLeft(), viewportHandler.layerBottom(),
                viewportHandler.layerRight(), viewportHandler.layerBottom(), getAxisPaint());
    }

    @Override
    public void drawAxisLabel(Canvas canvas) {
        int xSize = xAxis.size();
        float yLocation = viewportHandler.layerBottom() + xAxis.getLabelHeight() + xAxis.getGap();
        for (int i = 0; i < xSize; i++) {
            if (xAxis.isVisible(i)) {
                float xLocation = xAxis.getLocation(xAxis.getValue(i));

                canvas.drawText(valueFormatter.getFormattedValue(xAxis.getValue(i)), xLocation, yLocation, getLabelPaint());
                //Draw grid lines by default.
                if (drawGridLines) {
                    canvas.drawLine(xLocation, viewportHandler.layerTop(), xLocation, viewportHandler.layerBottom(), getGridPaint());
                }
            }
        }
    }
}
