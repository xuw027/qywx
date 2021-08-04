package com.jackinjava.qywx;

import com.jackinjava.qywx.service.UserDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;

@SpringBootTest
class QywxApplicationTests {
    @Autowired
    UserDetailService userDetailService;

    @Test
    void contextLoads() throws ExecutionException, InterruptedException {
        userDetailService.getUser();
    }

}
