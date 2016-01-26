package com.github.ogress.serializer;

public class DefaultSerializers {
    public static final OgressFieldSerializer<Integer> INTEGER_FIELD_SERIALIZER = (val, info) -> Integer.toString(val);
    public static final OgressFieldSerializer<Object> REFERENCE_FIELD_SERIALIZER = (val, info) -> info.schema.typeName + ":" + info.id;

}
