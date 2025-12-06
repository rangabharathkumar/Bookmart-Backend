package com.bookmart.bookmart_backend.model.dto.response;

import com.bookmart.bookmart_backend.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private UUID id;
    private String email;
    private String name;
    private UserRole role;
    private String accessToken; 
    private String tokenType = "Bearer"; 

    

}
