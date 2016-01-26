package com.github.ogress;

import com.github.ogress.io.FileSystemIOAdapter;
import com.github.ogress.serializer.PropertiesObjectSerializer;
import com.github.ogress.util.Check;
import com.github.ogress.util.EmptyOgressObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
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

    //todo: clean after startup
    @NotNull
    private final Map<Long, Object> startupMap = new HashMap<>();

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
    public static OgressDbImpl newInstance(@NotNull Properties properties) {
        //todo: parse properties for IO adapter & serializer instances.
        OgressDbImpl db = new OgressDbImpl();
        byte[] rootData = db.ioAdapter.readObjectData(0L, "root");
        Object root = db.serializer.fromRawData(rootData);
        //todo:
        db.setRoot(root);
        return db;
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
        for (OgressFieldInfo i : info.schema.referenceFields) {
            Object ref = i.accessor.getValue(o);
            if (ref != null) {
                visit(ref, visitMark, f);
            }
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
        int tSep = val.indexOf(':');
        Check.isTrue(tSep > 0, () -> "Illegal serialized form of reference: " + val);
        String typeName = val.substring(0, tSep);
        OgressObjectSchema schema = schemaRegistry.getSchemaByTypeName(typeName);
        Check.notNull(schema, () -> "Type is not registered: " + typeName);
        long id = Long.parseLong(val.substring(tSep + 1));

        Object res = startupMap.get(id);
        if (res != null) {
            OgressObjectSchema schema2 = schemaRegistry.getSchemaByTypeName(typeName);
            Check.isTrue(schema == schema2, () -> "Duplicate schema for type: " + typeName + ", Oid:" + id);
            return res;
        }
        try {
            res = schema.typeClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Exception in  " + schema.typeClass + " constructor!", e);
        }
        attach(res);
        startupMap.put(id, res);
        return res;
    }
}
