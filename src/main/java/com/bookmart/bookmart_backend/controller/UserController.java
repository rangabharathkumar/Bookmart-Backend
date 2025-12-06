package com.bookmart.bookmart_backend.controller;

import com.bookmart.bookmart_backend.model.dto.response.UserResponse;
import com.bookmart.bookmart_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User profile and account management")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
      @Autowired
      private UserService userService;

      @GetMapping("/me/{email}")
      @Operation(summary = "Get user profile", description = "Retrieve current user's profile information")
      @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
              @ApiResponse(responseCode = "404", description = "User not found")
      })
      public ResponseEntity<UserResponse> getCurrentUserProfile(
              @Parameter(description = "User email address") @PathVariable String email){
            UserResponse user =userService.getCurrentUserProfile(email);
            return ResponseEntity.ok(user);
      }

}
