package com.bookmart.bookmart_backend.service;

import com.bookmart.bookmart_backend.model.dto.response.UserResponse;
import com.bookmart.bookmart_backend.model.enums.UserRole;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse getCurrentUserProfile(String email);

    List<UserResponse> getAllUsers();

    void deleteUser(UUID id);

    UserResponse updateUserRole(String email, UserRole Newrole);

}
