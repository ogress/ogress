package com.github.ogress.serializer;

import com.github.ogress.OgressDbImpl;
import com.github.ogress.OgressFieldInfo;
import com.github.ogress.OgressObjectSchema;
import com.github.ogress.OgressObjectSchemaRegistry;
import com.github.ogress.OgressObjectSerializer;
import com.github.ogress.util.Check;
import com.github.ogress.util.OgressUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

public final class PropertiesObjectSerializer implements OgressObjectSerializer {
    private static final String OBJECT_META_PROPERTY = "__object";

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
        Properties p = new Properties();
        OgressObjectSchema schema = schemaRegistry.getSchemaByObject(object);
        Check.notNull(schema, () -> "schema not found: " + object.getClass());
        p.setProperty(OBJECT_META_PROPERTY, db.serializeReference(this));
        for (OgressFieldInfo field : schema.fields) {
            String value = serializeToString(field, object);
            p.setProperty(field.ogressFieldName, value);
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os, true, "UTF-8");
            p.list(ps);
            ps.flush();
            return os.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to serialize properties for " + p.get(OBJECT_META_PROPERTY), e);
        }
    }


    @Nullable
    @Override
    public Object fromRawData(@NotNull byte[] bytes) {
        if (bytes.length == 0) {
            return null;
        }
        Properties p = new Properties();
        try {
            p.load(new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new IllegalStateException("Filed to parse object: " + new String(bytes, OgressUtils.UTF8_CHARSET));
        }
        String info = p.getProperty(OBJECT_META_PROPERTY);
        Object obj = db.deserializeReference(info);
        OgressObjectSchema schema = db.getObjectSchemaRegistry().getSchemaByObject(obj);
        Check.notNull(schema, () -> "Schema is not registered for type: " + info);
        for (String key : p.stringPropertyNames()) {
            OgressFieldInfo field = schema.fieldByOgressName.get(key);
            Check.notNull(field, () -> "No field info found: " + schema.typeName + "." + key);

            String strValue = p.getProperty(key);
            Object value;
            if (field.isReference) {
                value = db.deserializeReference(strValue);
            } else {
                //noinspection ConstantConditions
                value = field.valueDeserializer.fromString(strValue);
            }
            field.accessor.setValue(obj, value);
        }
        return obj;
    }


    @Nullable
    private String serializeToString(@NotNull OgressFieldInfo field, @NotNull Object object) {
        Object val = field.accessor.getValue(object);
        if (val == null) {
            return null;
        }
        if (field.isReference) {
            return db.serializeReference(val);
        }
        assert field.valueSerializer != null;
        //noinspection unchecked
        return field.valueSerializer.toString(val);
    }

}
