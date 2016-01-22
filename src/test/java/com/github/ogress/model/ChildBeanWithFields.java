package com.github.ogress.model;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("child_bean_with_fields")
public class ChildBeanWithFields extends ParentBeanWithFields {
    @OgressField("public_child_field")
    public String publicChildField;

    @OgressField("private_child_field")
    private String privateChildField;

    @OgressField("private_child_field")
    private String privateChildField2;

    @OgressField("private_child_field_boolean")
    private boolean privateChildFieldBoolean;

    public String getPrivateChildField() {
        return privateChildField;
    }

    public void setPrivateChildField(String v) {
        this.privateChildField = v;
    }

    public String getPrivateChildField2() {
        return privateChildField2;
    }

    public ChildBeanWithFields setPrivateChildField2(String v) {
        this.privateChildField2 = v;
        return this;
    }

    public boolean isPrivateChildFieldBoolean() {
        return privateChildFieldBoolean;
    }

    public void setPrivateChildFieldBoolean(boolean privateChildFieldBoolean) {
        this.privateChildFieldBoolean = privateChildFieldBoolean;
    }
}
