package com.racine.renderer.legend;

import android.graphics.Canvas;
import com.racine.utils.ViewportHandler;

/**
 * Created by Shawn Racine on 2016/10/17.
 */
public abstract class LegendRenderer {
    private static final int POSITION_LEFT = 1;
    private static final int POSITION_TOP = 2;
    private static final int POSITION_RIGHT = 3;
    private static final int POSITION_BOTTOM = 4;

    protected ViewportHandler viewportHandler;

    public LegendRenderer(ViewportHandler viewportHandler) {
        this.viewportHandler = viewportHandler;
    }

    public abstract void drawLegend(Canvas canvas);
}
