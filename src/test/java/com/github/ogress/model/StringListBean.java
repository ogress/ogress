package com.github.ogress.model;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

import java.util.List;

@OgressType("slb")
public class StringListBean {

    @OgressField("list")
    List<String> field;

}
