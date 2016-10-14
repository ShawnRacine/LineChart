package com.racine.renderer.edge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by sunrx on 2016/10/14.
 */
public class LeftEdgeEffectRenderer extends EdgeEffectRenderer {

    public LeftEdgeEffectRenderer(Context context, View view, RectF displayRectF) {
        super(context, view, displayRectF);
    }

    @Override
    public void drawEdgeEffect(Canvas canvas) {
        int c = canvas.save();

        if (!edgeEffect.isFinished()) {
            canvas.translate(displayRectF.left, displayRectF.bottom);
            canvas.rotate(-90, 0, 0);
            edgeEffect.setSize((int) displayRectF.height(), (int) displayRectF.width());
            if (edgeEffect.draw(canvas)) {
                view.postInvalidate();
            }
        }

        canvas.restoreToCount(c);
    }
}
