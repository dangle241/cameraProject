package com.yourname.authservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps {@code DATABASE_URL} (Aiven / Render / Heroku style {@code postgres://...}) to Spring JDBC properties.
 * Preserves query string (e.g. {@code sslmode=require}) when present.
 */
public class CloudPostgresDatabaseEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(CloudPostgresDatabaseEnvironmentPostProcessor.class);

    private static final String SOURCE_NAME = "cloudPostgresDatabaseUrl";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return;
        }
        try {
            JdbcTarget target = toJdbc(databaseUrl, environment.getProperty("PG_SSL_MODE", "require"));
            Map<String, Object> map = new HashMap<>();
            map.put("spring.datasource.url", target.url());
            map.put("spring.datasource.username", target.username());
            map.put("spring.datasource.password", target.password());
            map.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
            environment.getPropertySources().addFirst(new MapPropertySource(SOURCE_NAME, map));
            log.info("DataSource from DATABASE_URL (host={}, database={})", target.host(), target.database());
        } catch (Exception e) {
            log.warn("Could not parse DATABASE_URL; using spring.datasource.* from files: {}", e.toString());
        }
    }

    private static JdbcTarget toJdbc(String databaseUrl, String sslModeFallback) throws Exception {
        URI uri = URI.create(databaseUrl.replaceFirst("^postgres(ql)?://", "http://"));
        String userInfo = uri.getUserInfo();
        if (userInfo == null || userInfo.isBlank()) {
            throw new IllegalArgumentException("DATABASE_URL missing user info");
        }
        int colon = userInfo.indexOf(':');
        String user = URLDecoder.decode(colon > 0 ? userInfo.substring(0, colon) : userInfo, StandardCharsets.UTF_8);
        String password = colon > 0 && colon < userInfo.length() - 1
                ? URLDecoder.decode(userInfo.substring(colon + 1), StandardCharsets.UTF_8)
                : "";
        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("DATABASE_URL missing host");
        }
        int port = uri.getPort() > 0 ? uri.getPort() : 5432;
        String path = uri.getPath();
        if (path == null || path.length() <= 1) {
            throw new IllegalArgumentException("DATABASE_URL missing database name in path");
        }
        String database = path.substring(1);
        String jdbcBase = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
        String query = uri.getRawQuery();
        String jdbcUrl;
        if (query != null && !query.isBlank()) {
            jdbcUrl = jdbcBase + "?" + query;
        } else {
            String ssl = sslModeFallback == null || sslModeFallback.isBlank() ? "require" : sslModeFallback;
            jdbcUrl = jdbcBase + "?sslmode=" + ssl;
        }
        return new JdbcTarget(jdbcUrl, user, password, host, database);
    }

    private record JdbcTarget(String url, String username, String password, String host, String database) {}
}
