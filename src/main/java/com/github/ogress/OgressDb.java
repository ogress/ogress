package com.github.ogress;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface OgressDb {

    @NotNull
    static OgressDb newInstance(@NotNull Map<String, Object> properties) {
        return OgressDbImpl.newInstance(properties);
    }

    void registerType(Class<?>... types);

    @NotNull
    Object getRoot();

    void setRoot(@NotNull Object o);

    void flush();

    void cleanup();

    @NotNull
    Map<String, Object> getProperties();
}
