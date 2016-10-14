package com.racine.renderer.edge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.View;
import android.widget.EdgeEffect;

/**
 * Created by sunrx on 2016/10/14.
 */
public abstract class EdgeEffectRenderer {
    protected View view;
    protected EdgeEffectCompat edgeEffect;
    protected RectF displayRectF;

    public EdgeEffectRenderer(Context context, View view, RectF displayRectF) {
        this.view = view;
        this.edgeEffect = new EdgeEffectCompat(context);
        this.displayRectF = displayRectF;
    }

    public abstract void drawEdgeEffect(Canvas canvas);

    public boolean onPull(float distance) {
        return edgeEffect.onPull(distance / displayRectF.width(), 0.5f);
    }

    public boolean onRelease() {
        return edgeEffect.onRelease();
    }
}
