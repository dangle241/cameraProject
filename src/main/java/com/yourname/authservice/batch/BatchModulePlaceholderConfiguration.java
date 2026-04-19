package com.yourname.authservice.batch;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Activate with {@code SPRING_PROFILES_ACTIVE=batch} after adding {@code spring-boot-starter-batch}
 * to the {@code batch} Maven profile, then add {@code @EnableBatchProcessing} on a dedicated config class.
 */
@Configuration
@Profile("batch")
public class BatchModulePlaceholderConfiguration {
}
