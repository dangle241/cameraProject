package com.yourname.authservice.integration.messaging;

/**
 * Outbound domain events. Replace default bean with Kafka / Rabbit / Redis Streams adapter later.
 */
public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
