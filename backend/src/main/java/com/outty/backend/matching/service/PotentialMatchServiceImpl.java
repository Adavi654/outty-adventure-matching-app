package com.outty.backend.matching.service;

import com.outty.backend.common.exception.PotentialMatchUnavailableException;
import com.outty.backend.common.exception.ProfileNotFoundException;
import com.outty.backend.matching.dto.response.PotentialMatchResponse;
import com.outty.backend.matching.provider.PotentialMatchProvider;
import com.outty.backend.profile.dto.response.AdventurePreferenceResponse;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.entity.enums.AdventureType;
import com.outty.backend.profile.entity.enums.RelationshipGoal;
import com.outty.backend.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

        List<PotentialMatchResponse> candidates;

        try {
            candidates = potentialMatchProvider.getCandidates();
        } catch (RuntimeException ex) {
            throw new PotentialMatchUnavailableException(
                    "Potential matches are temporarily unavailable",
                    ex
            );
        }

        return candidates.stream()
                .filter(candidate -> !Objects.equals(candidate.userId(), userId))
                .filter(candidate -> sameValue(requester.getCountry(), candidate.country()))
                .filter(candidate -> relationshipGoalsAreCompatible(
                        requester.getRelationshipGoal(),
                        candidate.relationshipGoal()
                ))
                .filter(candidate -> sharesAdventureInterest(requester, candidate))
                .sorted(
                        Comparator
                                .comparingInt(
                                        (PotentialMatchResponse candidate) ->
                                                geographicRank(requester, candidate)
                                )
                                .thenComparing(PotentialMatchResponse::userId)
                )
                .toList();
    }

    private boolean relationshipGoalsAreCompatible(
            RelationshipGoal requesterGoal,
            RelationshipGoal candidateGoal
    ) {
        if (requesterGoal == null || candidateGoal == null) {
            return false;
        }

        return requesterGoal == candidateGoal
                || requesterGoal == RelationshipGoal.BOTH
                || candidateGoal == RelationshipGoal.BOTH;
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
            return false;
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
