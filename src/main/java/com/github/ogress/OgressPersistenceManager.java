package com.github.ogress;

import org.jetbrains.annotations.NotNull;

public interface OgressPersistenceManager {

    @NotNull
    OgressObject getRoot();

    void setRoot(@NotNull OgressObject o);

    void flush();

    void cleanup();

    void close();
}
