package com.racine.components;

/**
 * Created by sunrx on 2016/9/2.
 */
public class XAxis extends Axis<String> {

    public XAxis() {
        super();
    }

    @Override
    public float getStep() {
        if (step == 0) {
            step = labelWidth * 2;
        }
        return step;
    }

    @Override
    public int getSize() {
        if (size == 0) {
            size = values.size();
        }
        return size;
    }
}
