package com.github.ogress;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OgressObjectSerializer {
    @NotNull
    byte[] toRawData(@NotNull OgressObject object);

    @Nullable
    OgressObject fromRawData(@NotNull byte[] data);
}
