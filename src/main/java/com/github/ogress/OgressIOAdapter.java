package com.github.ogress;

import org.jetbrains.annotations.NotNull;

public interface OgressIOAdapter {
    @NotNull
    byte[] readObjectData(long objectId, @NotNull String typeName);

    void writeObjectData(long objectId, @NotNull byte[] data, @NotNull String typeName);

    void deleteObjectData(long objectId, @NotNull String typeName);

}
