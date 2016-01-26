package com.github.ogress;

import com.github.ogress.serializer.OgressValueDeserializer;
import com.github.ogress.serializer.OgressValueSerializer;
import com.github.ogress.util.OgressUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class OgressFieldInfo {
    @NotNull
    public Field field;

    @NotNull
    public final String ogressFieldName;

    @NotNull
    public final OgressFieldAccessor accessor;

    public final boolean isReference;

    @Nullable
    public final OgressValueSerializer valueSerializer;

    @Nullable
    public final OgressValueDeserializer valueDeserializer;

    public OgressFieldInfo(@NotNull Field field, @NotNull String ogressFieldName, @NotNull OgressFieldAccessor accessor) {
        this.field = field;
        this.ogressFieldName = ogressFieldName;
        this.accessor = accessor;
        this.isReference = OgressUtils.isReferenceType(field.getType());
        valueSerializer = isReference ? null : OgressUtils.getValueSerializer(field.getType());
        valueDeserializer = isReference ? null : OgressUtils.getValueDeserializer(field.getType());
    }
}
