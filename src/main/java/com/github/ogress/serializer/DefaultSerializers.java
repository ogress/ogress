package com.github.ogress.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Default serializers & de-serializers uses string representation for values & references
 */

public class DefaultSerializers {

    public static final Map<Class, OgressValueSerializer> DEFAULT_VALUE_SERIALIZERS = new HashMap<Class, OgressValueSerializer>() {{
        put(Boolean.class, val -> Boolean.toString((Boolean) val));
        put(Boolean.TYPE, get(Boolean.class));

        put(Byte.class, val -> Byte.toString((Byte) val));
        put(Byte.TYPE, get(Byte.class));

        put(Character.class, val -> Character.toString((Character) val));
        put(Character.TYPE, get(Character.class));

        put(Double.class, val -> Double.toString((Double) val));
        put(Double.TYPE, get(Double.class));

        put(Float.class, val -> Float.toString((Float) val));
        put(Float.TYPE, get(Float.class));

        put(Float.class, val -> Float.toString((Float) val));
        put(Float.TYPE, get(Float.class));

        put(Integer.class, val -> Integer.toString((Integer) val));
        put(Integer.TYPE, get(Integer.class));

        put(Long.class, val -> Long.toString((Long) val));
        put(Long.TYPE, get(Long.class));

        put(Short.class, val -> Short.toString((Short) val));
        put(Short.TYPE, get(Short.class));

        put(String.class, val -> (String) val);

    }};

    public static final Map<Class, OgressValueDeserializer> DEFAULT_VALUE_DESERIALIZERS = new HashMap<Class, OgressValueDeserializer>() {{
        put(Boolean.class, (s) -> Boolean.parseBoolean(s.toString()));
        put(Boolean.TYPE, get(Boolean.class));

        put(Byte.class, (s) -> Byte.parseByte(s.toString()));
        put(Byte.TYPE, get(Byte.class));

        put(Character.class, (s) -> ((String) s).charAt(0));
        put(Character.TYPE, get(Character.class));

        put(Double.class, (s) -> Double.parseDouble((String) s));
        put(Double.TYPE, get(Double.class));

        put(Float.class, (s) -> Float.parseFloat((String) s));
        put(Float.TYPE, get(Float.class));

        put(Integer.class, (s) -> Integer.parseInt((String) s));
        put(Integer.TYPE, get(Integer.class));

        put(Long.class, (s) -> Long.parseLong((String) s));
        put(Long.TYPE, get(Long.class));

        put(Short.class, (s) -> Short.parseShort((String) s));
        put(Short.TYPE, get(Short.class));

        put(String.class, (s) -> s);
    }};

}
