package com.yourname.authservice.integration.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default publisher until Kafka / AMQP wiring exists.
 */
public class LoggingDomainEventPublisher implements DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingDomainEventPublisher.class);

    @Override
    public void publish(DomainEvent event) {
        log.info("domain-event type={} subjectKey={} at={} attrs={}",
                event.getType(), event.getSubjectKey(), event.getOccurredAt(), event.getAttributes());
    }
}
