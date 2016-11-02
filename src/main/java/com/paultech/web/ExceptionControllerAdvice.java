package com.paultech.web;

import com.paultech.web.exceptions.ItemNotFoundException;
import com.paultech.web.exceptions.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by paulzhang on 2/11/2016.
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(value = ItemNotFoundException.class)
    public String displayItemNotFoundPage() {
        return "errorPages/itemNotFound";
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public String displayUnauthorizedPage() {
        return "errorPages/unauthorized";
    }
}
