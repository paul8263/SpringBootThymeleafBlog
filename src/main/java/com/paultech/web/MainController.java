package com.paultech.web;

import com.paultech.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by paulzhang on 28/10/2016.
 */
@Controller
public class MainController {
    @GetMapping(value = "/")
    public String index() {
        return "redirect:/blog";
    }

    @GetMapping(value = "/about")
    public String about() {
        return "about";
    }

    @GetMapping(value = "/signup")
    public String signUp(Model model) {
        model.addAttribute("user", new User());
        return "signUp";
    }

    @GetMapping(value = "/login")
    public String displayLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "login";
    }
}
