package com.outty.backend.auth.service;

import com.outty.backend.auth.dto.request.RegisterRequest;
import com.outty.backend.auth.dto.response.RegisterResponse;
import com.outty.backend.auth.entity.User;
import com.outty.backend.auth.entity.enums.Role;
import com.outty.backend.auth.repository.UserRepository;
import com.outty.backend.common.exception.EmailAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_shouldRegisterUserSuccessfully() {

        RegisterRequest registerRequest =
                new RegisterRequest(
                        "John",
                        "Doe",
                        "john@example.com",
                        "Password123"
                );

        when(userRepository.existsByEmail(registerRequest.email()))
                .thenReturn(false);

        when(passwordEncoder.encode(registerRequest.password()))
                .thenReturn("encodedPassword");

        User savedUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .enabled(true)
                .build();

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        RegisterResponse registerResponse = authService.register(registerRequest);

        assertEquals(1L, registerResponse.id());
        assertEquals("John", registerResponse.firstName());
        assertEquals("Doe", registerResponse.lastName());
        assertEquals("john@example.com", registerResponse.email());

        verify(userRepository).existsByEmail("john@example.com");
        verify(passwordEncoder).encode(registerRequest.password());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void register_shouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        RegisterRequest registerRequest = new RegisterRequest(
                "John",
                "Doe",
                "john@example.com",
                "Password123"
        );

        when(userRepository.existsByEmail(registerRequest.email()))
                .thenReturn(true);

        // Act & Assert
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(registerRequest)
        );

        assertEquals("Email is already registered", exception.getMessage());

        // Verify
        verify(userRepository).existsByEmail(registerRequest.email());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
