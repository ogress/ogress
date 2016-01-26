package com.github.ogress.util;

import com.github.ogress.OgressField;
import com.github.ogress.OgressFieldAccessor;
import com.github.ogress.OgressFieldInfo;
import com.github.ogress.OgressObjectSchema;
import com.github.ogress.OgressType;
import com.github.ogress.serializer.OgressValueDeserializer;
import com.github.ogress.serializer.OgressValueSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.ogress.serializer.ValueSerializers.FIELD_DESERIALIZERS;
import static com.github.ogress.serializer.ValueSerializers.FIELD_SERIALIZERS;
import static java.util.stream.Collectors.toMap;

public class OgressUtils {

    public static Charset UTF8_CHARSET = Charset.forName("UTF-8");

    @NotNull
    public static String getObjectTypeName(@NotNull Class<?> cls) {
        checkOgressTypeAnnotationConsistency(cls);
        OgressType type = cls.getAnnotation(OgressType.class);
        Check.notNull(type, () -> "OgressType annotation is missed! Class: " + cls);
        String typeName = type.value();
        Check.notEmpty(typeName, () -> "OgressType.typeName is empty! Class: " + cls);
        return typeName;
    }

    private static void checkOgressTypeAnnotationConsistency(@NotNull Class cls) {
        if (cls  == Object.class) {
            return;
        }
        OgressType type = (OgressType) cls.getAnnotation(OgressType.class);
        if (type != null) {
            Check.isTrue(!cls.isInterface(), () -> "Interfaces are not allowed to have @OgressType annotation: " + cls);
            Check.isTrue(!Modifier.isAbstract(cls.getModifiers()), () -> "Abstract classes are not allowed to have @OgressType annotation: " + cls);
        }
        checkOgressTypeAnnotationConsistency(cls.getSuperclass());
        for (Class i : cls.getInterfaces()) {
            checkOgressTypeAnnotationConsistency(i);
        }
    }

    @NotNull
    public static OgressObjectSchema prepareObjectSchema(Class<?> cls) {
        String typeName = getObjectTypeName(cls);

        // process all annotations and store getters/setters for fieldByOgressName
        Map<String, Field> ogressFields = getOgressFields(cls);
        Map<String, OgressFieldInfo> fieldInfos = ogressFields.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> new OgressFieldInfo(e.getValue(), e.getKey(), getFieldAccessor(cls, e.getValue()))));

        return new OgressObjectSchema(typeName, cls, fieldInfos);
    }

    @NotNull
    public static OgressFieldAccessor getFieldAccessor(@NotNull Class<?> cls, @NotNull Field field) {
        int m = field.getModifiers();
        Check.isTrue(!Modifier.isFinal(m), () -> "Field is final: " + field);
        Check.isTrue(!Modifier.isStatic(m), () -> "Field is static: " + field);
        Check.isTrue(field.getDeclaringClass().isAssignableFrom(cls), () -> "Wrong field class: " + field + ", class: " + cls);
        if (Modifier.isPublic(m)) { // if field is public -> use direct accessor
            return new OgressFieldAccessor(field, null, null);
        }
        // search for getter & setter methods using naming convention
        Method getter = findGetterMethod(cls, field);
        Method setter = findSetterMethod(cls, field);
        Check.notNull(getter, () -> "No public access or getter for field: " + field);
        Check.notNull(setter, () -> "No public access or setter for field: " + field);
        return new OgressFieldAccessor(null, getter, setter);
    }

    @Nullable
    private static Method findGetterMethod(@NotNull Class<?> cls, @NotNull Field field) {
        Class<?> fieldType = field.getType();
        if (fieldType == Boolean.class || fieldType == Boolean.TYPE) {
            Method res = findPublicMethodByNameAndSignature(cls, "is" + capitalize(field.getName()), fieldType);
            if (res != null) {
                return res;
            }
        }
        return findPublicMethodByNameAndSignature(cls, "get" + capitalize(field.getName()), fieldType);
    }

    @Nullable
    public static Method findSetterMethod(@NotNull Class<?> cls, @NotNull Field field) {
        return findPublicMethodByNameAndSignature(cls, "set" + capitalize(field.getName()), null, field.getType());
    }

    @Nullable
    private static Method findPublicMethodByNameAndSignature(@NotNull Class<?> cls, @NotNull String methodName, @Nullable Class<?> returnType, Class<?>... paramTypes) {
        for (Method m : cls.getMethods()) {
            int modifiers = m.getModifiers();
            if (!Modifier.isStatic(modifiers)
                    && m.getName().equals(methodName)
                    && (m.getReturnType() == returnType || returnType == null)
                    && m.getParameterCount() == paramTypes.length
                    && Arrays.equals(paramTypes, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }

    @Contract("!null -> !null")
    private static String capitalize(@Nullable String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }


    @NotNull
    public static Map<String, Field> getOgressFields(Class<?> cls) {
        Map<String, Field> res = new HashMap<>();
        if (cls.getSuperclass() != Object.class) {
            res.putAll(getOgressFields(cls.getSuperclass()));
        }

        for (Field f : cls.getDeclaredFields()) {
            OgressField oField = f.getAnnotation(OgressField.class);
            if (oField == null) {
                continue;
            }
            int m = f.getModifiers();
            Check.isTrue(!Modifier.isStatic(m), () -> "Field is static: " + f);
            Check.isTrue(!Modifier.isFinal(m), () -> "Field is final: " + f);

            String oName = oField.value();
            Check.notEmpty(oName, () -> "OgressField::name is empty! Field: " + f);
            Check.isNull(res.get(oName), () -> "Duplicate field mapping! first: " + f + ", second : " + res.get(oName));
            res.put(oName, f);
        }
        return res;
    }

    public static boolean isReferenceType(@NotNull Class<?> type) {
        return !type.isPrimitive() && !type.isArray() && !List.class.isAssignableFrom(type);
    }

    @Nullable
    public static OgressValueSerializer getValueSerializer(@NotNull Class<?> type) {
        if (isReferenceType(type)) {
            return null;
        }
        OgressValueSerializer res = FIELD_SERIALIZERS.get(type);
        Check.notNull(res, () -> "Can't find valueSerializer for " + type);
        return res;
    }

    @Nullable
    public static OgressValueDeserializer getValueDeserializer(@NotNull Class<?> type) {
        if (isReferenceType(type)) {
            return null;
        }
        OgressValueDeserializer res = FIELD_DESERIALIZERS.get(type);
        Check.notNull(res, () -> "Can't find valueDeserializer for " + type);
        return res;
    }
}
