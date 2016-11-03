package com.paultech.web;

import com.paultech.web.exceptions.ItemNotFoundException;
import com.paultech.web.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public String displayForbiddenPage() {
        return "errorPages/unauthorized";
    }
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String display404Page() {
        return "errorPages/itemNotFound";
    }
}
