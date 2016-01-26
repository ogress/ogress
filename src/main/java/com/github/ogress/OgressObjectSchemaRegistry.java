package com.github.ogress;

import com.github.ogress.util.Check;
import com.github.ogress.util.OgressUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

public class OgressObjectSchemaRegistry {

    private final ConcurrentHashMap<String, OgressObjectSchema> schemaByType = new ConcurrentHashMap<>();

    @Nullable
    public OgressObjectSchema getSchemaByTypeName(@NotNull String typeName) {
        return schemaByType.get(typeName);
    }

    @Nullable
    public OgressObjectSchema getSchemaByObject(@NotNull Object obj) {
        return getSchemaByClass(obj.getClass());
    }

    @Nullable
    public OgressObjectSchema getSchemaByClass(@NotNull Class<?> cls) {
        String typeName = OgressUtils.getObjectTypeName(cls);
        return getSchemaByTypeName(typeName);
    }

    protected void addSchema(@NotNull OgressObjectSchema schema) {
        Check.isTrue(!schemaByType.containsKey(schema.typeName), () -> "Schema is already registered: " + schema.typeName);
        schemaByType.put(schema.typeName, schema);
    }

}
