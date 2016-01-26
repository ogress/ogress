package com.github.ogress.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OgressValueDeserializer<T> {
    @Nullable
    T fromString(@NotNull String val);
}

