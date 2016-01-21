package com.github.ogress.model;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("child_bean")
public class ComplexChildBean extends SimpleBean {

    @OgressField("public_field")
    public String publicField;

    @OgressField("default_access_field")
    String defaultAccessField;

    @OgressField("protected_field")
    protected String protectedField;

    @OgressField("private_field")
    private String privateField;

    public String transientField;
}
