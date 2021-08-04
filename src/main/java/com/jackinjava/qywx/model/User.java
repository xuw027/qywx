package com.jackinjava.qywx.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Data
public class User {
    private String userid;

    private String name;

    private int[] department;

    private String mobile;

    private String email;

    private int status; //激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return StringUtils.equals(mobile, user.mobile) ||
                StringUtils.equals(email.split("@")[0], user.email.split("@")[0]);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobile, email.split("@")[0]);
    }
}
