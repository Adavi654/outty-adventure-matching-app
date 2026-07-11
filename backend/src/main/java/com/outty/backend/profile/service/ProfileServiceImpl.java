package com.outty.backend.profile.service;

import com.outty.backend.auth.entity.User;
import com.outty.backend.auth.repository.UserRepository;
import com.outty.backend.common.exception.ProfileAlreadyExistsException;
import com.outty.backend.common.exception.UserNotFoundException;
import com.outty.backend.profile.dto.request.CreateProfileRequest;
import com.outty.backend.profile.dto.response.ProfileResponse;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.mapper.ProfileMapper;
import com.outty.backend.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    public ProfileResponse createProfile(Long userId, CreateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (profileRepository.existsByUserId(userId)) {
            throw new ProfileAlreadyExistsException("Profile already exists for this user");
        }

        Profile profile = Profile.builder()
                .user(user)
                .city(request.city())
                .state(request.state())
                .country(request.country())
                .gender(request.gender())
                .birthDate(request.birthDate())
                .bio(request.bio())
                .interestedIn(request.interestedIn())
                .relationshipGoal(request.relationshipGoal())
                .build();

        Profile savedProfile = profileRepository.save(profile);

        return ProfileMapper.toProfileResponse(savedProfile);
    }
}
