package com.jackinjava.qywx.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class AccessToken extends AbstractResponse {
    @JsonAlias("access_token")
    private String accessToken;//获取到的凭证，最长为512字节

    @JsonAlias("expires_in")
    private int expiresIn;//凭证的有效时间（秒）
}
