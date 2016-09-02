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
    public void setScaleNum(int scaleNum) {
        this.scaleNum = scaleNum;

        step = Math.abs(contentRect.right - contentRect.left) / (scaleNum - 1);
    }

    @Override
    public int getScaleNum() {
        if (scaleNum == 0) {
            scaleNum = (int) (Math.abs(contentRect.right - contentRect.left) / getStep()) + 1;
        }
        return scaleNum;
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
