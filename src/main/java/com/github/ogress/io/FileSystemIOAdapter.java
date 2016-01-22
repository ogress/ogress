package com.github.ogress.io;

import com.github.ogress.OgressIOAdapter;
import org.jetbrains.annotations.NotNull;

public class FileSystemIOAdapter implements OgressIOAdapter {
    @NotNull
    @Override
    public byte[] readObjectData(long objectId, @NotNull String typeName) {
        return new byte[0];
    }

    @Override
    public void writeObjectData(long objectId, @NotNull byte[] data, @NotNull String typeName) {

    }
}
