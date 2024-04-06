package com.incorpr.controllers;

import com.incorpr.controllers.Main.Main;
import com.incorpr.models.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController extends Main {

    @GetMapping("/")
    public String home(Model model) {
        List<Event> event = eventsRepository.findAll();
        List<Event> result = new ArrayList<>();
        int i = 0;
        while (i < 4) {
            if (event.get(i) == null) result.add(null);
            else result.add(event.get(i));
            i++;
        }
        model.addAttribute("events", result);
        model.addAttribute("role", getRole());
        return "home";
    }

}
