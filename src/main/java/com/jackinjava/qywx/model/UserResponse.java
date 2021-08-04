package com.jackinjava.qywx.model;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse extends AbstractResponse {
    private List<User> userlist;
}
