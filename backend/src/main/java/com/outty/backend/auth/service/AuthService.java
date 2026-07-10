package com.outty.backend.auth.service;

import com.outty.backend.auth.dto.request.LoginRequest;
import com.outty.backend.auth.dto.request.RegisterRequest;
import com.outty.backend.auth.dto.response.LoginResponse;
import com.outty.backend.auth.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
