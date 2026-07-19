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
import java.util.List;
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
                .photos(List.of("https://example.com/photo-1.jpg"))
                .build();
    }

    @Test
    void shouldCreateProfile() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.existsByUserId(1L)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> {
            Profile savedProfile = invocation.getArgument(0);
            savedProfile.setId(2L);
            savedProfile.setPhotos(List.of("https://example.com/photo-1.jpg", "https://example.com/photo-2.jpg"));
            return savedProfile;
        });

        ProfileRequest request = new ProfileRequest(
                1L, "City", "State", "Country", Gender.MALE,
                LocalDate.of(1990,1,1), "Bio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                List.of("https://example.com/photo-1.jpg", "https://example.com/photo-2.jpg")
        );

        ProfileResponse response = profileService.createProfile(1L, request);

        assertNotNull(response);
        assertEquals(2L, response.id());
        assertEquals(List.of("https://example.com/photo-1.jpg", "https://example.com/photo-2.jpg"), response.photos());
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
                List.of("https://example.com/photo-3.jpg")
        );

        ProfileResponse response = profileService.updateProfile(1L, request);

        assertNotNull(response);
        assertEquals("NewCity", response.city());
        assertEquals(List.of("https://example.com/photo-3.jpg"), response.photos());
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
                List.of()
        );

        assertThrows(ProfileAlreadyExistsException.class, () -> profileService.createProfile(1L, request));
    }
}
