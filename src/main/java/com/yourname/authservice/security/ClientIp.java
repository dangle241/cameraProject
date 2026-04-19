package com.yourname.authservice.security;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Resolves client IP behind reverse proxies (Render, nginx) using X-Forwarded-For.
 */
public final class ClientIp {

    private ClientIp() {
    }

    public static String resolve(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
