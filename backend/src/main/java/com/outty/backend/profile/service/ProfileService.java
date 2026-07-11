package com.outty.backend.profile.service;

import com.outty.backend.profile.dto.request.ProfileRequest;
import com.outty.backend.profile.dto.response.ProfileResponse;

public interface ProfileService {
    ProfileResponse createProfile(Long userId, ProfileRequest request);

    ProfileResponse getProfile(Long userId);

    ProfileResponse updateProfile(Long userId, ProfileRequest request);

    void deleteProfile(Long userId);
}
