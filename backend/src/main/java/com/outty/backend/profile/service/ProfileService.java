package com.outty.backend.profile.service;

import com.outty.backend.profile.dto.request.ProfileRequest;
import com.outty.backend.profile.dto.response.ProfileResponse;
import com.outty.backend.profile.dto.request.UpdateProfileRequest;

public interface ProfileService {
    ProfileResponse createProfile(Long userId, ProfileRequest request);

    ProfileResponse getProfile(Long userId);

    ProfileResponse updateProfile(Long profileId, UpdateProfileRequest request);

    void deleteProfile(Long userId);
}
