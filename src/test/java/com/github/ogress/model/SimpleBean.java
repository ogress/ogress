package com.github.ogress.model;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("simple_bean")
public class SimpleBean {

    @OgressField("name")
    public String name;

    public String transientField;
}
