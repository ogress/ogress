package com.github.ogress.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public interface OgressValueDeserializer<T> {
    @Nullable
    T deserialize(@NotNull Serializable val);
}

