package com.outty.backend.profile.service;

import com.outty.backend.profile.dto.request.CreateProfileRequest;
import com.outty.backend.profile.dto.response.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(Long userId, CreateProfileRequest request);
}
