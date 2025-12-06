package com.bookmart.bookmart_backend.service;

import com.bookmart.bookmart_backend.model.dto.request.LoginRequest;
import com.bookmart.bookmart_backend.model.dto.request.RegisterRequest;
import com.bookmart.bookmart_backend.model.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    String register(RegisterRequest registerRequest);
}
