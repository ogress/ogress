package com.github.ogress;

public enum OgressFieldKind {
    Value,
    Reference,
    ArrayOfValues,
    ArrayOfReferences,
    IterableOfValues,
    IterableOfReferences,
    MapOfValues,
    MapOfReferences;

    public boolean hasReferences() {
        return this == Reference || this == ArrayOfReferences || this == IterableOfReferences || this == MapOfReferences;
    }
}
