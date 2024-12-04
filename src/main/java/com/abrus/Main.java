package com.abrus;

import com.abrus.user.model.UserRegistrationDto;
import com.abrus.user.model.UserResponseDto;
import com.abrus.user.repository.UserRepository;
import com.abrus.user.repository.UserRepositoryImpl;
import com.abrus.user.service.UserService;
import com.abrus.user.service.UserServiceImpl;
import com.abrus.user.validator.UserRegistrationValidator;
import com.abrus.user.validator.UserRegistrationValidatorImpl;

import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        UserRegistrationValidator userRegistrationValidator = new UserRegistrationValidatorImpl(userRepository);
        UserService userService = new UserServiceImpl(userRepository, userRegistrationValidator);

        UserResponseDto newUser = userService.registerUser(
                new UserRegistrationDto("alice" + System.currentTimeMillis() + "@example.com", "AAbbcc78", "AAbbcc78", null)
        );
        System.out.println(newUser);

        List<UserResponseDto> users = userService.getUserList();
        System.out.println(users);

        Optional<UserResponseDto> user = userService.getUserById(1L);
        System.out.println(user);
    }
}
