package com.yourname.authservice.dto;
// DTO: đối tượng dùng để nhận dữ liệu từ client

public class LoginRequest {

    public String username;
    // Username gửi từ client (JSON)

    public String password;
    // Password dạng plaintext (chỉ tồn tại trong request)
}
