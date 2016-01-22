package com.github.ogress;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldAccessor {
    @Nullable
    public final Field field;
    @Nullable
    public final Method getter;
    @Nullable
    public final Method setter;

    public FieldAccessor(@Nullable Field field, @Nullable Method getter, @Nullable Method setter) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }
}
