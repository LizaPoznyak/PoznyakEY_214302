package com.incorpr.controllers;

import com.incorpr.controllers.Main.Main;
import com.incorpr.models.User;
import com.incorpr.models.enums.Role;
import com.incorpr.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class StaffController extends Main {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/staff/all")
    public String staff(Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("users", usersRepository.findAll());
        return "staff";
    }

    @GetMapping("/staff/{id}")
    public String profile(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("role", getRole());
        if(!usersRepository.existsById(id)) {
            return "redirect:/staff/all";
        }
        Optional<User> user = usersRepository.findById(id);
        ArrayList<User> result = new ArrayList<>();
        user.ifPresent(result::add);
        model.addAttribute("users", result);
        model.addAttribute("roles", Role.values());
        return "profile";
    }

    @PostMapping("/staff/{id}/delete")
    public String deleteUserPost(@PathVariable(value = "id") Long id, Model model) {
        User user = usersRepository.findById(id).orElseThrow();
        usersRepository.delete(user);
        return "redirect:/staff/all";
    }

}
