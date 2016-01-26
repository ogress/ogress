package com.github.ogress.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OgressValueSerializer<T> {
    @Nullable
    String toString(@NotNull T val);
}

