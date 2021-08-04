package com.jackinjava.qywx.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Data
@Component
public class LocalApi implements Api {
    @Value("${wx.local.apihost}")
    private String apihost;

    @Value("${wx.local.corpid}")
    private String corpid;

    @Value("${wx.local.corpsecret}")
    private String corpsecret;

    @Autowired
    private RestTemplate restTemplate;

}
