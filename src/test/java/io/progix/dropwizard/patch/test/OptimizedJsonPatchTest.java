package io.progix.dropwizard.patch.test;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.progix.dropwizard.patch.JsonPatchDeserializerHelper;
import io.progix.dropwizard.patch.OptimizedJsonPatch;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.JsonPatchOperationType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class OptimizedJsonPatchTest {

    OptimizedJsonPatch<Object> patch;

    @Before
    public void init() {
        ObjectMapper mapper = Jackson.newObjectMapper();
        JsonPatchDeserializerHelper.register(mapper);

        JsonPatchOperation add = new JsonPatchOperation(JsonPatchOperationType.ADD,
                JsonPointer.compile("/"), mapper.convertValue("test", JsonNode.class));

        patch = new OptimizedJsonPatch<>(new JsonPatchOperation[] { add }, mapper);
    }

    @Test
    public void testOverride() {
        //TODO
    }
}
