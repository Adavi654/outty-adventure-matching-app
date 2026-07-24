package com.outty.backend.matching.service;

import com.outty.backend.auth.entity.User;
import com.outty.backend.common.exception.PotentialMatchUnavailableException;
import com.outty.backend.common.exception.ProfileNotFoundException;
import com.outty.backend.matching.dto.response.PotentialMatchResponse;
import com.outty.backend.matching.provider.PotentialMatchProvider;
import com.outty.backend.profile.dto.response.AdventurePreferenceResponse;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.entity.ProfileAdventure;
import com.outty.backend.profile.entity.enums.AdventureType;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;
import com.outty.backend.profile.entity.enums.SkillLevel;
import com.outty.backend.profile.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PotentialMatchServiceImplTest {

    private static final Long USER_ID = 1L;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PotentialMatchProvider potentialMatchProvider;

    @InjectMocks
    private PotentialMatchServiceImpl potentialMatchService;

    private Profile requester;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(USER_ID)
                .build();

        requester = Profile.builder()
                .id(10L)
                .user(user)
                .city("Atlanta")
                .state("Georgia")
                .country("United States")
                .interestedIn(InterestedIn.BOTH)
                .relationshipGoal(RelationshipGoal.FRIENDSHIPS)
                .adventures(new ArrayList<>(List.of(
                        ProfileAdventure.builder()
                                .adventureType(AdventureType.HIKING)
                                .skillLevel(SkillLevel.INTERMEDIATE)
                                .build()
                )))
                .build();
    }

    @Test
    void shouldReturnCompatibleCandidates() {
        PotentialMatchResponse candidate = candidate(
                2L,
                "Jordan",
                "Atlanta",
                "Georgia",
                "United States",
                RelationshipGoal.FRIENDSHIPS,
                defaultAdventures()
        );

        when(profileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(requester));
        when(potentialMatchProvider.getCandidates())
                .thenReturn(List.of(candidate));

        List<PotentialMatchResponse> matches =
                potentialMatchService.getPotentialMatches(USER_ID);

        assertEquals(1, matches.size());
        assertEquals(candidate, matches.getFirst());
        assertTrue(matches.getFirst().demoData());
    }

    @Test
    void shouldThrowWhenRequesterHasNoProfile() {
        when(profileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ProfileNotFoundException.class,
                () -> potentialMatchService.getPotentialMatches(USER_ID)
        );

        verify(potentialMatchProvider, never()).getCandidates();
    }

    @Test
    void shouldTreatBothAsCompatible() {
        requester.setRelationshipGoal(RelationshipGoal.BOTH);

        PotentialMatchResponse friendshipCandidate = candidate(
                2L,
                "Jordan",
                "Atlanta",
                "Georgia",
                "United States",
                RelationshipGoal.FRIENDSHIPS,
                defaultAdventures()
        );
        PotentialMatchResponse relationshipCandidate = candidate(
                3L,
                "Riley",
                "Atlanta",
                "Georgia",
                "United States",
                RelationshipGoal.RELATIONSHIPS,
                defaultAdventures()
        );

        when(profileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(requester));
        when(potentialMatchProvider.getCandidates())
                .thenReturn(List.of(
                        friendshipCandidate,
                        relationshipCandidate
                ));

        List<PotentialMatchResponse> matches =
                potentialMatchService.getPotentialMatches(USER_ID);

        assertEquals(2, matches.size());
    }

    @Test
    void shouldExcludeRequesterAndIncompatibleCandidates() {
        PotentialMatchResponse requesterCandidate = candidate(
                USER_ID,
                "Requester",
                "Atlanta",
                "Georgia",
                "United States",
                RelationshipGoal.FRIENDSHIPS,
                defaultAdventures()
        );
        PotentialMatchResponse incompatibleCandidate = candidate(
                2L,
                "Riley",
                "Atlanta",
                "Georgia",
                "United States",
                RelationshipGoal.RELATIONSHIPS,
                defaultAdventures()
        );
        PotentialMatchResponse compatibleCandidate = candidate(
                3L,
                "Jordan",
                "Atlanta",
                "Georgia",
                "United States",
                RelationshipGoal.BOTH,
                defaultAdventures()
        );
        PotentialMatchResponse differentCountryCandidate = candidate(
                4L,
                "Taylor",
                "Toronto",
                "Ontario",
                "Canada",
                RelationshipGoal.BOTH,
                defaultAdventures()
        );

        when(profileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(requester));
        when(potentialMatchProvider.getCandidates())
                .thenReturn(List.of(
                        requesterCandidate,
                        incompatibleCandidate,
                        compatibleCandidate,
                        differentCountryCandidate
                ));

        List<PotentialMatchResponse> matches =
                potentialMatchService.getPotentialMatches(USER_ID);

        assertEquals(List.of(3L), matches.stream()
                .map(PotentialMatchResponse::userId)
                .toList());
    }

    @Test
    void shouldRequireSharedAdventureInterest() {
        PotentialMatchResponse noSharedAdventureCandidate = candidate(
                2L,
                "Jordan",
                "Atlanta",
                "Georgia",
                "United States",
                RelationshipGoal.FRIENDSHIPS,
                List.of(
                        new AdventurePreferenceResponse(
                                AdventureType.KAYAKING,
                                SkillLevel.ADVANCED
                        )
                )
        );
        PotentialMatchResponse sharedAdventureCandidate = candidate(
                3L,
                "Avery",
                "Atlanta",
                "Georgia",
                "United States",
                RelationshipGoal.FRIENDSHIPS,
                List.of(
                        new AdventurePreferenceResponse(
                                AdventureType.HIKING,
                                SkillLevel.EXPERT
                        )
                )
        );

        when(profileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(requester));
        when(potentialMatchProvider.getCandidates())
                .thenReturn(List.of(
                        noSharedAdventureCandidate,
                        sharedAdventureCandidate
                ));

        List<PotentialMatchResponse> matches =
                potentialMatchService.getPotentialMatches(USER_ID);

        assertEquals(List.of(3L), matches.stream()
                .map(PotentialMatchResponse::userId)
                .toList());
    }

    @Test
    void shouldOrderSameCityBeforeSameStateAndSameCountry() {
        PotentialMatchResponse countryCandidate = candidate(
                2L,
                "Morgan",
                "Denver",
                "Colorado",
                "United States",
                RelationshipGoal.FRIENDSHIPS,
                defaultAdventures()
        );
        PotentialMatchResponse stateCandidate = candidate(
                3L,
                "Jordan",
                "Savannah",
                "Georgia",
                "United States",
                RelationshipGoal.FRIENDSHIPS,
                defaultAdventures()
        );
        PotentialMatchResponse cityCandidate = candidate(
                4L,
                "Avery",
                "Atlanta",
                "Georgia",
                "United States",
                RelationshipGoal.FRIENDSHIPS,
                defaultAdventures()
        );

        when(profileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(requester));
        when(potentialMatchProvider.getCandidates())
                .thenReturn(List.of(
                        countryCandidate,
                        stateCandidate,
                        cityCandidate
                ));

        List<PotentialMatchResponse> matches =
                potentialMatchService.getPotentialMatches(USER_ID);

        assertEquals(
                List.of(4L, 3L, 2L),
                matches.stream()
                        .map(PotentialMatchResponse::userId)
                        .toList()
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoCandidatesQualify() {
        when(profileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(requester));
        when(potentialMatchProvider.getCandidates())
                .thenReturn(List.of());

        List<PotentialMatchResponse> matches =
                potentialMatchService.getPotentialMatches(USER_ID);

        assertNotNull(matches);
        assertTrue(matches.isEmpty());
    }

    @Test
    void shouldWrapProviderFailure() {
        RuntimeException providerFailure =
                new RuntimeException("Provider failed");

        when(profileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(requester));
        when(potentialMatchProvider.getCandidates())
                .thenThrow(providerFailure);

        PotentialMatchUnavailableException exception = assertThrows(
                PotentialMatchUnavailableException.class,
                () -> potentialMatchService.getPotentialMatches(USER_ID)
        );

        assertEquals(
                "Potential matches are temporarily unavailable",
                exception.getMessage()
        );
        assertSame(providerFailure, exception.getCause());
    }

    private List<AdventurePreferenceResponse> defaultAdventures() {
        return List.of(
                new AdventurePreferenceResponse(
                        AdventureType.HIKING,
                        SkillLevel.INTERMEDIATE
                )
        );
    }

    private PotentialMatchResponse candidate(
            Long userId,
            String firstName,
            String city,
            String state,
            String country,
            RelationshipGoal relationshipGoal,
            List<AdventurePreferenceResponse> adventures
    ) {
        return new PotentialMatchResponse(
                userId,
                firstName,
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
                city,
                state,
                country,
                "Prefer not to say",
                LocalDate.of(1990, 1, 1),
                "Demo bio",
                InterestedIn.BOTH,
                relationshipGoal,
                adventures,
                true
        );
    }
}
