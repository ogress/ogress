package com.github.ogress;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

public class OgressObjectSchemaRegistry {

    private final ConcurrentHashMap<String, OgressObjectSchema> registry = new ConcurrentHashMap<>();

    @Nullable
    public OgressObjectSchema getByType(@NotNull String type) {
        return registry.get(type);
    }

}
