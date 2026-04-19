package com.yourname.authservice.config;

import com.yourname.authservice.integration.messaging.DomainEventPublisher;
import com.yourname.authservice.integration.messaging.LoggingDomainEventPublisher;
import com.yourname.authservice.integration.properties.IntegrationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IntegrationProperties.class)
public class IntegrationBeansConfiguration {

    @Bean
    @ConditionalOnMissingBean(DomainEventPublisher.class)
    public DomainEventPublisher domainEventPublisher() {
        return new LoggingDomainEventPublisher();
    }
}
