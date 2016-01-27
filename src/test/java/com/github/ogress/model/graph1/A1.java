package com.github.ogress.model.graph1;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

@OgressType("a1")
public class A1 {

    public static final String VALUE = "A1 VALUE LINE1 \r\n A1 VALUE LINE 2";

    @OgressField("b1")
    public B1 b1;

    @OgressField("value")
    public String value;

    public static A1 createGraph() {
        A1 a = new A1();
        a.value = VALUE;
        a.b1 = new B1();
        a.value = B1.VALUE;
        return a;
    }
}
