package com.outty.backend.profile.service;

import com.outty.backend.auth.entity.User;
import com.outty.backend.auth.repository.UserRepository;
import com.outty.backend.common.exception.ProfileAlreadyExistsException;
import com.outty.backend.common.exception.ProfileNotFoundException;
import com.outty.backend.profile.dto.request.ProfileRequest;
import com.outty.backend.profile.dto.request.UpdateProfileRequest;
import com.outty.backend.profile.dto.response.ProfileResponse;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.entity.enums.Gender;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;
import com.outty.backend.profile.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private User user;
    private Profile profile;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("pass")
                .firstName("First")
                .lastName("Last")
                .build();

        profile = Profile.builder()
                .id(2L)
                .user(user)
                .city("City")
                .state("State")
                .country("Country")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1990,1,1))
                .bio("Bio")
                .interestedIn(InterestedIn.BOTH)
                .relationshipGoal(RelationshipGoal.BOTH)
                .build();
    }

    @Test
    void shouldCreateProfile() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.existsByUserId(1L)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        ProfileRequest request = new ProfileRequest(
                1L, "City", "State", "Country", Gender.MALE,
                LocalDate.of(1990,1,1), "Bio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                null, null, null
        );

        ProfileResponse response = profileService.createProfile(1L, request);

        assertNotNull(response);
        assertEquals(2L, response.id());
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void shouldGetProfile() {
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        ProfileResponse response = profileService.getProfile(1L);

        assertNotNull(response);
        assertEquals(2L, response.id());
    }

    @Test
    void shouldThrowWhenGetProfileNotFound() {
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> profileService.getProfile(1L));
    }

    @Test
    void shouldUpdateProfile() {
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        UpdateProfileRequest request = new UpdateProfileRequest(
                "NewCity", "NewState", "NewCountry", Gender.MALE,
                LocalDate.of(1991,2,2), "NewBio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                null, null, null
        );

        ProfileResponse response = profileService.updateProfile(1L, request);

        assertNotNull(response);
        assertEquals("NewCity", response.city());
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void shouldDeleteProfile() {
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        profileService.deleteProfile(1L);

        verify(profileRepository).delete(profile);
    }

    @Test
    void shouldNotCreateWhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.existsByUserId(1L)).thenReturn(true);

        ProfileRequest request = new ProfileRequest(
                1L, "City", "State", "Country", Gender.FEMALE,
                LocalDate.of(1990,1,1), "Bio", InterestedIn.BOTH, RelationshipGoal.FRIENDSHIPS,
                null, null, null
        );

        assertThrows(ProfileAlreadyExistsException.class, () -> profileService.createProfile(1L, request));
    }

    @Test
    void shouldCreateProfileWithValidSocialUrls() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.existsByUserId(1L)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> {
            Profile saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        ProfileRequest request = new ProfileRequest(
                1L, "City", "State", "Country", Gender.MALE,
                LocalDate.of(1990, 1, 1), "Bio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                "https://instagram.com/outty_hiker",
                "https://facebook.com/outty.hiker",
                "https://x.com/outty_hiker"
        );

        ProfileResponse response = profileService.createProfile(1L, request);

        assertEquals("https://instagram.com/outty_hiker", response.instagramUrl());
        assertEquals("https://facebook.com/outty.hiker", response.facebookUrl());
        assertEquals("https://x.com/outty_hiker", response.xUrl());
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void shouldUpdateAndReplaceSocialUrl() {
        profile.setInstagramUrl("https://instagram.com/old_handle");
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        UpdateProfileRequest request = new UpdateProfileRequest(
                "City", "State", "Country", Gender.MALE,
                LocalDate.of(1990, 1, 1), "Bio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                "https://instagram.com/new_handle",
                "https://facebook.com/outty.hiker",
                "https://x.com/outty_hiker"
        );

        ProfileResponse response = profileService.updateProfile(1L, request);

        assertEquals("https://instagram.com/new_handle", response.instagramUrl());
        verify(profileRepository).save(profile);
    }

    @Test
    void shouldClearSocialUrlWithBlankInput() {
        profile.setInstagramUrl("https://instagram.com/outty_hiker");
        profile.setFacebookUrl("https://facebook.com/outty.hiker");
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        UpdateProfileRequest request = new UpdateProfileRequest(
                "City", "State", "Country", Gender.MALE,
                LocalDate.of(1990, 1, 1), "Bio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                "https://instagram.com/outty_hiker",
                "",
                null
        );

        ProfileResponse response = profileService.updateProfile(1L, request);

        assertEquals("https://instagram.com/outty_hiker", response.instagramUrl());
        assertNull(response.facebookUrl());
        assertNull(response.xUrl());
    }

    @Test
    void shouldIncludeSavedSocialUrlsInProfileResponse() {
        profile.setInstagramUrl("https://instagram.com/outty_hiker");
        profile.setFacebookUrl("https://facebook.com/outty.hiker");
        profile.setXUrl("https://x.com/outty_hiker");
        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));

        ProfileResponse response = profileService.getProfile(1L);

        assertEquals("https://instagram.com/outty_hiker", response.instagramUrl());
        assertEquals("https://facebook.com/outty.hiker", response.facebookUrl());
        assertEquals("https://x.com/outty_hiker", response.xUrl());
    }
}
