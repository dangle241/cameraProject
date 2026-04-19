package com.yourname.authservice.controller;

import com.yourname.authservice.dto.CsrfTokenResponse;
import com.yourname.authservice.dto.LoginRequest;
import com.yourname.authservice.dto.LoginResponse;
import com.yourname.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Exposes CSRF token metadata for the SPA (cookie + header name).
     */
    @GetMapping(value = "/csrf", produces = MediaType.APPLICATION_JSON_VALUE)
    public CsrfTokenResponse csrf(CsrfToken csrfToken) {
        return new CsrfTokenResponse(csrfToken.getToken(), csrfToken.getHeaderName(), csrfToken.getParameterName());
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        authService.authenticate(req);
        LoginResponse body = new LoginResponse("JWT_TOKEN_WILL_BE_HERE");
        return ResponseEntity.ok(body);
    }
}
