package com.abrus.user.validator;

import com.abrus.user.model.UserRegistrationDto;
import com.abrus.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserRegistrationValidatorImpl implements UserRegistrationValidator {

    private final UserRepository userRepository;

    @Override
    public ValidationResult validate(UserRegistrationDto user) {
        return new EmailValidationStep()
                .with(new EmailDuplicationValidationStep(userRepository))
                .with(new PasswordValidationStep())
                .with(new PhoneValidationStep())
                .validate(user);
    }

    @AllArgsConstructor
    private static final class EmailValidationStep extends ValidationStep<UserRegistrationDto> {
        @Override
        public ValidationResult validate(UserRegistrationDto user) {
            String email = user.email();
            if (email == null || email.equals("")) {
                return new ValidationResult(false, "Email is required");
            }
            if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                return new ValidationResult(false, "Email is not valid");
            }
            return checkNext(user);
        }
    }

    @AllArgsConstructor
    private static final class EmailDuplicationValidationStep extends ValidationStep<UserRegistrationDto> {

        private final UserRepository userRepository;

        @Override
        public ValidationResult validate(UserRegistrationDto user) {
            if (userRepository.getByEmail(user.email()).isPresent()) {
                return ValidationResult.invalid(String.format("Email [%s] is already used", user.email()));
            }
            return checkNext(user);
        }
    }

    @AllArgsConstructor
    private static final class PhoneValidationStep extends ValidationStep<UserRegistrationDto> {
        @Override
        public ValidationResult validate(UserRegistrationDto user) {
            String phone = user.phoneNumber();
            if (phone != null && !phone.matches("^(\\d{3}[- .]?){2}\\d{4}$")) {
                return new ValidationResult(false, "Phone number is not valid");
            }
            return checkNext(user);
        }
    }

    @AllArgsConstructor
    private static final class PasswordValidationStep extends ValidationStep<UserRegistrationDto> {

        private final int minLength = 8;
        private final int maxLength = 20;
        private final boolean specCharNeeded = false;

        @Override
        public ValidationResult validate(UserRegistrationDto user) {
            String password = user.password();
            String repeatPassword = user.repeatPassword();
            if (password == null) {
                return new ValidationResult(false, "Password is required");
            }
            if (password.length() < minLength || password.length() > maxLength) {
                return new ValidationResult(false, "Password length must be not less than 8 and not more than 20 characters.");
            }
            if (!password.equals(repeatPassword)) {
                return new ValidationResult(false, "Password and repeat password must match.");
            }
            String oneDigit = "(?=.*[0-9])";
            String lowerCase = "(?=.*[a-z])";
            String upperCase = "(?=.*[A-Z])";
            String specChar = specCharNeeded ? "(?=.*[@#$%^&+=])" : "";
            String noSpace = "(?=\\S+$)";
            String minMaxChar = ".{" + minLength + "," + maxLength + "}";
            String pattern = oneDigit + lowerCase + upperCase + specChar + noSpace + minMaxChar;
            if (!password.matches(pattern)) {
                return new ValidationResult(false, "Password must contain lowercase and uppercase letters and numbers. ");
            }
            return checkNext(user);
        }
    }
}
