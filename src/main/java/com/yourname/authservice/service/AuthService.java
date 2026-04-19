package com.yourname.authservice.service;
// Package service: xử lý nghiệp vụ

import com.yourname.authservice.dto.LoginRequest;
import com.yourname.authservice.entity.AppUser;
import com.yourname.authservice.exception.AuthExceptions.InvalidPasswordException;
import com.yourname.authservice.exception.AuthExceptions.UserDisabledException;
import com.yourname.authservice.exception.AuthExceptions.UserNotFoundException;
import com.yourname.authservice.integration.messaging.DomainEvent;
import com.yourname.authservice.integration.messaging.DomainEventPublisher;
import com.yourname.authservice.integration.properties.IntegrationProperties;
import com.yourname.authservice.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
// Đánh dấu class này là Service Bean
public class AuthService {

    private final AppUserRepository userRepo;
    // Repository để truy vấn user từ DB

    private final PasswordEncoder encoder;
    // Bean PasswordEncoder (BCrypt)

    private final DomainEventPublisher domainEventPublisher;
    private final IntegrationProperties integrationProperties;

    public AuthService(
            AppUserRepository userRepo,
            PasswordEncoder encoder,
            DomainEventPublisher domainEventPublisher,
            IntegrationProperties integrationProperties) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.domainEventPublisher = domainEventPublisher;
        this.integrationProperties = integrationProperties;
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

        if (integrationProperties.getMessaging().isPublishLoginSuccess()) {
            domainEventPublisher.publish(DomainEvent.simple("auth.login.success", user.getUsername()));
        }

        return user;
        // Trả về user đã xác thực thành công
    }
}
