package com.racine.exception;

/**
 * Created by Shawn Racine on 2016/10/17.
 */
public class UnassignedException extends RuntimeException {
    public UnassignedException() {
    }

    public UnassignedException(String detailMessage) {
        super(detailMessage);
    }
}
