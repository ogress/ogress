package com.github.ogress.util;

import com.github.ogress.OgressField;
import com.github.ogress.OgressObjectSchema;
import com.github.ogress.OgressObjectSchema.FieldAccessor;
import com.github.ogress.OgressType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class OgressUtils {

    @NotNull
    public static String getObjectTypeName(@NotNull Class<?> cls) {
        checkOgressTypeAnnotationConsistency(cls);
        OgressType type = cls.getAnnotation(OgressType.class);
        Check.notNull(type, () -> "OgressType annotation is missed! Object: " + cls);
        String typeName = type.value();
        Check.notEmpty(typeName, () -> "OgressType::typeName is empty! Object: " + cls);
        return typeName;
    }

    private static void checkOgressTypeAnnotationConsistency(@NotNull Class cls) {
        OgressType type = (OgressType) cls.getAnnotation(OgressType.class);
        if (type != null) {
            Check.isTrue(!cls.isInterface(), () -> "Interfaces are not allowed to have @OgressType annotation: " + cls);
            Check.isTrue(!Modifier.isAbstract(cls.getModifiers()), () -> "Abstract classes are not allowed to have @OgressType annotation: " + cls);
        }
        if (cls.getSuperclass() != Object.class) {
            checkOgressTypeAnnotationConsistency(cls.getSuperclass());
        }
        for (Class i : cls.getInterfaces()) {
            checkOgressTypeAnnotationConsistency(i);
        }
    }

    @NotNull
    public static OgressObjectSchema prepareObjectSchema(Class<?> cls) {
        String typeName = getObjectTypeName(cls);

        // process all annotations and store getters/setters for fields
        Map<String, Field> ogressFields = getOgressFields(cls);
        Map<String, FieldAccessor> adapters = getFieldAccessors(cls, ogressFields);

        return new OgressObjectSchema(typeName, adapters);
    }

    @NotNull
    public static Map<String, FieldAccessor> getFieldAccessors(@NotNull Class<?> cls, @NotNull Map<String, Field> fieldMap) {
        Map<String, FieldAccessor> res = new HashMap<>();
        for (Map.Entry<String, Field> e : fieldMap.entrySet()) {
            // if field is public -> use direct accessor
            Field field = e.getValue();
            int m = field.getModifiers();
            if (Modifier.isPublic(m)) {
                res.put(e.getKey(), new FieldAccessor(field, null, null));
                continue;
            }
            // search for getter & setter methods using naming convention
            Method getter = findGetterMethod(cls, field);
            Method setter = findSetterMethod(cls, field);
            Check.notNull(getter, () -> "No public access or getter for field: " + field);
            Check.notNull(setter, () -> "No public access or setter for field: " + field);
        }
        return res;
    }

    @Nullable
    public static Method findGetterMethod(@NotNull Class<?> cls, @NotNull Field field) {
        Class<?> fieldClass = field.getType();
        if (fieldClass == Boolean.class || fieldClass == Boolean.TYPE) {
            Method res = findPublicMethodByNameAndSignature(cls, "is" + capitalize(field.getName()), fieldClass, 0);
            if (res != null) {
                return res;
            }
        }
        return findPublicMethodByNameAndSignature(cls, "get" + capitalize(field.getName()), fieldClass, 0);
    }

    @Nullable
    public static Method findSetterMethod(@NotNull Class<?> cls, @NotNull Field field) {
        return findPublicMethodByNameAndSignature(cls, "get" + capitalize(field.getName()), field.getType(), 0);
    }

    @Nullable
    private static Method findPublicMethodByNameAndSignature(Class<?> cls, @NotNull String methodName, Class<?> returnType, int numberOfParams) {
        for (Method m : cls.getMethods()) {
            if (m.getName().equals(methodName) && m.getReturnType() == returnType && m.getParameterCount() == numberOfParams) {
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

}
