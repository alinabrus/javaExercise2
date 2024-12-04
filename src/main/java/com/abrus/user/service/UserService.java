package com.abrus.user.service;

import com.abrus.user.model.UserRegistrationDto;
import com.abrus.user.model.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDto registerUser(UserRegistrationDto userRegistrationDto);

    Optional<UserResponseDto> getUserById(Long userId);

    List<UserResponseDto> getUserList();
}
