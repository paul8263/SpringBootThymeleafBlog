package com.paultech.web;

import com.paultech.domain.User;
import com.paultech.service.JavaBlogUserDetailsService;
import com.paultech.service.UserRepo;
import com.paultech.web.exceptions.UnauthorizedException;
import com.paultech.web.helpers.IUserHelper;
import com.paultech.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Created by paulzhang on 28/10/2016.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JavaBlogUserDetailsService javaBlogUserDetailsService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private IUserHelper userHelper;

    @PostMapping
    public String signUp(@ModelAttribute @Valid User user, BindingResult result) {
        userValidator.validate(user, result);
        User existedUser = userRepo.findByEmail(user.getEmail());
        if (existedUser != null) {
            result.rejectValue("email", "NotValid", "This email has already been registered");
        }
        if (result.hasErrors()) {
            return "signUp";
        }

        String plainPassword = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(plainPassword));

        userRepo.save(user);
        UserDetails userDetails = javaBlogUserDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                plainPassword,
                userDetails.getAuthorities()
        );

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        return "redirect:/blog";
    }

    @GetMapping(value = "/settings")
    public String displaySettingPage(Model model) throws UnauthorizedException {
        User user = userHelper.getUserFromAuthentication();
        if (null == user) throw new UnauthorizedException();
        model.addAttribute("user", user);
        return "settings";
    }

//    @PostMapping
//    public String modifyUser() {}

}
