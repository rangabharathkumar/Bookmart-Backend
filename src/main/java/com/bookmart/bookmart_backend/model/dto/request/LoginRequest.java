package com.bookmart.bookmart_backend.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequest {
    @Email
    @Column(unique = true)
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
