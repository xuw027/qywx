package com.jackinjava.qywx.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Data
@Component
public class SaasApi implements Api {
    @Value("${wx.saas.apihost}")
    private String apihost;

    @Value("${wx.saas.corpid}")
    private String corpid;

    @Value("${wx.saas.corpsecret}")
    private String corpsecret;

    @Autowired
    private RestTemplate restTemplate;
}
