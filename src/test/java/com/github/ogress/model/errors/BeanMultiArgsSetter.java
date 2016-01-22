package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("bean_multi_args_setter")
public class BeanMultiArgsSetter {
    @OgressField("field")
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String v, int x) {
        field = v + x;
    }
}
