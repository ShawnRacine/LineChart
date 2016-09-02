package com.racine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by sunrx on 2016/9/1.
 */
public class MScrollView extends ScrollView {
    private static final String TAG = "MScrollView";

    public MScrollView(Context context) {
        this(context, null);
    }

    public MScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.i(TAG, "ACTION_DOWN " + super.onTouchEvent(ev));
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, "ACTION_MOVE " + super.onTouchEvent(ev));
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.i(TAG, "ACTION_UP " + super.onTouchEvent(ev));
//                break;
//        }
        return super.onTouchEvent(ev);
    }
}
