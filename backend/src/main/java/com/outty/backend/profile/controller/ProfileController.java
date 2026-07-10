package com.outty.backend.profile.controller;

import com.outty.backend.profile.dto.request.CreateProfileRequest;
import com.outty.backend.profile.dto.response.ProfileResponse;
import com.outty.backend.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor

public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponse> createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody CreateProfileRequest request
    ) {
        ProfileResponse response = profileService.createProfile(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
