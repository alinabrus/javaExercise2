package com.abrus.user.service;

import com.abrus.user.exception.ValidationException;
import com.abrus.user.model.User;
import com.abrus.user.model.UserRegistrationDto;
import com.abrus.user.model.UserResponseDto;
import com.abrus.user.repository.UserRepository;
import com.abrus.user.validator.UserRegistrationValidator;
import com.abrus.user.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRegistrationValidator userRegistrationValidator;

    @InjectMocks
    private UserServiceImpl userService;

    private User user = new User(1L, "john@white.com", "AAbbcc789", null);

    @Test
    void registerUser_WithValidUser() {
        when(userRegistrationValidator.validate(any())).thenReturn(new ValidationResult(true, null));
        when(userRepository.insert(any())).thenReturn(user);
        when(userRepository.getById(any())).thenReturn(Optional.of(user));

        UserRegistrationDto newUser = new UserRegistrationDto(user.getEmail(), user.getPassword(), user.getPassword(), user.getPhoneNumber());
        assertDoesNotThrow(() -> userService.registerUser(newUser));
    }

    @Test
    void registerUser_WithEmptyPassword() {
        when(userRegistrationValidator.validate(any())).thenReturn(new ValidationResult(false, "Password is required"));

        UserRegistrationDto newUser = new UserRegistrationDto(user.getEmail(), "", user.getPassword(), user.getPhoneNumber());
        Exception exception = assertThrows(ValidationException.class, () -> userService.registerUser(newUser));
        assertEquals("Password is required", exception.getMessage());
    }

    @Test
    void getUserById() {
        when(userRepository.getById(any())).thenReturn(Optional.of(user));

        Optional<UserResponseDto> foundRecord = userService.getUserById(1L);
        User foundUser = null;
        if (foundRecord.isPresent()) {
            foundUser = new User();
            foundUser.setEmail(foundRecord.get().email());
        }
        assertEquals(foundUser.getEmail(), user.getEmail());
    }

    @Test
    void getUserList() {
        when(userRepository.getList()).thenReturn(List.of(user));

        assertDoesNotThrow(() -> userService.getUserList());
    }
}