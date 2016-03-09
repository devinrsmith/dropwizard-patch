package io.progix.dropwizard.patch;

import io.dropwizard.validation.ConstraintViolations;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.exceptions.JsonPatchFailedException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;


/**
 * Created by dsmith on 3/9/16.
 */
public class ValidatedJsonPatcher<T> implements JsonPatcher<T> {
    private static final Class<?>[] DEFAULT_ARRAY_GROUP = new Class<?>[]{Default.class};

    @Valid
    private final JsonPatcher<T> delegate;

    private final Validator validator;
    private final Class<?>[] groups;

    public ValidatedJsonPatcher(JsonPatcher<T> delegate, Validator validator) {
        this(delegate, validator, DEFAULT_ARRAY_GROUP);
    }

    public ValidatedJsonPatcher(JsonPatcher<T> delegate, Validator validator, Class<?>[] groups) {
        this.delegate = delegate;
        this.validator = validator;
        this.groups = groups;
    }

    @Override
    public JsonPatchOperation[] getOperations() {
        return delegate.getOperations();
    }

    @Override
    public T patch(T current) throws JsonPatchFailedException {
        final T patched = delegate.patch(current);
        final Set<ConstraintViolation<T>> violations = validator.validate(patched, groups);
        if (violations != null && !violations.isEmpty()) {
            Set<ConstraintViolation<?>> constraintViolations = ConstraintViolations.copyOf(violations);
            throw new ConstraintViolationException("The request entity had the following errors:",
                    constraintViolations);
        }
        return patched;
    }

    @Override
    public JsonPatcher<T> validated(Validator validator) {
        return new ValidatedJsonPatcher<>(delegate, validator);
    }

    @Override
    public JsonPatcher<T> validated(Validator validator, Class<?>... groups) {
        return new ValidatedJsonPatcher<>(delegate, validator, groups);
    }
}
