package com.github.ogress.model;

import com.github.ogress.OgressField;
import com.github.ogress.OgressType;

import java.util.List;

@OgressType("rlb")
public class RefListBean {

    @OgressField("list")
    List<SimpleBean> field;

}
