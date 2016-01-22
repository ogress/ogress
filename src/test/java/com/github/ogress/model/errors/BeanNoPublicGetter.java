package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("bean_no_public_getter")
public class BeanNoPublicGetter {
    @OgressField("field")
    private String field;

    String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
