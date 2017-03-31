package com.racine.components;

/**
 * Created by Shawn Racine on 2016/10/21.
 */
public class Legend {
    private float width;
    private float height;
    private int align;

    private static final int ALIGN_LEFT = 1;
    private static final int ALIGN_TOP = 2;
    private static final int ALIGN_RIGHT = 3;
    private static final int ALIGN_BOTTOM = 4;

    public Legend() {
        this.height = 30;
        align = ALIGN_BOTTOM;
    }

    public float getHorOccupied() {
        if (align == ALIGN_BOTTOM){
            return height;
        }
        return 0;
    }

    public float getVerOccupied() {
        if (align == ALIGN_RIGHT){
            return width;
        }
        return 0;
    }
}
