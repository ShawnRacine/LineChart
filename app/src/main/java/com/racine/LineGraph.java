package com.racine;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;
import com.racine.components.Series;
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

/**
 * Created by sunrx on 2016/8/23.
 */
public class LineGraph extends View {
    private static final String TAG = "LineC";

    protected Context context;

    protected List<Series> seriesList;

    protected ViewportHandler viewportHandler;

    protected AxisRenderer xAxisRenderer;
    protected AxisRenderer yAxisRenderer;

    protected EdgeEffectRenderer leftEdgeEffectRenderer;
    protected EdgeEffectRenderer rightEdgeEffectRenderer;

    protected SeriesRenderer seriesRenderer;

    protected LegendRenderer legendRenderer;

    //
    private GestureDetector mGestureDetector;

    protected OverScroller mScroller;

    public LineGraph(Context context) {
        this(context, null);
    }

    public LineGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        seriesList = new ArrayList<>();

        viewportHandler = new ViewportHandler(this);

        mGestureDetector = new GestureDetector(context, onGestureListener);
        mScroller = new OverScroller(context, new DecelerateInterpolator());
    }

    public void addSeries(Series series) {
        seriesList.add(series);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewportHandler.setDimens(w, h);
        viewportHandler.restrainViewport(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        viewportHandler.assembleAxesValues(seriesList);

        initializeRenderer();
    }

    protected void initializeRenderer() {
        xAxisRenderer = new XAxisRenderer(viewportHandler);
        yAxisRenderer = new YAxisRenderer(viewportHandler);

        leftEdgeEffectRenderer = new LeftEdgeEffectRenderer(context, viewportHandler);
        rightEdgeEffectRenderer = new RightEdgeEffectRenderer(context, viewportHandler);

        seriesRenderer = new SmoothLineRenderer(viewportHandler);

        legendRenderer = new LineLegendRenderer(viewportHandler);
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

//    public void start(int duration) {
//        mScroller.startScroll(viewportHandler.scrollStartX(), 0, viewportHandler.scrollDistanceX(), 0, duration);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        xAxisRenderer.drawAxisLine(canvas);
        yAxisRenderer.drawAxisLine(canvas);

        yAxisRenderer.drawAxisLabel(canvas);

        seriesRenderer.drawDataSeriesUnClipped(canvas, seriesList);

        //get visibility of xAxis after drawDataSeriesUnClipped.
        xAxisRenderer.drawGridLines(false);
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
            Log.i(TAG, "onSingleTapUp: " + e.getX());
            return true;
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
                    viewportHandler.flingMinX(), viewportHandler.flingMaxX(), (int) viewportHandler.xAxis.getStep(), 0);
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

    protected void scrollViewport(float currX) {
        viewportHandler.moveUnclipRectLeft(currX);

        if (viewportHandler.isLeftEdge()) {
            viewportHandler.moveUnclipRectLeft(viewportHandler.layerLeft());
        }
        if (viewportHandler.isRightEdge()) {
            viewportHandler.moveUnclipRectRight(viewportHandler.layerRight());
        }
    }
}
