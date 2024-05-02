package com.example.incorpr;

import com.example.incorpr.controllers.EventsController;
import com.example.incorpr.models.Event;
import com.example.incorpr.models.enums.Type;
import com.example.incorpr.repositories.EventsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventsControllerTest {

    @InjectMocks
    private EventsController eventsController;

    @Mock
    private EventsRepository eventsRepository;

    @Test
    public void testEvents() {
        Model model = mock(Model.class);
        Iterable<Event> events = new ArrayList<>();
        when(eventsRepository.findAll()).thenReturn((List<Event>) events);
        String result = eventsController.events(model);
        assertEquals("events", result);
        verify(model).addAttribute("events", events);
        verify(model).addAttribute("role", eventsController.getRole());
    }

    @Test
    public void testAddEvent() {
        Model model = mock(Model.class);
        String result = eventsController.addEvent(model);
        assertEquals("add-event", result);
        verify(model).addAttribute("role", eventsController.getRole());
        verify(model).addAttribute("type", Type.values());
    }

    @Test
    public void testAddEventPost() {
        String title = "Test Event";
        String description = "Test description";
        String type = "Type1";
        Date date = new Date(System.currentTimeMillis());
        Event event = new Event(title, type, description, date);
        String result = eventsController.addEventPost(title, description, type, date, mock(Model.class));
        assertEquals("redirect:/events/all", result);
        verify(eventsRepository).save(event);
    }

    @Test
    void testEventDetails() {
        Long eventId = 1L;
        Event event = new Event(eventId, "Test Event", "IT_конференция", "Test description", Date.valueOf("2023-01-01"));
        EventsRepository eventsRepository = mock(EventsRepository.class);
        when(eventsRepository.findById(eventId)).thenReturn(Optional.of(event));
        Model model = mock(Model.class);
        EventsController controller = new EventsController();
        controller.eventsRepository = eventsRepository;
        String result = controller.eventDetails(eventId, model);
        assertEquals("redirect:/events/all", result);
        verify(eventsRepository).findById(eventId);
        verify(model).addAttribute("event", new ArrayList<>(List.of(event)));
    }

    @Test
    void testEditEvent() {
        Event existingEvent = new Event(1L, "Event 1", "Description 1", "Type 1", Date.valueOf("2023-01-01"));
        when(eventsRepository.existsById(1L)).thenReturn(true);
        when(eventsRepository.findById(1L)).thenReturn(Optional.of(existingEvent));
        String result = eventsController.editEvent(1L, mock(Model.class));
        assertEquals("edit-event", result);
    }

    @Test
    void testEditEventPost() {
        Event existingEvent = new Event(1L, "Event 1", "Description 1", "Type 1", Date.valueOf("2023-01-01"));
        when(eventsRepository.findById(1L)).thenReturn(Optional.of(existingEvent));
        String result = eventsController.editEventPost(1L, "New Title", "New Description", "New Type", Date.valueOf("2023-02-01"), mock(Model.class));
        assertEquals("redirect:/events/all", result);
        assertEquals("New Title", existingEvent.getTitle());
        assertEquals("New Description", existingEvent.getDescription());
        assertEquals("New Type", existingEvent.getType());
        assertEquals(Date.valueOf("2023-02-01"), existingEvent.getDate());
        verify(eventsRepository, times(1)).save(existingEvent);
    }

    @Test
    void testDeleteEventPost() {
        Event existingEvent = new Event(1L, "Event 1", "Description 1", "Type 1", Date.valueOf("2023-01-01"));
        when(eventsRepository.findById(1L)).thenReturn(Optional.of(existingEvent));
        String result = eventsController.deleteEventPost(1L, mock(Model.class));
        assertEquals("redirect:/events/all", result);
        verify(eventsRepository, times(1)).delete(existingEvent);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRegisterUserForEvent() throws Exception {
        Long eventId = 1L; 
        Long userId = 1L; 
        mockMvc.perform(post("/events/" + eventId + "/register")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events/all"));
    }

    @Test
    public void testViewRegisteredUsers() throws Exception {
        Long eventId = 1L; 
        MvcResult result = mockMvc.perform(get("/events/" + eventId + "/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("event-users"))
                .andReturn();
    }
}
