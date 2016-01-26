package com.github.ogress;

import com.github.ogress.serializer.OgressValueDeserializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OgressObjectSerializer {
    @NotNull
    byte[] toRawData(@NotNull Object object);

    @Nullable
    Object fromRawData(@NotNull byte[] bytes);
}
