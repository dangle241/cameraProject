package com.yourname.authservice.repository;
// Package repository: tầng giao tiếp DB

import com.yourname.authservice.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    // JpaRepository<T, ID>
    // T  = Entity
    // ID = kiểu khóa chính (UUID)

    Optional<AppUser> findByUsername(String username);
    // Spring Data JPA tự sinh query:
    // SELECT * FROM app_user WHERE username = ?
    //
    // Optional dùng để:
    // - Tránh NullPointerException
    // - Bắt buộc xử lý trường hợp không tồn tại user
}
