package com.github.ogress;

import com.github.ogress.io.StubIOAdapter;
import com.github.ogress.serializer.PropertiesObjectSerializer;
import com.github.ogress.util.Check;
import com.github.ogress.util.EmptyOgressObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
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
    private Object rootObject = EmptyOgressObject.INSTANCE;

    private static long traversalNumber = 0;

    @NotNull
    private final Map<String, Object> properties;

    private OgressDbImpl(@NotNull Map<String, Object> properties) {
        this.properties = properties;
        this.serializer = new PropertiesObjectSerializer(this);
        this.ioAdapter = new StubIOAdapter(this);
    }

    @NotNull
    public static OgressDbImpl newInstance(@NotNull Map<String, Object> properties) {
        //todo: parse properties for IO adapter & serializer instances.
        OgressDbImpl db = new OgressDbImpl(properties);
        byte[] rootData = db.ioAdapter.readObjectData(0L, "root");
        Object root = db.serializer.fromRawData(rootData);
        if (root != null) {
            db.setRoot(root);
        }
        return db;
    }

    @Override
    public synchronized void registerType(Class<?>... types) {
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
    public synchronized void setRoot(@NotNull Object o) {
        Check.notNull(o, () -> "Root object can't be null!");
        attach(o);
        rootObject = o;
    }

    @Override
    public synchronized void flush() {
        if (rootObject == EmptyOgressObject.INSTANCE) {
            return;
        }
        // first check that all graph is correct.
        checkAndAttachGraph();

        // and flush next
        visit(rootObject, ++traversalNumber, (o, info) -> {
            byte[] data = serializer.toRawData(o);
            ioAdapter.writeObjectData(info.id, data, info.schema.typeName);
        });

        List<Map.Entry<Object, OgressObjectInfo>> values = new ArrayList<>(objectsMap.entrySet());
        for (Map.Entry<Object, OgressObjectInfo> entry : values) {
            OgressObjectInfo info = entry.getValue();
            if (info.traversalNumber != traversalNumber) {
                ioAdapter.deleteObjectData(info.id, info.schema.typeName);
                objectsMap.remove(entry.getKey());
            }
        }
    }

    protected synchronized void checkAndAttachGraph() {
        visit(rootObject, ++traversalNumber, (o, info) -> {
            //do nothing, just check that graph has no errors.
        });
    }

    private void visit(@NotNull Object o, long traversalNumber, @NotNull BiConsumer<Object, OgressObjectInfo> f) {
        OgressObjectInfo info = attach(o);
        if (info.traversalNumber == traversalNumber) {
            return;
        }
        info.traversalNumber = traversalNumber;
        f.accept(o, info);
        for (OgressFieldInfo i : info.schema.referenceFields) {
            Object ref = i.accessor.getValue(o);
            if (ref != null) {
                visit(ref, traversalNumber, f);
            }
        }
    }

    @Override
    public void cleanup() {
        //todo:
    }

    @NotNull
    @Override
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
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
