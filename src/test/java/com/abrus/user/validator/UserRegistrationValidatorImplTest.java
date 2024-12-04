package com.abrus.user.validator;

import com.abrus.user.model.User;
import com.abrus.user.model.UserRegistrationDto;
import com.abrus.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRegistrationValidatorImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRegistrationValidatorImpl userRegistrationValidator;

    @Test
    void validate_WithEmptyEmail() {
        UserRegistrationDto input = new UserRegistrationDto("", "aaBBcc123", "aaBBcc123", "1234567890");
        ValidationResult validationResult = userRegistrationValidator.validate(input);

        assertFalse(validationResult.getIsValid());
        assertEquals("Email is required", validationResult.getErrorMsg());
    }

    @Test
    void validate_WithInvalidEmail() {
        UserRegistrationDto input = new UserRegistrationDto("bob", "aaBBcc123", "aaBBcc123", "1234567890");
        ValidationResult validationResult = userRegistrationValidator.validate(input);

        assertFalse(validationResult.getIsValid());
        assertEquals("Email is not valid", validationResult.getErrorMsg());
    }

    @Test
    void validate_WithPasswordsNotMatch() {
        UserRegistrationDto input = new UserRegistrationDto("bob@bob.com", "aaBBcc123", "aaBBcc456", "1234567890");
        ValidationResult validationResult = userRegistrationValidator.validate(input);

        assertFalse(validationResult.getIsValid());
        assertEquals("Password and repeat password must match.", validationResult.getErrorMsg());
    }

    @Test
    void validate_WithInvalidPassword() {
        UserRegistrationDto input = new UserRegistrationDto("bob@bob.com", "aaBBcc", "aaBBcc", "1234567890");
        ValidationResult validationResult = userRegistrationValidator.validate(input);

        assertFalse(validationResult.getIsValid());
        assertEquals("Password length must be not less than 8 and not more than 20 characters.", validationResult.getErrorMsg());
    }

    @Test
    void validate_WithValidUser() {
        UserRegistrationDto input = new UserRegistrationDto("bob@bob.com", "aaBBcc123", "aaBBcc123", "1234567890");
        ValidationResult validationResult = userRegistrationValidator.validate(input);

        assertTrue(validationResult.getIsValid());
    }

    @Test
    void validate_WithDuplicatedEmail() {
        User user = new User();
        user.setEmail("bob@bob.com");
        when(userRepository.getByEmail(any())).thenReturn(Optional.of(user));

        UserRegistrationDto input = new UserRegistrationDto("bob@bob.com", "aaBBcc123", "aaBBcc123", "1234567890");
        ValidationResult validationResult = userRegistrationValidator.validate(input);
        assertFalse(validationResult.getIsValid());
    }
}