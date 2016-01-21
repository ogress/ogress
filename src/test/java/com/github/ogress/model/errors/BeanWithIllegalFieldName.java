package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("illegal_field_names")
public class BeanWithIllegalFieldName {
    @OgressField("")
    public String someField;
}
