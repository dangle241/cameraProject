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
 * Maps a cloud {@code postgres://} URI to Spring JDBC properties.
 * Tries DATABASE_URL, then Aiven/Render-style aliases. On Render, fails fast if still pointing at localhost.
 */
public class CloudPostgresDatabaseEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(CloudPostgresDatabaseEnvironmentPostProcessor.class);

    private static final String SOURCE_NAME = "cloudPostgresDatabaseUrl";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = resolvePostgresUri(environment);
        if (databaseUrl != null && !databaseUrl.isBlank()) {
            try {
                JdbcTarget target = toJdbc(databaseUrl, environment.getProperty("PG_SSL_MODE", "require"));
                Map<String, Object> map = new HashMap<>();
                map.put("spring.datasource.url", target.url());
                map.put("spring.datasource.username", target.username());
                map.put("spring.datasource.password", target.password());
                map.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
                environment.getPropertySources().addFirst(new MapPropertySource(SOURCE_NAME, map));
                log.info("DataSource from cloud URI (host={}, database={})", target.host(), target.database());
            } catch (Exception e) {
                log.warn("Could not parse cloud Postgres URI; using spring.datasource.* from files: {}", e.toString());
            }
            return;
        }

        if (isRender(environment) && datasourceLooksLikeLocalPostgres(environment)) {
            throw new IllegalStateException(
                    "Render: chua cau hinh Postgres. Them bien moi truong DATABASE_URL = Service URI day du cua Aiven "
                            + "(postgres://avnadmin:MAT_KHAU@host:port/defaultdb?sslmode=require). "
                            + "Hoac dat SPRING_DATASOURCE_URL jdbc:postgresql://... va SPRING_DATASOURCE_USERNAME / PASSWORD. "
                            + "Xem Environment tab cua Web Service tren Render.");
        }
    }

    private static boolean isRender(ConfigurableEnvironment environment) {
        return "true".equalsIgnoreCase(environment.getProperty("RENDER"));
    }

    private static boolean datasourceLooksLikeLocalPostgres(ConfigurableEnvironment environment) {
        String url = environment.getProperty("spring.datasource.url", "");
        return url.contains("localhost") || url.contains("127.0.0.1");
    }

    /**
     * First non-blank URI in postgres:// form, or SPRING_DATASOURCE_URL if it is a postgres URI (misplaced paste).
     */
    private static String resolvePostgresUri(ConfigurableEnvironment environment) {
        String[] keys = {"DATABASE_URL", "AIVEN_DATABASE_URL", "POSTGRES_URL", "POSTGRESQL_URL"};
        for (String key : keys) {
            String v = environment.getProperty(key);
            if (v != null && !v.isBlank() && looksLikePostgresUri(v)) {
                return v.trim();
            }
        }
        String spring = environment.getProperty("SPRING_DATASOURCE_URL");
        if (spring != null && looksLikePostgresUri(spring.trim())) {
            return spring.trim();
        }
        return null;
    }

    private static boolean looksLikePostgresUri(String v) {
        String lower = v.toLowerCase();
        return lower.startsWith("postgres://") || lower.startsWith("postgresql://");
    }

    private static JdbcTarget toJdbc(String databaseUrl, String sslModeFallback) throws Exception {
        URI uri = URI.create(databaseUrl.replaceFirst("^postgres(ql)?://", "http://"));
        String userInfo = uri.getUserInfo();
        if (userInfo == null || userInfo.isBlank()) {
            throw new IllegalArgumentException("URI missing user info");
        }
        int colon = userInfo.indexOf(':');
        String user = URLDecoder.decode(colon > 0 ? userInfo.substring(0, colon) : userInfo, StandardCharsets.UTF_8);
        String password = colon > 0 && colon < userInfo.length() - 1
                ? URLDecoder.decode(userInfo.substring(colon + 1), StandardCharsets.UTF_8)
                : "";
        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("URI missing host");
        }
        int port = uri.getPort() > 0 ? uri.getPort() : 5432;
        String path = uri.getPath();
        if (path == null || path.length() <= 1) {
            throw new IllegalArgumentException("URI missing database name in path");
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
