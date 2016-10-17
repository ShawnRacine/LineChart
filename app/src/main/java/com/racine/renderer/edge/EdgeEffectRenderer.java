package com.racine.renderer.edge;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.widget.EdgeEffectCompat;
import com.racine.utils.ViewportHandler;

/**
 * Created by sunrx on 2016/10/14.
 */
public abstract class EdgeEffectRenderer {
    protected ViewportHandler viewportHandler;
    protected EdgeEffectCompat edgeEffect;

    public EdgeEffectRenderer(Context context, ViewportHandler viewportHandler) {
        this.viewportHandler = viewportHandler;
        this.edgeEffect = new EdgeEffectCompat(context);
    }

    public abstract void drawEdgeEffect(Canvas canvas);

    public boolean onPull(float distance) {
        return edgeEffect.onPull(distance / viewportHandler.layerWidth(), 0.5f);
    }

    public boolean onRelease() {
        return edgeEffect.onRelease();
    }
}
