package com.github.ogress;

import java.util.Map;

public final class OgressObjectSchema {
    public final String typeName;
    public final Map<String, FieldAccessor> fields;

    public OgressObjectSchema(String typeName, Map<String, FieldAccessor> fields) {
        this.typeName = typeName;
        this.fields = fields;
    }

}
