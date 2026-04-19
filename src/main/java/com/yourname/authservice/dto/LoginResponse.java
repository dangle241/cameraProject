package com.yourname.authservice.dto;
// DTO trả dữ liệu cho client

public class LoginResponse {

    public String token;
    // JWT token sau khi login thành công

    public LoginResponse(String token) {
        // Constructor để gán token
        this.token = token;
    }
}
