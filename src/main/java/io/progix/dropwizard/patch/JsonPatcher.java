package io.progix.dropwizard.patch;

import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.exceptions.JsonPatchFailedException;

import javax.validation.Validator;

/**
 * Created by dsmith on 3/9/16.
 */
public interface JsonPatcher<T> {
    JsonPatchOperation[] getOperations();

    T patch(T current) throws JsonPatchFailedException;

    default JsonPatcher<T> validated(Validator validator) {
        return new ValidatedJsonPatcher<>(this, validator);
    }

    default JsonPatcher<T> validated(Validator validator, Class<?>... groups) {
        return new ValidatedJsonPatcher<>(this, validator, groups);
    }
}
