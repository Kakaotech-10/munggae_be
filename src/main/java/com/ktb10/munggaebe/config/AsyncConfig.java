package com.ktb10.munggaebe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "notificationAsyncExecutor")
    public TaskExecutor notificationAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);            // 기본 스레드 개수
        executor.setMaxPoolSize(10);            // 최대 스레드 개수
        executor.setQueueCapacity(25);          // 대기 작업 큐 크기
        executor.setKeepAliveSeconds(60);       // 유휴 스레드 유지 시간 (초)
        executor.setThreadNamePrefix("NotificationAsync-"); // 스레드 이름 접두사
        executor.initialize();                  // ThreadPool 초기화
        return executor;
    }
}
