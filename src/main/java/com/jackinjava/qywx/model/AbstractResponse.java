package com.jackinjava.qywx.model;

import lombok.Data;

@Data
public abstract class AbstractResponse {
    private int errcode;

    private String errmsg;

    public static final String OK = "ok";
    public static final int OK_CODE = 0;
}
