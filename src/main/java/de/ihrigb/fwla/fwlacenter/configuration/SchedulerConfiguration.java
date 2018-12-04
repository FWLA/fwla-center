package de.ihrigb.fwla.fwlacenter.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(SchedulerProperties.class)
@RequiredArgsConstructor
public class SchedulerConfiguration implements SchedulingConfigurer {

	private SchedulerProperties properties;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(properties.getPoolSize());
		threadPoolTaskScheduler.setThreadNamePrefix("scheduled-");
		threadPoolTaskScheduler.initialize();

		taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}
}
