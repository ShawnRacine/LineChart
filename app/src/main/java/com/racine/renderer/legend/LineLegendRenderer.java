package com.racine.renderer.legend;

import android.graphics.Canvas;
import com.racine.utils.ViewportHandler;

/**
 * Created by Shawn Racine on 2016/10/17.
 */
public class LineLegendRenderer extends LegendRenderer {

    public LineLegendRenderer(ViewportHandler viewportHandler) {
        super(viewportHandler);
    }

    @Override
    public void drawLegend(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
//            canvas.drawRect(Legend.space+i*Legend.width,viewportHandler.getXAxisBottom+Legend.getPaddingTop,Legend.left+Legend.radius*2,Legend.top+Legend.radius*2,viewportHandler.getSeriesPaints().get(i));
        }
    }
}
