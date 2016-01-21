package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("bean_with_final_field")
public class BeanWithFinalField {
    @OgressField("static_field")
    public final String someField = "";
}
