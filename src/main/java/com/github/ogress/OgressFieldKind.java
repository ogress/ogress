package com.github.ogress;

public enum OgressFieldKind {
    Value,
    Reference,
    ArrayOfValues,
    ArrayOfReferences,
    CollectionOfValues,
    CollectionOfReferences,
    MapOfValues,
    MapOfReferences;

    public boolean hasReferences() {
        return this == Reference || this == ArrayOfReferences || this == CollectionOfReferences || this == MapOfReferences;
    }
}
