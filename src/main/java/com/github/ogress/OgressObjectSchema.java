package com.github.ogress;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public final class OgressObjectSchema {
    @NotNull
    public final String typeName;

    @NotNull
    public final Class<?> typeClass;

    @NotNull
    public final Map<String, OgressFieldInfo> fieldByOgressName;

    @NotNull
    public OgressFieldInfo[] fields;

    @NotNull
    public OgressFieldInfo[] referenceFields;

    public OgressObjectSchema(@NotNull String typeName, @NotNull Class<?> typeClass, @NotNull Map<String, OgressFieldInfo> fieldByOgressName) {
        this.typeName = typeName;
        this.typeClass = typeClass;
        this.fieldByOgressName = fieldByOgressName;
        this.fields = fieldByOgressName.values().toArray(new OgressFieldInfo[fieldByOgressName.size()]);
        this.referenceFields = Arrays.stream(fields).filter(f -> f.kind.hasReferences()).toArray(OgressFieldInfo[]::new);
    }
}
