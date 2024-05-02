package com.example.incorpr.controllers;

import com.example.incorpr.controllers.main.Main;
import com.example.incorpr.models.Event;
import com.example.incorpr.models.User;
import com.example.incorpr.models.enums.Type;
import com.example.incorpr.repositories.EventsRepository;
import com.example.incorpr.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class EventsController extends Main {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    public EventsRepository eventsRepository;

    @GetMapping("/events/all")
    public String events(Model model) {
        Iterable<Event> events = eventsRepository.findAll();
        model.addAttribute("events", events);
        model.addAttribute("role", getRole());
        return "events";
    }

    @GetMapping("/events/add")
    public String addEvent(Model model) {
        model.addAttribute("role", getRole());
        model.addAttribute("type", Type.values());
        return "add-event";
    }

    @PostMapping("/events/add")
    public String addEventPost(@RequestParam String title, @RequestParam String description, @RequestParam String type, @RequestParam Date date, Model model) {
        Event event = new Event(title, type, description, date);
        eventsRepository.save(event);
        return "redirect:/events/all";
    }

    @GetMapping("/events/{id}")
    public String eventDetails(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("role", getRole());
        if(!eventsRepository.existsById(id)) {
            return "redirect:/events/all";
        }
        Optional<Event> event = eventsRepository.findById(id);
        ArrayList<Event> result = new ArrayList<>();
        event.ifPresent(result::add);
        model.addAttribute("event", result);
        return "event-details";
    }

    @GetMapping("/events/{id}/edit")
    public String editEvent(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("type", Type.values());
        model.addAttribute("role", getRole());
        if(!eventsRepository.existsById(id)) {
            return "redirect:/events/all";
        }
        Optional<Event> event = eventsRepository.findById(id);
        ArrayList<Event> result = new ArrayList<>();
        event.ifPresent(result::add);
        model.addAttribute("event", result);
        return "edit-event";
    }

    @PostMapping("/events/{id}/edit")
    public String editEventPost(@PathVariable(value = "id") Long id, @RequestParam String title, @RequestParam String description, @RequestParam String type, @RequestParam Date date, Model model) {
        Event event = eventsRepository.findById(id).orElseThrow();
        event.setTitle(title);
        event.setDescription(description);
        event.setType(type);
        event.setDate(date);
        eventsRepository.save(event);
        return "redirect:/events/all";
    }

    @PostMapping("/events/{id}/delete")
    public String deleteEventPost(@PathVariable(value = "id") Long id, Model model) {
        Event event = eventsRepository.findById(id).orElseThrow();
        eventsRepository.delete(event);
        return "redirect:/events/all";
    }

//    @PostMapping("/events/{id}/register")
//    public String registerUserForEvent(@PathVariable(value = "id") Long id, @RequestParam Long userId, Model model) {
//        Event event = eventsRepository.findById(id).orElseThrow();
//        User user = usersRepository.findById(userId).orElseThrow();
//        UserEvent userEvent = new UserEvent(user, event);
//        userEventRepository.save(userEvent);
//        return "redirect:/events/all";
//    }
//
//    @GetMapping("/events/{id}/users")
//    public String viewRegisteredUsers(@PathVariable(value = "id") Long id, Model model) {
//        Event event = eventsRepository.findById(id).orElseThrow();
//        List<UserEvent> userEvents = userEventRepository.findByEvent(event);
//        List<User> users = userEvents.stream().map(UserEvent::getUser).collect(Collectors.toList());
//        model.addAttribute("users", users);
//        return "event-users";
//    }

}
