package com.racine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;
import com.racine.components.Axis;
import com.racine.components.Series;
import com.racine.components.XAxis;
import com.racine.components.YAxis;
import com.racine.linechart.R;
import com.racine.renderer.axis.AxisRenderer;
import com.racine.renderer.axis.XAxisRenderer;
import com.racine.renderer.axis.YAxisRenderer;
import com.racine.renderer.edge.EdgeEffectRenderer;
import com.racine.renderer.edge.LeftEdgeEffectRenderer;
import com.racine.renderer.edge.RightEdgeEffectRenderer;
import com.racine.renderer.legend.LegendRenderer;
import com.racine.renderer.legend.LineLegendRenderer;
import com.racine.renderer.series.SeriesRenderer;
import com.racine.renderer.series.SmoothLineRenderer;
import com.racine.utils.ViewportHandler;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.width;

/**
 * Created by sunrx on 2016/8/23.
 */
public abstract class GraphView extends View {
    private static final String TAG = "LineC";

    private Context context;

    protected XAxis xAxis = new XAxis();
    protected YAxis yAxis = new YAxis();

    protected ViewportHandler viewportHandler;

    protected AxisRenderer xAxisRenderer;
    protected AxisRenderer yAxisRenderer;

    protected EdgeEffectRenderer leftEdgeEffectRenderer;
    protected EdgeEffectRenderer rightEdgeEffectRenderer;

    protected SeriesRenderer seriesRenderer;

    protected LegendRenderer legendRenderer;

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

        viewportHandler = new ViewportHandler(this, xAxis, yAxis);

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

        viewportHandler.setDimens(w, h);
        viewportHandler.restrainViewport(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());

        xAxisRenderer = new XAxisRenderer(viewportHandler, xAxis);
        yAxisRenderer = new YAxisRenderer(viewportHandler, yAxis);

        leftEdgeEffectRenderer = new LeftEdgeEffectRenderer(context, viewportHandler);
        rightEdgeEffectRenderer = new RightEdgeEffectRenderer(context, viewportHandler);

        seriesRenderer = new SmoothLineRenderer(viewportHandler, xAxisRenderer, yAxisRenderer);

        legendRenderer = new LineLegendRenderer();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minChartSize = getResources().getDimensionPixelSize(R.dimen.min_chart_size);

        setMinimumWidth(minChartSize);
        setMinimumHeight(minChartSize);
        //when set wrapcontent,
        int wrap_width = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int wrap_height = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();

        setMeasuredDimension(resolveSize(wrap_width, widthMeasureSpec), resolveSize(wrap_height, heightMeasureSpec));
    }

    public void start(int duration) {
        mScroller.startScroll(viewportHandler.scrollStartX(), 0, viewportHandler.scrollDistanceX(), 0, duration);
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

        legendRenderer.drawLegend(canvas);
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
            scrollViewport(viewportHandler.scrollerCurrentX(distanceX));

            if (viewportHandler.isLeftEdge()) {
                leftEdgeEffectRenderer.onPull(distanceX);
            }
            if (viewportHandler.isRightEdge()) {
                rightEdgeEffectRenderer.onPull(distanceX);
            }
            postInvalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mScroller.fling(viewportHandler.flingStartX(), 0, (int) velocityX, 0,
                    viewportHandler.flingMinX(), viewportHandler.flingMaxX(), (int) xAxis.getStep(), 0);
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
        viewportHandler.setWholeLeft(position);

        if (viewportHandler.isLeftEdge()) {
            viewportHandler.setWholeLeft(viewportHandler.layerLeft());
        }
        if (viewportHandler.isRightEdge()) {
            viewportHandler.setWholeRight(viewportHandler.layerRight());
        }
    }

}
