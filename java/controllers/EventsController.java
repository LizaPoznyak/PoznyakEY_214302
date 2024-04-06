package com.incorpr.controllers;

import com.incorpr.controllers.Main.Main;
import com.incorpr.models.Event;
import com.incorpr.repositories.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class EventsController extends Main {

    @Autowired
    private EventsRepository eventsRepository;

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
        return "add-event";
    }

    @PostMapping("/events/add")
    public String addEventPost(@RequestParam String title, @RequestParam String description, @RequestParam String type, @RequestParam int day, @RequestParam int month, @RequestParam int year, Model model) {
        Event event = new Event(title, type, description, day, month, year);
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
    public String editEventPost(@PathVariable(value = "id") Long id, @RequestParam String title, @RequestParam String description, @RequestParam String type, @RequestParam int day, @RequestParam int month,  @RequestParam int year, Model model) {
        Event event = eventsRepository.findById(id).orElseThrow();
        event.setTitle(title);
        event.setDescription(description);
        event.setType(type);
        event.setDay(day);
        event.setMonth(month);
        event.setYear(year);
        eventsRepository.save(event);
        return "redirect:/events/all";
    }

    @PostMapping("/events/{id}/delete")
    public String deleteEventPost(@PathVariable(value = "id") Long id, Model model) {
        Event event = eventsRepository.findById(id).orElseThrow();
        eventsRepository.delete(event);
        return "redirect:/events/all";
    }

}
