package com.racine.components;

import com.racine.interfaces.Axis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunrx on 2016/9/2.
 */
public class XAxis extends Axis<String> {

    private List<Boolean> visibleList;

    private int index;

    public XAxis() {
        super();
        visibleList = new ArrayList<>();
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

    public int getIndex(String value) {
        return values.indexOf(value);
    }

    public boolean isVisible(int index) {
        if (visibleList.size() > index) {
            return visibleList.get(index);
        } else {
            return false;
        }
    }

    public void setVisible(int index) {
        visibleList.add(index, true);
    }

    public void setInVisible(int index) {
        visibleList.add(index, false);
    }
}
