package com.enjoydelivery.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfiguration {

  private static final int THREAD_SIZE = 10;
  private static final int QUEUE_SIZE = 5;

  @Bean
  public Executor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor taskExecutor
        = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(THREAD_SIZE);
    taskExecutor.setMaxPoolSize(THREAD_SIZE);
    taskExecutor.setQueueCapacity(QUEUE_SIZE);

    taskExecutor.setRejectedExecutionHandler(new AbortPolicy());
    taskExecutor.initialize();
    return taskExecutor;
  }

}
