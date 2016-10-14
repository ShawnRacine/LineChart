package com.racine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;

import com.racine.components.Axis;
import com.racine.linechart.R;
import com.racine.components.XAxis;
import com.racine.components.YAxis;
import com.racine.components.Series;
import com.racine.datas.DataRes;
import com.racine.renderer.axis.AxisRenderer;
import com.racine.renderer.axis.XAxisRenderer;
import com.racine.renderer.axis.YAxisRenderer;
import com.racine.renderer.edge.EdgeEffectRenderer;
import com.racine.renderer.edge.LeftEdgeEffectRenderer;
import com.racine.renderer.edge.RightEdgeEffectRenderer;
import com.racine.renderer.series.SeriesRenderer;
import com.racine.renderer.series.SmoothLineRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunrx on 2016/8/23.
 */
public abstract class GraphView extends View {
    private static final String TAG = "LineC";

    private Context context;

    private int width, height;

    protected XAxis xAxis = new XAxis();
    protected YAxis yAxis = new YAxis();

    protected RectF contentRectF = new RectF();
    protected RectF unclipRectF = new RectF();
    protected RectF clipRectF = new RectF();

    protected AxisRenderer xAxisRenderer;
    protected AxisRenderer yAxisRenderer;

    protected EdgeEffectRenderer leftEdgeEffectRenderer;
    protected EdgeEffectRenderer rightEdgeEffectRenderer;

    protected SeriesRenderer seriesRenderer;

    private List<Series> seriesList;

    //
    private GestureDetector mGestureDetector;

    private OverScroller mScroller;

    public GraphView(Context context) {
        this(context, null);
    }

    public GraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        seriesList = new ArrayList<>();

        mGestureDetector = new GestureDetector(context, onGestureListener);
        mScroller = new OverScroller(context, new DecelerateInterpolator());
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
        }
        assembleYValues(yAxis);
    }

    protected void assembleYValues(Axis yAxis) {
        float yRange = yAxis.getMaxValue() - yAxis.getMinValue();
        float unit = yRange / (yAxis.getSize() - 1);
        for (int i = 0; i < yAxis.getSize(); i++) {
            yAxis.addValue(i, yAxis.getMinValue() + i * unit);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        contentRectF.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());

        clipRectF.set(contentRectF.left + xAxis.getLabelWidth() / 2,
                contentRectF.top + yAxis.getLabelHeight(),
                contentRectF.right - yAxis.getLabelWidth() - yAxis.getGap(),
                contentRectF.bottom - xAxis.getLabelHeight() - xAxis.getGap());

        unclipRectF.set(clipRectF.left, clipRectF.top, clipRectF.left + (xAxis.getSize() - 1) * xAxis.getStep(), clipRectF.bottom);

        xAxisRenderer = new XAxisRenderer(clipRectF, xAxis, unclipRectF);
        yAxisRenderer = new YAxisRenderer(clipRectF, yAxis);

        leftEdgeEffectRenderer = new LeftEdgeEffectRenderer(context, this, clipRectF);
        rightEdgeEffectRenderer = new RightEdgeEffectRenderer(context, this, clipRectF);

        seriesRenderer = new SmoothLineRenderer(clipRectF, xAxisRenderer, yAxisRenderer, xAxis);
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
        xAxisRenderer.drawAxisLine(canvas);
        yAxisRenderer.drawAxisLine(canvas);

        yAxisRenderer.drawAxisLabel(canvas);

        seriesRenderer.drawDataSeriesUnClipped(canvas, seriesList);

        //get visibility of xAxis after drawDataSeriesUnClipped.
        xAxisRenderer.drawAxisLabel(canvas);

        leftEdgeEffectRenderer.drawEdgeEffect(canvas);
        rightEdgeEffectRenderer.drawEdgeEffect(canvas);
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
            scrollViewport(unclipRectF.left - distanceX);

            if (unclipRectF.left >= clipRectF.left) {
                leftEdgeEffectRenderer.onPull(distanceX);
            }
            if (unclipRectF.right <= clipRectF.right) {
                rightEdgeEffectRenderer.onPull(distanceX);
            }
            postInvalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mScroller.fling((int) unclipRectF.left, 0, (int) velocityX, 0,
                    (int) (clipRectF.right - xAxis.getStep() * (DataRes.getSeries().size() - 1)), (int) clipRectF.left, 0, 0, (int) xAxis.getStep(), 0);
            //guarantee the smoother effect.
            leftEdgeEffectRenderer.onRelease();
            rightEdgeEffectRenderer.onRelease();
            postInvalidate();
            return true;
        }
    };

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollViewport(mScroller.getCurrX());

            postInvalidate();
        }
    }

    private void scrollViewport(float position) {
        unclipRectF.left = position;
        unclipRectF.right = unclipRectF.left + (xAxis.getSize() - 1) * xAxis.getStep();

        if (unclipRectF.left > clipRectF.left) {
            unclipRectF.left = clipRectF.left;
        }
        if (unclipRectF.right < clipRectF.right) {
            unclipRectF.left = clipRectF.right - xAxis.getStep() * (DataRes.getSeries().size() - 1);
        }
    }

}
