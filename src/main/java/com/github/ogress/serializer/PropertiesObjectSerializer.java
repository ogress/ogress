package com.github.ogress.serializer;

import com.github.ogress.OgressObjectSchemaRegistry;
import com.github.ogress.OgressObjectSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

public class PropertiesObjectSerializer implements OgressObjectSerializer {
    private static final String TYPE_NAME_PROP = "__type";
    private OgressObjectSchemaRegistry schemaRegistry;

    public PropertiesObjectSerializer(@NotNull OgressObjectSchemaRegistry schemaRegistry) {
        this.schemaRegistry = schemaRegistry;
    }

    @NotNull
    @Override
    public byte[] toRawData(@NotNull OgressObject ogressObject) {
        //noinspection MismatchedQueryAndUpdateOfCollection
        Properties p = new Properties();
        p.setProperty(TYPE_NAME_PROP, ogressObject.getOgressTypeName());

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
    public OgressObject fromRawData(@NotNull byte[] bytes) {
        return null;
    }
}