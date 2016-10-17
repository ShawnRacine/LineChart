package com.racine.renderer.edge;

import android.content.Context;
import android.graphics.Canvas;
import com.racine.utils.ViewportHandler;

/**
 * Created by sunrx on 2016/10/14.
 */
public class LeftEdgeEffectRenderer extends EdgeEffectRenderer {

    public LeftEdgeEffectRenderer(Context context, ViewportHandler viewportHandler) {
        super(context, viewportHandler);
    }

    @Override
    public void drawEdgeEffect(Canvas canvas) {
        int c = canvas.save();

        if (!edgeEffect.isFinished()) {
            canvas.translate(viewportHandler.layerLeft(), viewportHandler.layerBottom());
            canvas.rotate(-90, 0, 0);
            edgeEffect.setSize((int) viewportHandler.layerHeight(), (int) viewportHandler.layerWidth());
            if (edgeEffect.draw(canvas)) {
                viewportHandler.postInvalidate();
            }
        }

        canvas.restoreToCount(c);
    }
}
