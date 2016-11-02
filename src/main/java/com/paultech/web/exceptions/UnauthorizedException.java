package com.paultech.web.exceptions;

/**
 * Created by paulzhang on 2/11/2016.
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
