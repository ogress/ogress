package com.github.ogress;

import org.jetbrains.annotations.NotNull;

public final class OgressObjectInfo {
    @NotNull
    public final OgressObjectSchema schema;
    public final long id;
    public Object visitMark;

    public OgressObjectInfo(@NotNull OgressObjectSchema schema, long id) {
        this.schema = schema;
        this.id = id;
    }
}
