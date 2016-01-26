package com.github.ogress.serializer;

public class ValueSerializers {
    public static final OgressValueSerializer<Integer> INTEGER_FIELD_SERIALIZER = val -> Integer.toString(val);
}
