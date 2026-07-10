package com.outty.backend.auth.service;

import com.outty.backend.auth.dto.request.LoginRequest;
import com.outty.backend.auth.dto.request.RegisterRequest;
import com.outty.backend.auth.dto.response.LoginResponse;
import com.outty.backend.auth.dto.response.RegisterResponse;
import com.outty.backend.auth.entity.User;
import com.outty.backend.auth.entity.enums.Role;
import com.outty.backend.auth.jwt.JwtService;
import com.outty.backend.auth.mapper.UserMapper;
import com.outty.backend.auth.repository.UserRepository;
import com.outty.backend.common.exception.EmailAlreadyExistsException;
import com.outty.backend.common.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        return UserMapper.toRegisterResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid email or password");
        }

       String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
