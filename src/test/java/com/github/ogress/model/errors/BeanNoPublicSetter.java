package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("bean_no_public_getter")
public class BeanNoPublicSetter {
    @OgressField("field")
    private String field;

    public String getField() {
        return field;
    }
}
