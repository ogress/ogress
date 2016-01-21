package com.github.ogress;

import com.github.ogress.util.Check;
import com.github.ogress.util.EmptyOgressObject;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;

import static com.github.ogress.util.OgressUtils.getObjectTypeName;
import static com.github.ogress.util.OgressUtils.prepareObjectSchema;

//TODO: synchronization
public class OgressDbImpl implements OgressDb {
    @NotNull
    private final OgressObjectSchemaRegistry schemaRegistry = new OgressObjectSchemaRegistry();
    private IdentityHashMap<Object, OgressObjectInfo> objectsMap = new IdentityHashMap<>();

    @NotNull
    private Object rootObject = new EmptyOgressObject();

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

    }

    @Override
    public void cleanup() {

    }

    @Override
    public void close() {

    }

    private void attach(@NotNull Object o) {
        if (objectsMap.containsKey(o)) {
            return;
        }
        String typeName = getObjectTypeName(o.getClass());
        OgressObjectSchema schema = schemaRegistry.getByType(typeName);
        if (schema == null) {
            schema = prepareObjectSchema(o.getClass());
        }
        objectsMap.put(o, new OgressObjectInfo(schema, OgressConstants.ID_NOT_ASSIGNED));
    }


}
