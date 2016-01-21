package com.github.ogress.util;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Check {

    @Contract("!null,_ -> fail")
    public static void isNull(@Nullable Object o, @NotNull LazyValue<String> message) {
        isTrue(o == null, message);
    }


    @Contract("null,_ -> fail")
    public static void notNull(@Nullable Object o, @NotNull LazyValue<String> message) {
        isTrue(o != null, message);
    }

    @Contract("null,_ -> fail")
    public static void notEmpty(@Nullable String s, @NotNull LazyValue<String> message) {
        isTrue(s != null && !s.isEmpty(), message);
    }

    @Contract("false,_ -> fail")
    public static void isTrue(boolean v, @NotNull LazyValue<String> message) {
        if (!v) {
            throw new IllegalArgumentException(message.get());
        }
    }
}
