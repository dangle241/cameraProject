package com.yourname.authservice.dto;

/**
 * JSON shape for SPA to attach CSRF header on mutating requests.
 */
public record CsrfTokenResponse(String token, String headerName, String parameterName) {}
