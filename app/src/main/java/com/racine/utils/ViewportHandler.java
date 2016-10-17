package com.racine.utils;

import android.graphics.RectF;
import android.view.View;
import com.racine.components.Axis;
import com.racine.exception.UnassignedException;

/**
 * Created by Shawn Racine on 2016/10/17.
 */
public class ViewportHandler {
    private RectF contentRectF;
    private RectF clipRectF;
    private RectF unclipRectF;

    private View view;

    private Axis xAxis;
    private Axis yAxis;

    private float width;
    private float height;

    private float leftOffset;
    private float topOffset;
    private float rightOffset;
    private float bottomOffset;

    public ViewportHandler(View view, Axis xAxis, Axis yAxis) {
        this.view = view;
        this.xAxis = xAxis;
        this.yAxis = yAxis;

        contentRectF = new RectF();
        clipRectF = new RectF();
        unclipRectF = new RectF();
    }

    public void setDimens(float width, float height) {
        this.width = width;
        this.height = height;

        restrainViewport(leftOffset, topOffset, rightOffset, bottomOffset);
    }

    public void restrainViewport(float leftOffset, float topOffset, float rightOffset, float bottomOffset) {
        this.leftOffset = leftOffset;
        this.topOffset = topOffset;
        this.rightOffset = rightOffset;
        this.bottomOffset = bottomOffset;

        if (width == 0 || height == 0) {
            throw new UnassignedException("width or height unassigned! Need to ViewportHandler.setDimens() first.");
        }
        contentRectF.set(leftOffset, topOffset, width - rightOffset, height - bottomOffset);

        clipRectF.set(contentRectF.left + xAxis.getLabelWidth() / 2,
                contentRectF.top + yAxis.getLabelHeight(),
                contentRectF.right - yAxis.getLabelWidth() - yAxis.getGap(),
                contentRectF.bottom - xAxis.getLabelHeight() - xAxis.getGap());

        unclipRectF.set(clipRectF.left, clipRectF.top, clipRectF.left + (xAxis.getSize() - 1) * xAxis.getStep(), clipRectF.bottom);
    }

    public void postInvalidate() {
        view.postInvalidate();
    }

    public RectF layerRect() {
        return clipRectF;
    }

    public float layerLeft() {
        return clipRectF.left;
    }

    public float layerTop() {
        return clipRectF.top;
    }

    public float layerRight() {
        return clipRectF.right;
    }

    public float layerBottom() {
        return clipRectF.bottom;
    }

    public float layerWidth() {
        return clipRectF.width();
    }

    public float layerHeight() {
        return clipRectF.height();
    }

    public void setWholeLeft(float left) {
        unclipRectF.left = left;
        unclipRectF.right = unclipRectF.left + (xAxis.getSize() - 1) * xAxis.getStep();
    }

    public float getWholeLeft() {
        return unclipRectF.left;
    }

    public void setWholeRight(float right) {
        unclipRectF.right = right;
        unclipRectF.left = clipRectF.right - (xAxis.getSize() - 1) * xAxis.getStep();
    }

    public int scrollStartX() {
        return (int) unclipRectF.left;
    }

    public int scrollDistanceX() {
        return (int) (clipRectF.right - unclipRectF.right);
    }

    public float scrollerCurrentX(float distanceX) {
        return unclipRectF.left - distanceX;
    }

    public boolean isLeftEdge() {
        return unclipRectF.left >= clipRectF.left;
    }

    public boolean isRightEdge() {
        return unclipRectF.right <= clipRectF.right;
    }

    public int flingStartX() {
        return (int) unclipRectF.left;
    }

    public int flingMinX() {
        return (int) (clipRectF.right - (xAxis.getSize() - 1) * xAxis.getStep());
    }

    public int flingMaxX() {
        return (int) clipRectF.left;
    }

    public void setXAxisVisible(int index) {
        xAxis.setVisible(index);
    }

    public void setXAxisInVisible(int index) {
        xAxis.setInVisible(index);
    }
}
