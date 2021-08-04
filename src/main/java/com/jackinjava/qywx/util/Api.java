package com.jackinjava.qywx.util;

import com.jackinjava.qywx.model.AbstractResponse;
import com.jackinjava.qywx.model.AccessToken;
import com.jackinjava.qywx.model.User;
import com.jackinjava.qywx.model.UserResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public interface Api {
    String getApihost();

    String getCorpid();

    String getCorpsecret();

    RestTemplate getRestTemplate();

    default String getAccessToken() {
        String url = StringUtils.join(getApihost(), "/cgi-bin/gettoken?corpid=", getCorpid(), "&corpsecret=", getCorpsecret());
        AccessToken accessToken = getRestTemplate().getForObject(url, AccessToken.class);
        if (!AbstractResponse.OK.equals(accessToken.getErrmsg()))
            throw new RuntimeException("获取access_token错误！");
        return accessToken.getAccessToken();
    }

    default List<User> getUserDetail() {
        String url = StringUtils.join(getApihost(), "/cgi-bin/user/list?access_token=", getAccessToken(), "&fetch_child=1&department_id=1" );
        UserResponse response = getRestTemplate().getForObject(url, UserResponse.class);
        if (!AbstractResponse.OK.equals(response.getErrmsg()))
            throw new RuntimeException("获取部门成员详细信息错误！");
        return response.getUserlist();
    }
}
