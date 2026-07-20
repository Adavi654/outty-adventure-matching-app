package com.outty.backend.profile.service;

import com.outty.backend.auth.entity.User;
import com.outty.backend.auth.repository.UserRepository;
import com.outty.backend.common.exception.ProfileAlreadyExistsException;
import com.outty.backend.common.exception.ProfileNotFoundException;
import com.outty.backend.profile.dto.request.AdventurePreferenceRequest;
import com.outty.backend.profile.dto.request.ProfileRequest;
import com.outty.backend.profile.dto.request.UpdateProfileRequest;
import com.outty.backend.profile.dto.response.ProfileResponse;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.entity.ProfileAdventure;
import com.outty.backend.profile.entity.enums.*;
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
                List.of("https://example.com/photo-1.jpg", "https://example.com/photo-2.jpg"),
                null, null, null,
                List.of()
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
        assertEquals(List.of("https://example.com/photo-1.jpg"), response.photos());
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
                List.of("https://example.com/photo-3.jpg"),
                null, null, null,
                List.of()
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
                List.of(),
                null, null, null,
                List.of()
        );

        assertThrows(ProfileAlreadyExistsException.class, () -> profileService.createProfile(1L, request));
    }

    @Test
    void shouldCreateProfileWithNoImages() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.existsByUserId(1L)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> {
            Profile saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        ProfileRequest request = new ProfileRequest(
                1L, "City", "State", "Country", Gender.FEMALE,
                LocalDate.of(1990,1,1), "Bio", InterestedIn.BOTH, RelationshipGoal.FRIENDSHIPS,
                List.of(),
                null, null, null,
                List.of()
        );

        ProfileResponse response = profileService.createProfile(1L, request);

        assertNotNull(response.photos(), "List should be empty, not null");
        assertTrue(response.photos().isEmpty(), "Profile should have zero images");
    }
    
    @Test
    void shouldCreateProfileWithSingleImage() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.existsByUserId(1L)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> {
            Profile saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        String testImageUrl = "https://example.com/profile-pic.jpg";

        ProfileRequest request = new ProfileRequest(
                1L, "City", "State", "Country", Gender.MALE,
                LocalDate.of(1990, 1, 1), "Bio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                List.of(testImageUrl),
                null, null, null,
                List.of()
        );

        ProfileResponse response = profileService.createProfile(1L, request);

        assertNotNull(response.photos(), "Image list should not be null");
        assertEquals(1, response.photos().size(), "Profile should have exactly one image");
        assertEquals(testImageUrl, response.photos().get(0), "The saved image URL should match the input");
        
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void shouldCreateProfileWithMultipleImages() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.existsByUserId(1L)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> {
            Profile saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        String testImageUrl1 = "https://example.com/profile-pic1.jpg";
        String testImageUrl2 = "https://example.com/profile-pic2.jpg";

        ProfileRequest request = new ProfileRequest(
                1L, "City", "State", "Country", Gender.MALE,
                LocalDate.of(1990, 1, 1), "Bio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                List.of(testImageUrl1, testImageUrl2),
                null, null, null,
                List.of()
        );

        ProfileResponse response = profileService.createProfile(1L, request);

        assertNotNull(response.photos(), "Image list should not be null");
        assertEquals(2, response.photos().size(), "Profile should have exactly two images");
        assertEquals(testImageUrl1, response.photos().get(0), "The saved image URL should match the input");
        assertEquals(testImageUrl2, response.photos().get(1), "The saved image URL should match the input");
        
        verify(profileRepository).save(any(Profile.class));
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
                List.of(),
                "https://instagram.com/outty_hiker",
                "https://facebook.com/outty.hiker",
                "https://x.com/outty_hiker",
                List.of()
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
                List.of(),
                "https://instagram.com/new_handle",
                "https://facebook.com/outty.hiker",
                "https://x.com/outty_hiker",
                List.of()
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
                List.of(),
                "https://instagram.com/outty_hiker",
                "",
                null,
                List.of()
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

    @Test
    void shouldCreateProfileWithAdventurePreferences() {
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
                List.of(),
                null, null, null,
                List.of(
                        new AdventurePreferenceRequest(AdventureType.HIKING, SkillLevel.INTERMEDIATE),
                        new AdventurePreferenceRequest(AdventureType.CAMPING, SkillLevel.BEGINNER)
                )
        );

        ProfileResponse response = profileService.createProfile(1L, request);

        assertEquals(2, response.adventures().size());
        assertEquals(AdventureType.HIKING, response.adventures().get(0).adventureType());
        assertEquals(SkillLevel.INTERMEDIATE, response.adventures().get(0).skillLevel());
    }

    @Test
    void shouldUpdateAdventurePreferences() {
        ProfileAdventure existingAdventure = ProfileAdventure.builder()
                .adventureType(AdventureType.HIKING)
                .skillLevel(SkillLevel.BEGINNER)
                .build();
        profile.getAdventures().add(existingAdventure);
        existingAdventure.setProfile(profile);

        when(profileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        UpdateProfileRequest request = new UpdateProfileRequest(
                "City", "State", "Country", Gender.MALE,
                LocalDate.of(1990, 1, 1), "Bio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                List.of(),
                null, null, null,
                List.of(
                        new AdventurePreferenceRequest(AdventureType.CLIMBING, SkillLevel.ADVANCED)
                )
        );

        ProfileResponse response = profileService.updateProfile(1L, request);

        assertEquals(1, response.adventures().size());
        assertEquals(AdventureType.CLIMBING, response.adventures().get(0).adventureType());
        assertEquals(SkillLevel.ADVANCED, response.adventures().get(0).skillLevel());
    }

    @Test
    void shouldCreateProfileWithPhotosSocialsAndAdventurePreferences() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(profileRepository.existsByUserId(1L)).thenReturn(false);
        when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProfileRequest request = new ProfileRequest(
                1L, "City", "State", "Country", Gender.MALE,
                LocalDate.of(1990, 1, 1), "Bio", InterestedIn.BOTH, RelationshipGoal.BOTH,
                List.of("https://img.com/1.jpg", "https://img.com/2.jpg"),
                "https://instagram.com/user",
                "https://facebook.com/user",
                "https://x.com/user",
                List.of(
                        new AdventurePreferenceRequest(AdventureType.CLIMBING, SkillLevel.ADVANCED)
                )
        );

        ProfileResponse response = profileService.createProfile(1L, request);

        assertEquals(2, response.photos().size());
        assertEquals(List.of("https://img.com/1.jpg", "https://img.com/2.jpg"), response.photos());
        
        assertEquals("https://instagram.com/user", response.instagramUrl());
        assertEquals("https://facebook.com/user", response.facebookUrl());
        assertEquals("https://x.com/user", response.xUrl());

        assertEquals(1, response.adventures().size());
        assertEquals(AdventureType.CLIMBING, response.adventures().get(0).adventureType());
        assertEquals(SkillLevel.ADVANCED, response.adventures().get(0).skillLevel());
        
        verify(profileRepository).save(any(Profile.class));
    }
}
