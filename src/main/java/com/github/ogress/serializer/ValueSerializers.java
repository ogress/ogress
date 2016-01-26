package com.github.ogress.serializer;

import java.util.HashMap;
import java.util.Map;

public class ValueSerializers {

    public static final Map<Class, OgressValueSerializer> FIELD_SERIALIZERS = new HashMap<Class, OgressValueSerializer>() {{
        //todo:
        put(Integer.class, val -> Integer.toString((Integer) val));
        put(Integer.TYPE, get(Integer.class));

    }};

    public static final Map<Class, OgressValueDeserializer> FIELD_DESERIALIZERS = new HashMap<Class, OgressValueDeserializer>() {{
        put(Integer.class, Integer::parseInt);
        put(Integer.TYPE, get(Integer.class));
    }};

}
