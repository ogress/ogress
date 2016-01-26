package com.github.ogress;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OgressFieldAccessor {
    @Nullable
    public final Field field;
    @Nullable
    public final Method getter;
    @Nullable
    public final Method setter;

    public OgressFieldAccessor(@Nullable Field field, @Nullable Method getter, @Nullable Method setter) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }

    public Object getValue(@NotNull Object o) throws IllegalAccessException, InvocationTargetException {
        if (field != null) {
            return field.get(o);
        }
        assert getter != null;
        return getter.invoke(o);
    }
}
