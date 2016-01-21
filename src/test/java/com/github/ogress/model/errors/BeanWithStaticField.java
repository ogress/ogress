package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("bean_with_static_field")
public class BeanWithStaticField {
    @OgressField("static_field")
    public static String someField;
}
