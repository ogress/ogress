package com.github.ogress.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public interface OgressValueSerializer<T> {
    @Nullable
    Serializable serialize(@NotNull T val);
}

