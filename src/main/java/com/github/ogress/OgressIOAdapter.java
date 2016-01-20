package com.github.ogress;

import org.jetbrains.annotations.NotNull;

public interface OgressIOAdapter {
    @NotNull
    byte[] readObjectData(long objectId);

    void writeObjectData(long objectId, @NotNull byte[] data);
}
