package com.outty.backend.auth.mapper;

import com.outty.backend.auth.dto.response.RegisterResponse;
import com.outty.backend.auth.entity.User;

public class UserMapper {
    private UserMapper() {
    }

    public static RegisterResponse toRegisterResponse(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
