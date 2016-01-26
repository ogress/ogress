package com.github.ogress;

import com.github.ogress.io.FileSystemIOAdapter;
import com.github.ogress.serializer.PropertiesObjectSerializer;
import com.github.ogress.util.Check;
import com.github.ogress.util.EmptyOgressObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Properties;
import java.util.function.BiConsumer;

import static com.github.ogress.util.OgressUtils.getObjectTypeName;
import static com.github.ogress.util.OgressUtils.prepareObjectSchema;

//TODO: synchronization
public class OgressDbImpl implements OgressDb {
    @NotNull
    private final OgressObjectSchemaRegistry schemaRegistry = new OgressObjectSchemaRegistry();

    //todo: fix sync issues.
    @NotNull
    private IdentityHashMap<Object, OgressObjectInfo> objectsMap = new IdentityHashMap<>();

    @NotNull
    private OgressObjectSerializer serializer;

    @NotNull
    private OgressIOAdapter ioAdapter;

    @NotNull
    private Object rootObject = new EmptyOgressObject();

    private static long traversalNumber = 0;

    private OgressDbImpl() {
        this.serializer = new PropertiesObjectSerializer(this);
        this.ioAdapter = new FileSystemIOAdapter();
    }

    @NotNull
    public static OgressDbImpl create(@NotNull Properties properties) {
        return new OgressDbImpl();
    }

    @Override
    public void registerType(Class<?>... types) {
        for (Class<?> t : types) {
            OgressObjectSchema schema = prepareObjectSchema(t);
            schemaRegistry.addSchema(schema);
        }
    }

    @NotNull
    @Override
    public Object getRoot() {
        return rootObject;
    }

    @Override
    public void setRoot(@NotNull Object o) {
        Check.notNull(o, () -> "Root object can't be null!");
        attach(o);
        rootObject = o;
    }

    @Override
    public void flush() {
        // first check that all graph is correct.
        visit(rootObject, ++traversalNumber, (o, info) -> {
            //do nothing, just check that graph has no errors.
        });

        // and flush next
        visit(rootObject, ++traversalNumber, (o, info) -> {
            byte[] data = serializer.toRawData(o);
            ioAdapter.writeObjectData(info.id, data, info.schema.typeName);
        });
    }

    private void visit(@NotNull Object o, @NotNull Object visitMark, @NotNull BiConsumer<Object, OgressObjectInfo> f) {
        OgressObjectInfo info = attach(o);
        if (info.visitMark == visitMark) {
            return;
        }
        f.accept(o, info);
        info.visitMark = visitMark;
        try {
            for (OgressFieldInfo i : info.schema.referenceFields) {
                Object ref = i.accessor.getValue(o);
                if (ref != null) {
                    visit(ref, visitMark, f);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanup() {
        //todo:
    }

    @NotNull
    private OgressObjectInfo attach(@NotNull Object o) {
        OgressObjectInfo info = getObjectInfo(o);
        if (info == null) {
            String typeName = getObjectTypeName(o.getClass());
            OgressObjectSchema schema = schemaRegistry.getSchemaByTypeName(typeName);
            Check.notNull(schema, () -> "Type is not registered: " + o.getClass());
            info = new OgressObjectInfo(schema, OgressConstants.ID_NOT_ASSIGNED);
            objectsMap.put(o, info);
        }
        return info;
    }

    @Nullable
    public OgressObjectInfo getObjectInfo(@NotNull Object o) {
        return objectsMap.get(o);
    }

    public OgressObjectSchemaRegistry getObjectSchemaRegistry() {
        return schemaRegistry;
    }

    @NotNull
    public String serializeReference(@NotNull Object object) {
        OgressObjectInfo objectInfo = getObjectInfo(object);
        Check.notNull(objectInfo, () -> "No OgressObjectInfo for object: " + object);
        return objectInfo.schema.typeName + ":" + objectInfo.id;
    }

    @NotNull
    public Object deserializeReference(@NotNull String val) {
        //todo:
        return null;
    }
}
