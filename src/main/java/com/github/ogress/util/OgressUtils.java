package com.github.ogress.util;

import com.github.ogress.OgressField;
import com.github.ogress.OgressObjectSchema;
import com.github.ogress.OgressType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class OgressUtils {

    @NotNull
    public static String getObjectTypeName(@NotNull Object o) {
        Class<?> cls = o.getClass();
        checkOgressTypeAnnotationConsistency(cls);
        OgressType type = cls.getAnnotation(OgressType.class);
        Check.notNull(type, () -> "OgressType annotation is missed! Object: " + o);
        String typeName = type.value();
        Check.notEmpty(typeName, () -> "OgressType::typeName is empty! Object: " + o);
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
    public static OgressObjectSchema prepareObjectSchema(Object o) {
        String typeName = getObjectTypeName(o);

        // process all annotations and store getters/setters for fields
        Map<String, Field> ogressFields = getAllOgressFields(o.getClass());
        //todo: bind fields to accessors/getters/setters
        Map<String, OgressObjectSchema.FieldAdapter> adapters = new HashMap<>();

        return new OgressObjectSchema(typeName, adapters);
    }

    @NotNull
    public static Map<String, Field> getAllOgressFields(Class<?> cls) {
        Map<String, Field> res = new HashMap<>();
        if (cls.getSuperclass() != Object.class) {
            res.putAll(getAllOgressFields(cls.getSuperclass()));
        }

        for (Field f : cls.getDeclaredFields()) {
            OgressField oField = f.getAnnotation(OgressField.class);
            if (oField == null) {
                continue;
            }
            int m = f.getModifiers();
            Check.isTrue(!Modifier.isStatic(m), () -> "Field is static: " + f);

            String oName = oField.value();
            Check.notNull(oName, () -> "OgressField::name is empty! Field: " + f);
            Check.isNull(res.get(oName), () -> "Duplicate field mapping! first: " + f + ", second : " + res.get(oName));
            res.put(oName, f);
        }
        return res;
    }


}
