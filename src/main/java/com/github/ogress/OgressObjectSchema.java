package com.github.ogress;

import com.github.ogress.util.OgressUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public final class OgressObjectSchema {
    @NotNull
    public final String typeName;
    @NotNull
    public final Map<String, FieldInfo> fieldByOgressName;
    @NotNull
    public FieldInfo[] fields;
    @NotNull
    public FieldInfo[] referenceFields;

    public OgressObjectSchema(@NotNull String typeName, @NotNull Map<String, FieldInfo> fieldByOgressName) {
        this.typeName = typeName;
        this.fieldByOgressName = fieldByOgressName;
        this.fields = fieldByOgressName.values().toArray(new FieldInfo[fieldByOgressName.size()]);
        this.referenceFields = Arrays.stream(fields).filter(OgressUtils::isReferenceType).toArray(FieldInfo[]::new);
    }


}
