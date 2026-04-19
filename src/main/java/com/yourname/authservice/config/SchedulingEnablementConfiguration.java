package com.yourname.authservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Turn on when you add {@code @Scheduled} jobs. Later you can add Spring Batch with profile {@code batch}.
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "app.integration", name = "scheduling-enabled", havingValue = "true")
public class SchedulingEnablementConfiguration {
}
