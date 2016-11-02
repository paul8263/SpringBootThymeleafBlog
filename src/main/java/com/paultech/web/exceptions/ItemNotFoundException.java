package com.paultech.web.exceptions;

/**
 * Created by paulzhang on 2/11/2016.
 */
public class ItemNotFoundException extends Exception {
    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
