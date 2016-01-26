package com.github.ogress;

import org.jetbrains.annotations.NotNull;

public interface OgressDb {

    void registerType(Class<?> ... types);

    @NotNull
    Object getRoot();

    void setRoot(@NotNull Object o);

    void flush();

    void cleanup();
}
