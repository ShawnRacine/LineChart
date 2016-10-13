package com.racine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;

import com.racine.exception.InconsistentSizeException;
import com.racine.linechart.R;
import com.racine.components.XAxis;
import com.racine.components.YAxis;
import com.racine.components.Series;
import com.racine.datas.DataRes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunrx on 2016/8/23.
 */
public abstract class GraphView extends View {
    private static final String TAG = "LineC";
    private static final float SMOOTHNESS = 0.3f; // the higher the smoother, but don't go over 0.5

    private int width, height;

    protected XAxis xAxis = new XAxis();
    protected YAxis yAxis = new YAxis();

    private Paint axisPaint = new Paint();
    private Paint labelPaint = new Paint();
    private List<Paint> seriesPaintList = new ArrayList<>();

    private RectF contentRectF = new RectF();
    private RectF unclipRectF = new RectF();
    private RectF clipRectF = new RectF();

    private GestureDetector mGestureDetector;

    private OverScroller mScroller;

    private EdgeEffectCompat mLeftEdgeEffect;
    private EdgeEffectCompat mRightEdgeEffect;

    private List<Series> seriesList;

    // multiple of 10.
    private int precisionFormat = 10;

    public GraphView(Context context) {
        this(context, null);
    }

    public GraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        seriesList = new ArrayList<>();

        mGestureDetector = new GestureDetector(context, onGestureListener);
        mScroller = new OverScroller(context, new DecelerateInterpolator());
        mLeftEdgeEffect = new EdgeEffectCompat(context);
        mRightEdgeEffect = new EdgeEffectCompat(context);

        renderAxesPaint(axisPaint);

        renderLabelPaint(labelPaint);

        renderSeriesPaint(seriesPaintList);
    }

    protected void renderAxesPaint(Paint axisPaint) {
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setStrokeWidth(1f);
    }

    protected void renderLabelPaint(Paint labelPaint) {
        labelPaint.setColor(Color.BLACK);
        labelPaint.setTextSize(26);
        labelPaint.setTextAlign(Paint.Align.CENTER);
    }

    protected void renderClipPaint(Paint clipPaint) {
        clipPaint.setColor(Color.TRANSPARENT);
        clipPaint.setStyle(Paint.Style.STROKE);
    }

    protected void renderSeriesPaint(List<Paint> seriesPaintList) {
        Paint seriesPaint = new Paint();
        seriesPaint.setColor(Color.BLUE);
        seriesPaint.setStyle(Paint.Style.STROKE);
        seriesPaint.setStrokeWidth(5f);
        seriesPaint.setAntiAlias(true);

        seriesPaintList.add(seriesPaint);
    }

    public void addSeries(Series series) {
        seriesList.add(series);

        if (seriesList.size() == 1) {
            for (int i = 0; i < series.size(); i++) {
                xAxis.addValue(i, series.getXValue(i));
            }
            yAxis.setMinValue(series.getMinYValue());
            yAxis.setMaxValue(series.getMaxYValue());
        } else {
            if (yAxis.getMinValue() > series.getMinYValue()) {
                yAxis.setMinValue(series.getMinYValue());
            }
            if (yAxis.getMaxValue() < series.getMaxYValue()) {
                yAxis.setMaxValue(series.getMaxYValue());
            }
            adjustYExtremun(yAxis);
            refreshCurrentViewport();
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        initContentRect();
        initAxes();

        adjustYExtremun(yAxis);

        refreshCurrentViewport();
    }

    private void initContentRect() {
        contentRectF.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());

        clipRectF.set(contentRectF.left + xAxis.getLabelWidth() / 2,
                contentRectF.top + yAxis.getLabelHeight(),
                contentRectF.right - yAxis.getLabelWidth() - yAxis.getGap(),
                contentRectF.bottom - xAxis.getLabelHeight() - xAxis.getGap());

        unclipRectF.set(clipRectF.left, clipRectF.top, clipRectF.left + (xAxis.getSize() - 1) * xAxis.getStep(), clipRectF.bottom);
    }

    protected void initAxes() {
        //need to abolish setContentRect.
        xAxis.setContentRect(clipRectF);
        yAxis.setContentRect(clipRectF);
    }

    /**
     * reset minYValue and maxYValue.
     *
     * @param yAxis the object which will be reset.
     */
    protected void adjustYExtremun(YAxis yAxis) {
        // adjust minYValue and maxYValue.
        float minY = (float) Math.floor(yAxis.getMinValue());
        float maxY = (float) Math.ceil(yAxis.getMaxValue());

        maxY = maxY + (precisionFormat - maxY % precisionFormat);

        minY = minY - minY % precisionFormat;
        if (minY < 0) {
            minY = 0;
        }

        float range = maxY - minY;
        float precision = (yAxis.getSize() - 1) * precisionFormat;
        float mod = range % precision;
        float expansion = precision - mod;

        float top = maxY + expansion / 2;
        float bottom = minY - expansion / 2;
        // end of adjustment.
        yAxis.setMinValue(bottom);
        yAxis.setMaxValue(top);

        yAxis.setMinValue(Math.min(yAxis.getMinValue(), yAxis.getMinValue()));
        yAxis.setMaxValue(Math.max(yAxis.getMaxValue(), yAxis.getMaxValue()));
    }

    /**
     * It should be invoke when the range of xAxis or yAxis had been changed.
     */
    private void refreshCurrentViewport() {
        //As the change of unclipRectF.left or unclipRectF.right,
        // the step of xAxis need to change.
//         series.size() * xAxis.getStep();
//        float xStep = (unclipRectF.right - unclipRectF.left) / (xAxis.size() - 1);
//        xAxis.setStep(xStep);

        //As the change of unclipRectF.top or unclipRectF.bottom,
        // the currentMaxYValue or currentMinYValue of currentViewport need to change.
        float displayMinYValue = yAxis.getMinValue() +
                (unclipRectF.bottom - clipRectF.bottom) / (unclipRectF.bottom - unclipRectF.top)
                        * (yAxis.getMaxValue() - yAxis.getMinValue());
        float displayMaxYValue = yAxis.getMaxValue() -
                (clipRectF.top - unclipRectF.top) / (unclipRectF.bottom - unclipRectF.top)
                        * (yAxis.getMaxValue() - yAxis.getMinValue());
        yAxis.setDisplayMinValue(displayMinYValue);
        yAxis.setDisplayMaxValue(displayMaxYValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minChartSize = getResources().getDimensionPixelSize(R.dimen.min_chart_size);

        setMinimumWidth(minChartSize);
        setMinimumHeight(minChartSize);
        //when set wrapcontent,
        int width = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int height = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    public void start(int duration) {
        mScroller.startScroll((int) unclipRectF.left, 0, (int) -(unclipRectF.right - clipRectF.right), 0, duration);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int c = canvas.save();
        drawEdgeEffect(canvas);
        canvas.restoreToCount(c);

        drawDataSeriesUnClipped(canvas, seriesList, seriesPaintList);

        //get visibility of xAxis after drawDataSeriesUnClipped.
        drawAxes(canvas);
    }

    private void drawAxes(Canvas canvas) {
        //draw X-Axis
        canvas.drawLine(clipRectF.left, clipRectF.bottom, clipRectF.right, clipRectF.bottom, axisPaint);
        //draw Y-Axis
        canvas.drawLine(clipRectF.right, clipRectF.top, clipRectF.right, clipRectF.bottom, axisPaint);
        //draw X-Axis Label.
        int xSize = xAxis.size();
        float yLocation = clipRectF.bottom + xAxis.getLabelHeight() + xAxis.getGap();
        for (int i = 0; i < xSize; i++) {
            if (xAxis.isVisible(i)) {
                float xLocation = getXLocation(xAxis.getValue(i));

                canvas.drawText(formatXLabel(xAxis.getValue(i)), xLocation, yLocation, labelPaint);
            }
        }

        //draw Y-Axis Label.
        labelPaint.setTextAlign(Paint.Align.LEFT);

        int ySize = yAxis.getSize();
        float _xLocation = clipRectF.right + yAxis.getGap();
        for (int i = 0; i < ySize; i++) {
            float _yLocation = getYLocation(yAxis.getDisplayValue(i))
                    + Math.abs(labelPaint.getFontMetrics().ascent) / 3;

            canvas.drawText(formatYLabel(yAxis.getDisplayValue(i)), _xLocation, _yLocation, labelPaint);
        }
        labelPaint.setTextAlign(Paint.Align.CENTER);
    }

    protected String formatXLabel(String xValue) {
        return xValue;
    }

    protected String formatYLabel(float yValue) {
        DecimalFormat df = new DecimalFormat("0");
        return df.format(yValue) + "万";
    }

    private void drawDataSeriesUnClipped(Canvas canvas, List<Series> seriesList, List<Paint> seriesPaintList) {
        for (int j = 0; j < seriesList.size(); j++) {
            Paint seriesPaint;
            try {
                seriesPaint = seriesPaintList.get(j);
            } catch (Exception e) {
                seriesPaint = seriesPaintList.get(0);
            }

            Series series = seriesList.get(j);

            int c = canvas.saveLayer(clipRectF, null, Canvas.ALL_SAVE_FLAG);

            Path path = new Path();

            float lX = 0, lY = 0;

            int size = series.size();

            for (int i = 0; i < size; i++) {
                String xValue = series.getXValue(i);
                float xLocation = getXLocation(xValue);

                float yValue = series.getYValue(i);
                float yLocation = getYLocation(yValue);

                // previous point
                float preXLocation = i == 0 ? xLocation : getXLocation(series.getXValue(i - 1));
                float preYLocation = i == 0 ? yLocation : getYLocation(series.getYValue(i - 1));
                // next point
                float nextXLocation = i + 1 >= size ? xLocation : getXLocation(series.getXValue(i + 1));
                float nextYLocation = i + 1 >= size ? yLocation : getYLocation(series.getYValue(i + 1));

                // first control point
                // distance between p and p0
                float d0 = (float) Math.sqrt(Math.pow(xLocation - preXLocation, 2) + Math.pow(yLocation - preYLocation, 2));
                // min is used to avoid going too much right
                float x1 = Math.min(preXLocation + lX * d0, (preXLocation + xLocation) / 2);
                float y1 = preYLocation + lY * d0;

                // second control point
                // distance between p1 and p0 (length of reference line)
                float d1 = (float) Math.sqrt(Math.pow(nextXLocation - preXLocation, 2) + Math.pow(nextYLocation - preYLocation, 2));
                lX = (nextXLocation - preXLocation) / d1 * SMOOTHNESS;// (lX,lY) is the slope of the reference line
                //  lY = (nextYLocation - preYLocation) / d1 * SMOOTHNESS;
                lY = 0;
                //max is used to avoid going too much left
                float x2 = Math.max(xLocation - lX * d0, (preXLocation + xLocation) / 2);
                float y2 = yLocation - lY * d0;

                if (i > 0) {
                    //    path.cubicTo(x1, y1, x2, y2, xLocation, yLocation);
                    drawLinePath(path, xLocation, yLocation, x1, y1, x2, y2);
                } else {
                    path.moveTo(xLocation, yLocation);
                }

                canvas.drawCircle(xLocation, yLocation, 5, seriesPaint);
                canvas.drawLine(xLocation, clipRectF.top, xLocation, clipRectF.bottom, axisPaint);

                //
                if (xLocation >= clipRectF.left && xLocation <= clipRectF.right) {
                    xAxis.setVisible(i);
                } else {
                    xAxis.setInVisible(i);
                }
            }
            canvas.drawPath(path, seriesPaint);

            canvas.restoreToCount(c);
        }
    }

    /**
     * get x location by x value.
     *
     * @param xValue x value from datasource.
     * @return x location.
     */
    private float getXLocation(String xValue) {
        return unclipRectF.left + xAxis.getIndex(xValue) * xAxis.getStep();
    }

    /**
     * get y location by y value.
     *
     * @param yValue y value from datasource.
     * @return y location.
     */
    private float getYLocation(float yValue) {

        float yValueRange = yAxis.getMaxValue() - yAxis.getMinValue();

        float yLocationRange = clipRectF.bottom - clipRectF.top;

        float yLocation = yLocationRange * (yAxis.getMaxValue() - yValue) / yValueRange + clipRectF.top;

        return yLocation;
    }

    /**
     * path.lineTo or path.cubicTo
     *
     * @param path object used for canvas.drawPath();
     * @param x0   x-coordinate of next point.
     * @param y0   y-coordinate of next point.
     * @param x1   x-coordinate of first control point.
     * @param y1   y-coordinate of first control point.
     * @param x2   x-coordinate of second control point.
     * @param y2   y-coordinate of second control point.
     */
    protected void drawLinePath(Path path, float x0, float y0, float x1, float y1, float x2, float y2) {
        path.cubicTo(x1, y1, x2, y2, x0, y0);
    }

    private void drawEdgeEffect(Canvas canvas) {
        if (!mLeftEdgeEffect.isFinished()) {
            canvas.translate(clipRectF.left, clipRectF.bottom);
            canvas.rotate(-90, 0, 0);
            mLeftEdgeEffect.setSize((int) clipRectF.height(), (int) clipRectF.width());
            if (mLeftEdgeEffect.draw(canvas)) {
                invalidate();
            }
        }
        if (!mRightEdgeEffect.isFinished()) {
            canvas.translate(clipRectF.right, clipRectF.top);
            canvas.rotate(90, 0, 0);
            mRightEdgeEffect.setSize((int) clipRectF.height(), (int) clipRectF.width());
            if (mRightEdgeEffect.draw(canvas)) {
                invalidate();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = mGestureDetector.onTouchEvent(event);
        return retVal || super.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            unclipRectF.left -= distanceX;
            unclipRectF.right = unclipRectF.left + xAxis.getStep() * (DataRes.getSeries().size() - 1);
            if (unclipRectF.left > clipRectF.left) {
                unclipRectF.left = clipRectF.left;
            }
            if (unclipRectF.right < clipRectF.right) {
                unclipRectF.left = clipRectF.right - xAxis.getStep() * (DataRes.getSeries().size() - 1);
            }
            if (distanceX < 0 && unclipRectF.left >= clipRectF.left) {
                mLeftEdgeEffect.onPull(distanceX / clipRectF.width(), 0.5f);
            }
            if (distanceX > 0 && unclipRectF.right <= clipRectF.right) {
                mRightEdgeEffect.onPull(distanceX / clipRectF.width(), 0.5f);
            }
            invalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mScroller.fling((int) unclipRectF.left, 0, (int) velocityX, 0,
                    (int) (clipRectF.right - xAxis.getStep() * (DataRes.getSeries().size() - 1)), (int) clipRectF.left, 0, 0, (int) xAxis.getStep(), 0);
            //guarantee the smoother effect.
            mLeftEdgeEffect.onRelease();
            mRightEdgeEffect.onRelease();
            invalidate();
            return true;
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.i(TAG, "computeScroll: " + mScroller.getCurrX());
        if (mScroller.computeScrollOffset()) {
            unclipRectF.left = mScroller.getCurrX();
            unclipRectF.right = unclipRectF.left + (xAxis.getSize() - 1) * xAxis.getStep();
            invalidate();
        }
    }

}
