package com.bookmart.bookmart_backend.service.implement;

import com.bookmart.bookmart_backend.Mapper.UserMapper;
import com.bookmart.bookmart_backend.exception.ResourceNotFoundException;
import com.bookmart.bookmart_backend.model.dto.response.UserResponse;
import com.bookmart.bookmart_backend.model.entity.User;
import com.bookmart.bookmart_backend.model.enums.UserRole;
import com.bookmart.bookmart_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserResponse testUserResponse;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

        testUser = new User();
        testUser.setId(testId);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setRole(UserRole.USER);

        testUserResponse = new UserResponse();
        testUserResponse.setId(testId);
        testUserResponse.setEmail("test@example.com");
        testUserResponse.setName("Test User");
        testUserResponse.setRole(UserRole.USER);
    }

    @Test
    void getCurrentUserProfile_WhenUserExists_ShouldReturnUserProfile() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserResponse);

        // Act
        UserResponse result = userService.getCurrentUserProfile(testUser.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals(testUserResponse.getEmail(), result.getEmail());
        assertEquals(testUserResponse.getName(), result.getName());
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void getCurrentUserProfile_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> userService.getCurrentUserProfile(testUser.getEmail()));
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(userMapper, never()).toDto(any());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(testUser)).thenReturn(testUserResponse);

        // Act
        List<UserResponse> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUserResponse.getEmail(), result.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        when(userRepository.existsById(testId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(testId);

        // Act
        userService.deleteUser(testId);

        // Assert
        verify(userRepository, times(1)).existsById(testId);
        verify(userRepository, times(1)).deleteById(testId);
    }

    @Test
    void deleteUser_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.existsById(testId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(testId));
        verify(userRepository, times(1)).existsById(testId);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void updateUserRole_WhenUserExists_ShouldUpdateRole() {
        // Arrange
        UserRole newRole = UserRole.ADMIN;
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserResponse);

        // Act
        UserResponse result = userService.updateUserRole(testUser.getEmail(), newRole);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void updateUserRole_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> userService.updateUserRole(testUser.getEmail(), UserRole.ADMIN));
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(userRepository, never()).save(any());
    }
}
