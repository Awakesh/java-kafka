package co.lemnisk.consumer.async;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
	@Value("${thread.pool.size}")
	private int poolSize;

	@Value("${thread.pool.max.size}")
	private int poolMaxSize;

	@Value("${thread.queue.capacity}")
	private int queueCapacity;

	@Value("${thread.pool.name}")
	private String threadName;

	@Value("${rest.client.thread.pool.size}")
	private int restPoolSize;

	@Value("${rest.client.thread.pool.max.size}")
	private int restPoolMaxSize;

	@Value("${rest.client.thread.queue.capacity}")
	private int restQueueCapacity;

	@Value("${rest.client.thread.pool.name}")
	private String restThreadName;

	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(poolSize);
		executor.setMaxPoolSize(poolMaxSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix(threadName+"-");
		executor.initialize();
		return executor;
	}
	
	@Bean(name = "restClientExecutor")
	public Executor restClientExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(restPoolSize);
		executor.setMaxPoolSize(restPoolMaxSize);
		executor.setQueueCapacity(restQueueCapacity);
		executor.setThreadNamePrefix(restThreadName + "-");
		executor.initialize();
		return executor;
	}
}
