package com.abrus.user.model;

public record UserRegistrationDto(String email, String password, String repeatPassword, String phoneNumber) {
}
