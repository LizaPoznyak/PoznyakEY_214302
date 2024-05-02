package com.example.incorpr;

import com.example.incorpr.controllers.StaffController;
import com.example.incorpr.models.User;
import com.example.incorpr.models.enums.Role;
import com.example.incorpr.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StaffControllerTest {

    private StaffController staffController;

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        staffController = new StaffController();
        staffController.setUsersRepository(usersRepository);
    }

    @Test
    void testStaff() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "Joseph", "remoduv432", Role.USER));
        userList.add(new User(2L, "Annie", "j345tbvcd3", Role.USER));
        when(usersRepository.findAll()).thenReturn(userList);
        String result = staffController.staff(mock(Model.class));
        assertEquals("staff", result);
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    void testProfile() {
        User existingUser = new User(1L, "Joseph", "remoduv432", Role.USER);
        when(usersRepository.existsById(1L)).thenReturn(true);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        String result = staffController.profile(1L, mock(Model.class));
        assertEquals("profile", result);
    }

    @Test
    void testDeleteUserPost() {
        User existingUser = new User(1L, "Joseph", "remoduv432", Role.USER);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        String result = staffController.deleteUserPost(1L, mock(Model.class));
        assertEquals("redirect:/staff/all", result);
        verify(usersRepository, times(1)).delete(existingUser);
    }
}
