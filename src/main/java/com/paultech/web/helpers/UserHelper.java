package com.paultech.web.helpers;

import com.paultech.domain.User;
import com.paultech.service.UserRepo;
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
    public User getUserFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepo.findByEmail(userEmail);
    }
}
