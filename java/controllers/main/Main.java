package com.incorpr.controllers.Main;

import com.incorpr.models.Event;
import com.incorpr.models.User;
import com.incorpr.repositories.EventsRepository;
import com.incorpr.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

public class Main {

    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected EventsRepository eventsRepository;

    @Value("${upload.path}")
    protected String uploadPath;

    protected User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((!(auth instanceof AnonymousAuthenticationToken)) && auth != null) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            return usersRepository.findByUsername(userDetail.getUsername());
        }
        return null;
    }

    protected String getRole() {
        User users = getUser();
        if (users == null) return "NOT";
        return users.getRole().toString();
    }

    protected void AddAttributes(Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("user", getUser());
    }

}
