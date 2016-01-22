package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("bean_no_public_getter")
public class BeanStaticAccessor {
    @OgressField("field")
    private String field;

    public static String getField() {
        return null;
    }

    void setField(String field) {
        this.field = field;
    }

}
