package org.sc.auth.sc.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "invalid email format")
        String email,

        @NotBlank(message = "password is required")
        String password
) {
}
