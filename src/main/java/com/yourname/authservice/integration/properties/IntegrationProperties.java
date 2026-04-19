package com.yourname.authservice.integration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Central toggles for optional infrastructure. Wire real beans when you add starters (see Maven profiles).
 */
@ConfigurationProperties(prefix = "app.integration")
public class IntegrationProperties {

    /**
     * When true, enables {@link org.springframework.scheduling.annotation.Scheduled} jobs.
     */
    private boolean schedulingEnabled = false;

    @NestedConfigurationProperty
    private final Messaging messaging = new Messaging();
    @NestedConfigurationProperty
    private final Kafka kafka = new Kafka();
    @NestedConfigurationProperty
    private final Redis redis = new Redis();
    @NestedConfigurationProperty
    private final Amqp amqp = new Amqp();
    @NestedConfigurationProperty
    private final Batch batch = new Batch();

    public boolean isSchedulingEnabled() {
        return schedulingEnabled;
    }

    public void setSchedulingEnabled(boolean schedulingEnabled) {
        this.schedulingEnabled = schedulingEnabled;
    }

    public Messaging getMessaging() {
        return messaging;
    }

    public Kafka getKafka() {
        return kafka;
    }

    public Redis getRedis() {
        return redis;
    }

    public Amqp getAmqp() {
        return amqp;
    }

    public Batch getBatch() {
        return batch;
    }

    public static class Messaging {
        /**
         * Emit {@code auth.login.success} through {@link com.yourname.authservice.integration.messaging.DomainEventPublisher}.
         */
        private boolean publishLoginSuccess = true;

        public boolean isPublishLoginSuccess() {
            return publishLoginSuccess;
        }

        public void setPublishLoginSuccess(boolean publishLoginSuccess) {
            this.publishLoginSuccess = publishLoginSuccess;
        }
    }

    public static class Kafka {
        private boolean enabled = false;
        private String bootstrapServers = "";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getBootstrapServers() {
            return bootstrapServers;
        }

        public void setBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }
    }

    public static class Redis {
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Amqp {
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class Batch {
        /**
         * When you add {@code spring-boot-starter-batch}, use profile {@code batch} and set this true to run jobs on startup if needed.
         */
        private boolean jobRunnerEnabled = false;

        public boolean isJobRunnerEnabled() {
            return jobRunnerEnabled;
        }

        public void setJobRunnerEnabled(boolean jobRunnerEnabled) {
            this.jobRunnerEnabled = jobRunnerEnabled;
        }
    }
}
