package com.incorpr.controllers;

import com.incorpr.controllers.Main.Main;
import com.incorpr.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SighInController extends Main {

    @Autowired
    private  UsersRepository usersRepository;

    @GetMapping("/sign-in")
    public String signIn(Model model) {
        model.addAttribute("role", getRole());
        return "sign-in";
    }

}
