package com.yourname.authservice.entity;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Entity đại diện cho bảng app_user
 * Đây là DOMAIN OBJECT, KHÔNG phải DTO
 */
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    // Hibernate set field bằng reflection → không cần setter

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private Boolean enabled;

    /**
     * Constructor không tham số
     * BẮT BUỘC cho JPA
     * protected để code bên ngoài không dùng bừa
     */
    protected AppUser() {}

    /**
     * Constructor nghiệp vụ
     * Đảm bảo entity được tạo ở trạng thái hợp lệ
     */
    public AppUser(UUID id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = true;
    }

    // ===== GETTER – ĐỌC TRẠNG THÁI =====

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isEnabled() {
        // Tránh NullPointerException
        return Boolean.TRUE.equals(enabled);
    }

    // ===== METHOD NGHIỆP VỤ – THAY SETTER =====

    public void disable() {
        // Chỉ cho phép khóa user qua nghiệp vụ
        this.enabled = false;
    }
}
