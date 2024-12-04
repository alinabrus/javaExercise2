package com.abrus.user.validator;

import lombok.Value;

@Value
public class ValidationResult {
    @SuppressWarnings("checkstyle:VisibilityModifier")
    boolean isValid;
    @SuppressWarnings("checkstyle:VisibilityModifier")
    String errorMsg;

    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult invalid(String errorMsg) {
        return new ValidationResult(false, errorMsg);
    }

    public boolean getIsValid() {
        return isValid;
    }
}
