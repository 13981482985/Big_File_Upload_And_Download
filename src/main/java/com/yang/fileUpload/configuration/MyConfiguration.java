package com.yang.fileUpload.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class MyConfiguration {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors()*3,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(500),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()  // 由调用线程执行任务，降低速度
        );
    }
}
