package com.github.ogress.io;

import com.github.ogress.OgressDb;
import com.github.ogress.OgressIOAdapter;
import org.jetbrains.annotations.NotNull;

public class StubIOAdapter implements OgressIOAdapter {
    public StubIOAdapter(@NotNull OgressDb db) {
    }

    @NotNull
    @Override
    public byte[] readObjectData(long objectId, @NotNull String typeName) {
        return new byte[0];
    }

    @Override
    public void writeObjectData(long objectId, @NotNull byte[] data, @NotNull String typeName) {
    }

    @Override
    public void deleteObjectData(long objectId, @NotNull String typeName) {
    }
}
