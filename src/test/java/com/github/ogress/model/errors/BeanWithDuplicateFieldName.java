package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("bean_with_duplicate_field_name")
public class BeanWithDuplicateFieldName {
    @OgressField("field")
    public String someField1;

    @OgressField("field")
    public String someField2;
}
