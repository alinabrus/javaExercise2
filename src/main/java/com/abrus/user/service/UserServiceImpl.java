package com.abrus.user.service;

import com.abrus.user.exception.ValidationException;
import com.abrus.user.model.User;
import com.abrus.user.model.UserRegistrationDto;
import com.abrus.user.model.UserResponseDto;
import com.abrus.user.repository.UserRepository;
import com.abrus.user.validator.ValidationResult;
import com.abrus.user.validator.UserRegistrationValidator;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final UserRegistrationValidator userRegistrationValidator;

    public UserServiceImpl(UserRepository userRepository, UserRegistrationValidator userRegistrationValidator) {
        this.userRepository = userRepository;
        this.userRegistrationValidator = userRegistrationValidator;
    }

    @Override
    public UserResponseDto registerUser(UserRegistrationDto userRegistrationDto) {
        ValidationResult validationResult = userRegistrationValidator.validate(userRegistrationDto);
        if (!validationResult.getIsValid()) {
            throw new ValidationException(validationResult.getErrorMsg());
        }
        User user = new User();
        user.setEmail(userRegistrationDto.email());
        user.setPassword(userRegistrationDto.password());
        user.setPhoneNumber(userRegistrationDto.phoneNumber());

        User newUser = userRepository.insert(user);
        Optional<User> storedUser = userRepository.getById(newUser.getId());
        if (storedUser.isPresent()) {
            return new UserResponseDto(storedUser.get().getEmail(), storedUser.get().getPhoneNumber());
        }
        throw new RuntimeException("New user is not found in db: " + newUser.toString());
    }

    @Override
    public Optional<UserResponseDto> getUserById(Long userId) {
        Optional<User> storedUser = userRepository.getById(userId);
        if (storedUser.isPresent()) {
            User user = storedUser.get();
            return Optional.of(new UserResponseDto(user.getEmail(), user.getPhoneNumber()));
        }
        return Optional.empty();
    }

    @Override
    public List<UserResponseDto> getUserList() {
        List<UserResponseDto> users = new ArrayList<>();
        List<User> storedUsers = userRepository.getList();
        return storedUsers.stream()
                .map(user -> new UserResponseDto(user.getEmail(), user.getPhoneNumber()))
                .toList();
    }
}
