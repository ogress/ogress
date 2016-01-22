package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("bean_multi_args_setter")
public class BeanMultiArgsGetter {
    @OgressField("field")
    private String field;

    public void getField(String v, int x) {
        field = v + x;
    }

    public void setField(String field) {
        this.field = field;
    }

}
