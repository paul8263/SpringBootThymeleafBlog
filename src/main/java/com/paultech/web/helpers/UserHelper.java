package com.paultech.web.helpers;

import com.paultech.domain.User;
import com.paultech.service.UserRepo;
import com.paultech.web.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by paulzhang on 2/11/2016.
 */
@Component
public class UserHelper implements IUserHelper {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User getUserFromAuthentication() throws UnauthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepo.findByEmail(userEmail);
        if (null == user) throw new UnauthorizedException("User is not authenticated");
        else return userRepo.findByEmail(userEmail);
    }
}
