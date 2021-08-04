package com.jackinjava.qywx.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jie.li 2021/6/30 23:15
 */
@Configuration
@EnableAsync
public class AsyncExecutorConfiguration {
    @Value("${executors.threadPoolTaskExecutor.corePoolSize:12}")
    private Integer corePoolSize;

    @Value("${executors.threadPoolTaskExecutor.maxPoolSize:500}")
    private Integer maxPoolSize;

    @Value("${executors.threadPoolTaskExecutor.queueCapacity:500}")
    private Integer queueCapacity;

    @Value("${executors.threadPoolTaskExecutor.keepAliveSeconds:60}")
    private Integer keepAliveSeconds;

    @Value("${executors.threadPoolTaskExecutor.allowCoreThreadTimeOut:false}")
    private Boolean allowCoreThreadTimeOut;

    @Value("${executors.threadPoolTaskExecutor.threadNamePrefix:ThreadPoolTaskExecutor}")
    private String threadNamePrefix;

    @Value("${executors.threadPoolTaskExecutor.awaitTerminationSeconds:10}")
    private Integer awaitTerminationSeconds;

    @Value("${executors.threadPoolTaskExecutor.rejectedExecutionHandler:AbortPolicy}")
    private String rejectedExecutionHandler;

    @Bean("asyncTaskExecutor")
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数：线程池创建时候初始化的线程数
        executor.setCorePoolSize(corePoolSize);
        // 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(maxPoolSize);
        // 缓冲队列：用来缓冲执行任务的队列
        executor.setQueueCapacity(queueCapacity);
        // 允许线程的空闲时间60秒：当超过了核心线程之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 是否允许核心线程超时
        executor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
        //  线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix(threadNamePrefix);
        // 等待终止时间(秒)
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        /**
         * 线程池满了之后如何处理：默认是 new AbortPolicy();
         *
         * (1) ThreadPoolExecutor.AbortPolicy   处理程序遭到拒绝将抛出运行时RejectedExecutionException;
         * (2) ThreadPoolExecutor.CallerRunsPolicy 线程调用运行该任务的 execute 本身。此策略提供简单的反馈控制机制，能够减缓新任务的提交速度
         * (3) ThreadPoolExecutor.DiscardPolicy  不能执行的任务将被删除;
         * (4) ThreadPoolExecutor.DiscardOldestPolicy  如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）。
         */
        switch (rejectedExecutionHandler){
            case "AbortPolicy":
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
                break;
            case "CallerRunsPolicy":
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                break;
            case "DiscardPolicy":
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
                break;
            case "DiscardOldestPolicy":
                executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
                break;
        }
        executor.initialize();
        return executor;
    }

}
