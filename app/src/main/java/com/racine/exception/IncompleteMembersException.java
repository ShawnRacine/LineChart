package com.racine.exception;

/**
 * Created by sunrx on 2016/8/30.
 */
public class IncompleteMembersException extends RuntimeException {
    public IncompleteMembersException() {
    }

    public IncompleteMembersException(String message) {
        super(message);
    }
}
