package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("bean_incompatible_setter")
public class BeanIncompatibleSetter {
    @OgressField("field")
    private String field;

    public String getField() {
        return field;
    }

    public void setField(CharSequence v) {
        field = v.toString();
    }
}
