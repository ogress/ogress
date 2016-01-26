package com.github.ogress;

import com.github.ogress.serializer.OgressFieldSerializer;
import com.github.ogress.util.OgressUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class OgressFieldInfo {
    @NotNull
    public Field field;

    @NotNull
    public final String ogressFieldName;

    @NotNull
    public final OgressFieldAccessor accessor;

    public final boolean isReference;

    @NotNull
    public final OgressFieldSerializer serializer;

    public OgressFieldInfo(@NotNull Field field, @NotNull String ogressFieldName, @NotNull OgressFieldAccessor accessor) {
        this.field = field;
        this.ogressFieldName = ogressFieldName;
        this.accessor = accessor;
        this.isReference = OgressUtils.isReferenceType(field.getType());
        serializer = OgressUtils.getSerializer(field.getType());
    }
}
