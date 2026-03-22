package com.yourname.authservice.service;
// Package service: xử lý nghiệp vụ

import com.yourname.authservice.dto.LoginRequest;
import com.yourname.authservice.entity.AppUser;
import com.yourname.authservice.repository.AppUserRepository;
import com.yourname.authservice.exception.AuthExceptions.InvalidPasswordException;
import com.yourname.authservice.exception.AuthExceptions.UserDisabledException;
import com.yourname.authservice.exception.AuthExceptions.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
// Đánh dấu class này là Service Bean
public class AuthService {

    private final AppUserRepository userRepo;
    // Repository để truy vấn user từ DB

    private final PasswordEncoder encoder;
    // Bean PasswordEncoder (BCrypt)

    public AuthService(AppUserRepository userRepo, PasswordEncoder encoder) {
        // Constructor Injection (BEST PRACTICE)
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public AppUser authenticate(LoginRequest req) {
        // Hàm xác thực user khi login

        AppUser user = userRepo.findByUsername(req.username)
                .orElseThrow(UserNotFoundException::new);
        // Nếu không tìm thấy user → throw exception

        if (!Boolean.TRUE.equals(user.isEnabled())) {
            // Kiểm tra user có bị khóa không
            throw new UserDisabledException();
        }

        if (!encoder.matches(req.password, user.getPasswordHash())) {
            // So sánh password plaintext với password hash
            throw new InvalidPasswordException();
        }

        return user;
        // Trả về user đã xác thực thành công
    }
}
