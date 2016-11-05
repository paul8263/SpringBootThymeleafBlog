package com.paultech.web;

import com.paultech.domain.User;
import com.paultech.service.JavaBlogUserDetailsService;
import com.paultech.service.UserRepo;
import com.paultech.web.exceptions.UnauthorizedException;
import com.paultech.web.helpers.IUserHelper;
import com.paultech.web.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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

    @InitBinder
    public void bindValidator(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    @PostMapping
    public String signUp(@ModelAttribute @Valid User user, BindingResult result) {
//        userValidator.validate(user, result);
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

    @GetMapping(value = "/settings/password")
    public String displayChangePasswordPage(@RequestParam(required = false) String error , Model model) {
        model.addAttribute("error", error);
        return "changePassword";
    }

    @PostMapping(value = "/settings/password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String newPassword2) {
        final int MINLENGTH = 6;
        final int MAXLENGTH = 16;

        if (!newPassword.equals(newPassword2)) return "redirect:/user/settings/password?error=confirm";

        if (newPassword.length() < MINLENGTH || newPassword.length() > MAXLENGTH) return "redirect:/user/settings/password?error=new";

        User user = userHelper.getUserFromAuthentication();
        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) return "redirect:/user/settings/password?error=old";

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepo.save(user);

        return "redirect:/user/settings";
    }

}
