package com.github.ogress.model;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("parent_bean_with_fields")
public class ParentBeanWithFields {
    @OgressField("public_parent_field")
    public String publicParentField;

    @OgressField("private_parent_field")
    private String privateParentField;

    public String getPrivateParentField() {
        return privateParentField;
    }

    public void setPrivateParentField(String v) {
        this.privateParentField = v;
    }
}
