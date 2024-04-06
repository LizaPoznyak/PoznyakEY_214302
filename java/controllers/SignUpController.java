package com.incorpr.controllers;

import com.incorpr.controllers.Main.Main;
import com.incorpr.models.Event;
import com.incorpr.models.User;
import com.incorpr.models.enums.Role;
import com.incorpr.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class SignUpController extends Main {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/sign-up")
    public String sighUp(Model model) {
        model.addAttribute("role", getRole());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String sighUpPost(@RequestParam String username, @RequestParam String password, Model model) {
        if (usersRepository.findAll().size() == 0 || usersRepository.findAll().isEmpty()) {
            usersRepository.save(new User(username, password, Role.ADMIN));
            return "redirect:/sign-in";
        }
        User userFromDB = usersRepository.findByUsername(username);
        if (userFromDB != null) {
            model.addAttribute("role", getRole());
            model.addAttribute("message", "Пользователь c таким именем уже существует существует!");
            return "sign-up";
        }
        usersRepository.save(new User(username, password, Role.USER));
        return "redirect:/sign-in";
    }

}
