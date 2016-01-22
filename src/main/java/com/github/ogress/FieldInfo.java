package com.github.ogress;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class FieldInfo {
    @NotNull
    public Field field;

    @NotNull
    public final String ogressFieldName;

    @NotNull
    public final FieldAccessor accessor;

    public FieldInfo(@NotNull Field field, @NotNull String ogressFieldName, @NotNull FieldAccessor accessor) {
        this.field = field;
        this.ogressFieldName = ogressFieldName;
        this.accessor = accessor;
    }
}
