package com.github.ogress.model.errors;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;
import com.github.ogress.model.SimpleBean;

@OgressType("bean_with_duplicate_field_name_with_parent")
public class BeanWithDuplicateFieldNameWithParent extends SimpleBean {
    @OgressField("name")
    public String anotherName;
}
