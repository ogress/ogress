package com.github.ogress.model.graph1;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("b1")
public class B1 {
    public static final String VALUE = "B1 VALUE LINE1 \n B1 VALUE LINE 2";

    @OgressField("value")
    public String value;
}
