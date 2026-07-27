package com.outty.backend.matching.service;

import com.outty.backend.common.exception.PotentialMatchUnavailableException;
import com.outty.backend.common.exception.ProfileNotFoundException;
import com.outty.backend.matching.dto.response.PotentialMatchResponse;
import com.outty.backend.matching.provider.PotentialMatchProvider;
import com.outty.backend.profile.dto.response.AdventurePreferenceResponse;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.entity.enums.AdventureType;
import com.outty.backend.profile.entity.enums.Gender;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;
import com.outty.backend.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PotentialMatchServiceImpl implements PotentialMatchService {

    private final ProfileRepository profileRepository;
    private final PotentialMatchProvider potentialMatchProvider;

    @Override
    @Transactional(readOnly = true)
    public List<PotentialMatchResponse> getPotentialMatches(Long userId) {
        Profile requester = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        List<PotentialMatchResponse> allCandidates = new ArrayList<>();

        List<PotentialMatchResponse> dbCandidates = profileRepository.findAll().stream()
                .filter(profile -> profile.getUser() != null && !Objects.equals(profile.getUser().getId(), userId))
                .map(this::mapProfileToPotentialMatchResponse)
                .toList();

        allCandidates.addAll(dbCandidates);

        Set<Long> existingUserIds = dbCandidates.stream()
                .map(PotentialMatchResponse::userId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        try {
            List<PotentialMatchResponse> mockCandidates = potentialMatchProvider.getCandidates();
            if (mockCandidates != null) {
                for (PotentialMatchResponse mock : mockCandidates) {
                    if (!Objects.equals(mock.userId(), userId) && !existingUserIds.contains(mock.userId())) {
                        allCandidates.add(mock);
                    }
                }
            }
        } catch (RuntimeException ex) {}

        return allCandidates.stream()
                .filter(candidate -> !Objects.equals(candidate.userId(), userId))
                .filter(candidate -> isWithinRequestedDistance(requester, candidate))
                .filter(candidate -> hasMatchingCountry(requester, candidate))
                .filter(candidate -> relationshipGoalsAreCompatible(
                        requester.getRelationshipGoal(),
                        candidate.relationshipGoal()
                ))
                .filter(candidate -> matchesRequesterGenderPreference(
                        requester.getInterestedIn(),
                        candidate.gender()
                ))
                .sorted(
                        Comparator
                                .comparing((PotentialMatchResponse candidate) -> sharesAdventureInterest(requester, candidate), Comparator.reverseOrder())
                                .thenComparingInt(candidate -> geographicRank(requester, candidate))
                                .thenComparing(PotentialMatchResponse::userId)
                )
                .limit(20)
                .toList();
    }

    private PotentialMatchResponse mapProfileToPotentialMatchResponse(Profile profile) {
        String firstPhotoUrl = null;
        if (profile.getPhotos() != null && !profile.getPhotos().isEmpty()) {
            firstPhotoUrl = profile.getPhotos().get(0);
        }

        return new PotentialMatchResponse(
                profile.getUser() != null ? profile.getUser().getId() : profile.getId(),
                profile.getUser().getFirstName(),
                firstPhotoUrl,
                profile.getCity(),
                profile.getState(),
                profile.getCountry(),
                profile.getGender() != null ? profile.getGender().name() : null,
                profile.getBirthDate(),
                profile.getBio(),
                profile.getInterestedIn(),
                profile.getRelationshipGoal(),
                mapAdventuresToResponse(profile),
                profile.getMatchDistanceMiles(),
                false
        );
    }

    private List<AdventurePreferenceResponse> mapAdventuresToResponse(Profile profile) {
        if (profile.getAdventures() == null) {
            return List.of();
        }
        return profile.getAdventures().stream()
                .map(adv -> new AdventurePreferenceResponse(
                        adv.getAdventureType(),
                        adv.getSkillLevel()
                ))
                .toList();
    }

    private boolean isWithinRequestedDistance(Profile requester, PotentialMatchResponse candidate) {
        Integer requestedDistance = requester.getMatchDistanceMiles();
        if (requestedDistance == null || requestedDistance <= 0) {
            return true;
        }

        if (candidate.distanceMiles() == null) {
            return true;
        }

        return candidate.distanceMiles() <= requestedDistance;
    }

    private boolean hasMatchingCountry(Profile requester, PotentialMatchResponse candidate) {
        String requesterCountry = requester.getCountry();
        String candidateCountry = candidate.country();

        if (requesterCountry == null || requesterCountry.isBlank()) {
            return true;
        }

        if (candidateCountry == null || candidateCountry.isBlank()) {
            return true;
        }

        return sameValue(requesterCountry, candidateCountry);
    }

    private boolean relationshipGoalsAreCompatible(
            RelationshipGoal requesterGoal,
            RelationshipGoal candidateGoal
    ) {
        if (requesterGoal == null || candidateGoal == null) {
            return true;
        }

        return requesterGoal == candidateGoal
                || requesterGoal == RelationshipGoal.BOTH
                || candidateGoal == RelationshipGoal.BOTH;
    }

    private boolean matchesRequesterGenderPreference(
            InterestedIn requesterPreference,
            String candidateGender
    ) {
        if (requesterPreference == null) {
            return true;
        }

        Gender gender = parseCandidateGender(candidateGender);
        if (gender == null) {
            return true;
        }

        return switch (requesterPreference) {
            case WOMEN -> gender == Gender.FEMALE;
            case MEN -> gender == Gender.MALE;
            case BOTH -> gender == Gender.MALE || gender == Gender.FEMALE;
        };
    }

    private Gender parseCandidateGender(String candidateGender) {
        if (candidateGender == null || candidateGender.isBlank()) {
            return null;
        }

        String normalized = candidateGender.trim()
                .toUpperCase(Locale.ROOT)
                .replace("-", "")
                .replace(" ", "");

        return switch (normalized) {
            case "MALE" -> Gender.MALE;
            case "FEMALE" -> Gender.FEMALE;
            case "NONBINARY" -> Gender.NONBINARY;
            case "PREFERNOT", "PREFERSNOTTOSAY" -> Gender.PREFERNOT;
            default -> null;
        };
    }

    private boolean sharesAdventureInterest(
            Profile requester,
            PotentialMatchResponse candidate
    ) {
        Set<AdventureType> requesterAdventures = new HashSet<>();
        if (requester.getAdventures() != null) {
            requester.getAdventures().stream()
                    .map(adventure -> adventure.getAdventureType())
                    .filter(Objects::nonNull)
                    .forEach(requesterAdventures::add);
        }

        if (requesterAdventures.isEmpty()) {
            return true;
        }

        if (candidate.adventures() == null || candidate.adventures().isEmpty()) {
            return false;
        }

        return candidate.adventures().stream()
                .map(AdventurePreferenceResponse::adventureType)
                .anyMatch(requesterAdventures::contains);
    }

    private int geographicRank(
            Profile requester,
            PotentialMatchResponse candidate
    ) {
        if (sameValue(requester.getCity(), candidate.city())
                && sameValue(requester.getState(), candidate.state())) {
            return 0;
        }

        if (sameValue(requester.getState(), candidate.state())) {
            return 1;
        }

        return 2;
    }

    private boolean sameValue(String first, String second) {
        return first != null
                && second != null
                && first.equalsIgnoreCase(second);
    }
}