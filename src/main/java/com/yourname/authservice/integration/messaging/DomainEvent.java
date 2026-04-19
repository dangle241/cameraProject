package com.yourname.authservice.integration.messaging;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Small domain envelope; later map this to Kafka records, AMQP messages, or outbox rows.
 */
public final class DomainEvent {

    private final String type;
    private final String subjectKey;
    private final Instant occurredAt;
    private final Map<String, Object> attributes;

    public DomainEvent(String type, String subjectKey, Instant occurredAt, Map<String, Object> attributes) {
        this.type = Objects.requireNonNull(type, "type");
        this.subjectKey = Objects.requireNonNull(subjectKey, "subjectKey");
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt");
        this.attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }

    public static DomainEvent of(String type, String subjectKey, Map<String, Object> attributes) {
        return new DomainEvent(type, subjectKey, Instant.now(), attributes);
    }

    public static DomainEvent simple(String type, String subjectKey) {
        return new DomainEvent(type, subjectKey, Instant.now(), Collections.emptyMap());
    }

    public String getType() {
        return type;
    }

    public String getSubjectKey() {
        return subjectKey;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
