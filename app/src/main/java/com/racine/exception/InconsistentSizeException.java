package com.racine.exception;

/**
 * Created by sunrx on 2016/10/11.
 */
public class InconsistentSizeException extends RuntimeException {
    public InconsistentSizeException() {
    }

    public InconsistentSizeException(String detailMessage) {
        super(detailMessage);
    }
}
