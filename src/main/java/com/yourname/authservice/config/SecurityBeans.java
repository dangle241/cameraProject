package com.yourname.authservice.config;
// Package config: nơi khai báo Bean dùng chung

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// Đánh dấu đây là class cấu hình Spring
public class SecurityBeans {

    @Bean
    // Đăng ký PasswordEncoder vào Spring Context
    // => Có thể inject ở bất kỳ đâu bằng @Autowired / constructor
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
        // BCrypt:
        // - Hash mạnh
        // - Có salt
        // - Chậm có chủ đích → chống brute-force
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/login.html", "/home.html", "/login.js", "/login.css").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().permitAll()
                )
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login.html")
                        .defaultSuccessUrl("/home.html", true)
                        .failureUrl("/login.html?error=google_login_failed")
                        .userInfoEndpoint(user -> user.userService(new DefaultOAuth2UserService()))
                );

        return http.build();
    }
}
