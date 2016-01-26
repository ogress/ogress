package com.github.ogress.serializer;

import com.github.ogress.OgressObjectInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OgressFieldSerializer<T> {
    @Nullable
    String toString(@NotNull T val, @NotNull OgressObjectInfo info);
}

