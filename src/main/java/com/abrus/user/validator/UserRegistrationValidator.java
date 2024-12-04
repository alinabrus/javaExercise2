package com.abrus.user.validator;

import com.abrus.user.model.UserRegistrationDto;

public interface UserRegistrationValidator {
    ValidationResult validate(UserRegistrationDto user);
}
