package com.jackinjava.qywx.service;

import com.google.common.collect.Lists;
import com.jackinjava.qywx.model.User;
import com.jackinjava.qywx.model.UserCompare;
import com.jackinjava.qywx.util.LocalApi;
import com.jackinjava.qywx.util.SaasApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class UserDetailService {
    @Autowired
    private LocalApi localApi;

    @Autowired
    private SaasApi saasApi;

    @Autowired
    @Qualifier(value = "asyncTaskExecutor")
    private ThreadPoolTaskExecutor executor;


    public List<UserCompare> getUser() throws ExecutionException, InterruptedException {
        CompletableFuture<List<User>> saasUsersFuture = CompletableFuture.supplyAsync(() -> saasApi.getUserDetail(), executor);
        CompletableFuture<List<User>> localUsersFuture = CompletableFuture.supplyAsync(() -> localApi.getUserDetail(), executor);
        log.info("开始等待所有任务执行完成");
        CompletableFuture<Void> all = CompletableFuture.allOf(saasUsersFuture, localUsersFuture);
        all.join();
        log.info("所有任务已经执行完成");
        List<User> saasUsers = saasUsersFuture.get();
        List<User> localUsers = localUsersFuture.get();
        log.info("SAAS企业微信共有 {} 个用户" , saasUsers.size());
        log.info("私有企业微信共有 {} 个用户" , localUsers.size());

        List<User> emailUsers = new ArrayList<>();//email的用户标识符即为私有版用户的userid
        List<User> mobileUsers = new ArrayList<>();//email不存在的则使用手机号码对比
        List<User> noneUser = new ArrayList<>();//saas用户中邮箱和电话都没有的用户

        saasUsers.forEach(su -> {
            if (StringUtils.isNotBlank(su.getEmail())) {
                emailUsers.add(su);
            } else if (StringUtils.isNotBlank(su.getMobile())) {
                mobileUsers.add(su);
            } else {
                noneUser.add(su);
            }
        });

        log.info("SAAS用户中email账号有：{}, 手机账号有：{}, 都不存在的有：{}", emailUsers.size(), mobileUsers.size(), noneUser.size());
        log.info("开始执行比对......");

        List<UserCompare> userCompares = new Vector<>(emailUsers.size());//所有saas用户的对比结果
        List<Integer> cacheIndex = new Vector<>(localUsers.size());//私有版用户可以匹配的下标

        List<List<User>> emailPart = Lists.partition(emailUsers, 1000);
        List<List<User>> mobilePart = Lists.partition(mobileUsers, 1000);
        CountDownLatch countDownLatch = new CountDownLatch(emailPart.size() + mobilePart.size());

        emailPart.forEach(list ->
                executor.execute(() -> compareEmail(list, localUsers, userCompares, cacheIndex, countDownLatch))
        );
        mobilePart.forEach(list ->
                executor.execute(() -> compareMobile(list, localUsers, userCompares, cacheIndex, countDownLatch))
        );
        countDownLatch.await();
        log.info("比对完成，结果有 {}", userCompares.size());

        noneUser.forEach(nu -> {
            userCompares.add(new UserCompare(nu, null, "SAAS版用户邮箱电话都不存在, 无法比对"));
        });

        for (int i=0;i<localUsers.size();i++) {
            if (!cacheIndex.contains(i))
                userCompares.add(new UserCompare(null, localUsers.get(i), "私有版存在用户信息，SAAS版无用户信息"));
        }

        return userCompares;
    }


    private void compareEmail(List<User> saasUsers, List<User> localUsers, List<UserCompare> compares, List<Integer> cacheIndex, CountDownLatch countDownLatch) {
        long start = System.currentTimeMillis();
        saasUsers.forEach(su -> {
            boolean flag = true;
            for (int i=0;i<localUsers.size();i++) {
                User lu = localUsers.get(i);
                if (StringUtils.equals(su.getEmail().split("@")[0], lu.getUserid())) {
                    compares.add(new UserCompare(su, lu, "邮箱匹配"));
                    cacheIndex.add(i);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                compares.add(new UserCompare(su, null, "SAAS版有邮箱，私有版不存在用户"));
            }
        });
        countDownLatch.countDown();
        log.info("线程[{}]执行完毕：Email 耗时：{} ms", Thread.currentThread().getName(), System.currentTimeMillis()-start);
    }


    private void compareMobile(List<User> saasUsers, List<User> localUsers, List<UserCompare> compares, List<Integer> cacheIndex, CountDownLatch countDownLatch) {
        long start = System.currentTimeMillis();
        saasUsers.forEach(su -> {
            boolean flag = true;
            for (int i=0;i<localUsers.size();i++) {
                User lu = localUsers.get(i);
                if (StringUtils.equals(su.getMobile(), lu.getMobile())) {
                    compares.add(new UserCompare(su, lu, "电话匹配"));
                    cacheIndex.add(i);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                compares.add(new UserCompare(su, null, "SAAS版无邮箱，有电话号码，私有版不存在用户"));
            }
        });
        countDownLatch.countDown();
        log.info("线程[{}]执行完毕：mobile 耗时：{} ms", Thread.currentThread().getName(), System.currentTimeMillis()-start);
    }
}
