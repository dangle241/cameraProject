package com.yourname.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank
    @Size(max = 128)
    public String username;

    @NotBlank
    @Size(max = 256)
    public String password;
}
