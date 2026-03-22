package com.yourname.authservice.controller;
// Package controller: nhận HTTP request

import com.yourname.authservice.dto.LoginRequest;
import com.yourname.authservice.dto.LoginResponse;
import com.yourname.authservice.entity.AppUser;
import com.yourname.authservice.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
// Kết hợp @Controller + @ResponseBody
// Trả JSON trực tiếp

@RequestMapping("/auth")
// Prefix cho toàn bộ API trong controller
public class AuthController {

    private final AuthService authService;
    // Inject AuthService

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // API: POST /auth/login
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        // @RequestBody:
        // - Parse JSON → LoginRequest object
        //Jackson ObjectMapper
//        Có annotation @RequestBody
//        Content-Type = application/json
//        Tham số KHÔNG phải String / primitive
        AppUser user = authService.authenticate(req);
        // Gọi service để xác thực

        // TODO: Generate real JWT (demo: fixed token string)
        LoginResponse body = new LoginResponse("JWT_TOKEN_WILL_BE_HERE");
        return ResponseEntity.ok(body);
        // Trả token JSON { token: "..." }
    }
}
