package com.alammoah.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// ── Auth DTOs ─────────────────────────────────────────────────────────────────

public class AuthDto {

    @Data
    public static class RegisterRequest {
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be 3-50 characters")
        private String username;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        private String course;
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    @lombok.Builder
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class AuthResponse {
        private String token;
        private String type = "Bearer";
        private Long id;
        private String username;
        private String email;
        private String course;
        private long bookmarkCount;
    }
}
