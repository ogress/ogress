package com.github.ogress.serializer;

import com.github.ogress.OgressDbImpl;
import com.github.ogress.OgressFieldInfo;
import com.github.ogress.OgressObjectInfo;
import com.github.ogress.OgressObjectSchema;
import com.github.ogress.OgressObjectSchemaRegistry;
import com.github.ogress.OgressObjectSerializer;
import com.github.ogress.util.Check;
import com.github.ogress.util.OgressUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public final class PropertiesObjectSerializer implements OgressObjectSerializer {
    private static final String TYPE_NAME_PROP = "__type";

    @NotNull
    private final OgressDbImpl db;
    @NotNull
    private final OgressObjectSchemaRegistry schemaRegistry;


    public PropertiesObjectSerializer(@NotNull OgressDbImpl db) {
        this.db = db;
        this.schemaRegistry = db.getObjectSchemaRegistry();
    }

    @NotNull
    @Override
    public byte[] toRawData(@NotNull Object object) {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Properties p = new Properties();
        OgressObjectSchema schema = schemaRegistry.getSchemaByObject(object);
        Check.notNull(schema, () -> "schema not found: " + object.getClass());
        p.setProperty(TYPE_NAME_PROP, schema.typeName);
        for (OgressFieldInfo field : schema.fields) {
            try {
                String value = serializeToString(field, object);
                p.setProperty(field.ogressFieldName, value);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os, true, "UTF-8");
            p.list(ps);
            ps.flush();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Nullable
    @Override
    public Object fromRawData(@NotNull byte[] bytes, @NotNull OgressFieldInfo fieldInfo) {
        String strValue = new String(bytes, OgressUtils.UTF8_CHARSET);

    }


    @Nullable
    private String serializeToString(@NotNull OgressFieldInfo field, @NotNull Object object) throws InvocationTargetException, IllegalAccessException {
        Object val = field.accessor.getValue(object);
        if (val == null) {
            return null;
        }
        if (field.isReference) {
            return db.serializeReference(val);
        }
        //noinspection unchecked
        return field.valueSerializer.toString(val);
    }

}
