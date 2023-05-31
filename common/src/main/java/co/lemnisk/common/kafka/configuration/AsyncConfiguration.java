package co.lemnisk.common.kafka.configuration;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
public class AsyncConfiguration {
	@Value("${common.thread.pool.size}")
	private int poolSize;
	
	@Value("${common.thread.pool.max.size}")
	private int poolMaxSize;

    @Value("${common.thread.queue.capacity}")
	private int queueCapacity;
    
    @Value("${common.thread.pool.name}")
	private String threadName;
	  
	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(poolSize);
		executor.setMaxPoolSize(poolMaxSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix(threadName + "-");
		executor.initialize();
		return executor;
	}
}