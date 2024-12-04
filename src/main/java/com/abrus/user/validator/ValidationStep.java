package com.abrus.user.validator;

public abstract class ValidationStep<T> {
    private ValidationStep<T> next;

    public ValidationStep<T> with(ValidationStep<T> step) {
        if (this.next == null) {
            this.next = step;
            return this;
        }
        ValidationStep<T> lastStep = this.next;
        while (lastStep.next != null) {
            lastStep = lastStep.next;
        }
        lastStep.next = step;
        return this;
    }

    public abstract ValidationResult validate(T toValidate);

    protected ValidationResult checkNext(T toValidate) {
        if (next == null) {
            return ValidationResult.valid();
        }
        return next.validate(toValidate);
    }
}
