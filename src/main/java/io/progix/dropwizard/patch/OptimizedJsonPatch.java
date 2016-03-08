/*
 * Copyright 2014 Tariq Bugrara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.progix.dropwizard.patch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.progix.jackson.JsonPatch;
import io.progix.jackson.JsonPatchOperation;
import io.progix.jackson.exceptions.JsonPatchTestFailedException;

public class OptimizedJsonPatch<T> {

    protected JsonPatchOperation[] operations;

    @JsonIgnore
    protected final ObjectMapper mapper;

    /**
     * Constructs an instance using a list of {@link JsonPatchOperation}
     *
     * @param operations A list of {@link JsonPatchOperation}
     * @param mapper
     */
    public OptimizedJsonPatch(JsonPatchOperation[] operations, ObjectMapper mapper) {
        this.operations = operations;
        this.mapper = mapper;
    }

    public JsonPatchOperation[] getOperations() {
        return operations;
    }

    public T apply(T context) throws JsonPatchTestFailedException {
        final Class<T> typeClass = (Class<T>) context.getClass();

        JsonNode node = mapper.convertValue(context, JsonNode.class);

        // todo note:
        // there is a deep copy that is happening in each op at JsonPatchUtil
        // we should eventually make some that are unsafe to avoid the unnecessary copying
        node = JsonPatch.apply(operations, node);

        // note: even if there are no operations, we are still getting a new copy of the object (as desired)
        return mapper.convertValue(node, typeClass);
    }
}
