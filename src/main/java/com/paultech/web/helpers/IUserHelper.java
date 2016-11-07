package com.paultech.web.helpers;

import com.paultech.domain.User;
import com.paultech.web.exceptions.UnauthorizedException;

/**
 * Created by paulzhang on 2/11/2016.
 */
public interface IUserHelper {
    User getUserFromAuthentication() throws UnauthorizedException;
}
