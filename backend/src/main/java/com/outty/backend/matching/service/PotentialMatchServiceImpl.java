package com.outty.backend.matching.service;

import com.outty.backend.common.exception.PotentialMatchUnavailableException;
import com.outty.backend.common.exception.ProfileNotFoundException;
import com.outty.backend.matching.dto.response.PotentialMatchResponse;
import com.outty.backend.matching.provider.PotentialMatchProvider;
import com.outty.backend.profile.entity.Profile;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PotentialMatchServiceImpl implements PotentialMatchService {

    private final ProfileRepository profileRepository;
    private final PotentialMatchProvider potentialMatchProvider;

    @Override
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
                .filter(candidate -> interestsAreCompatible(
                        requester.getInterestedIn(),
                        candidate.interestedIn()
                ))
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

    private boolean interestsAreCompatible(
            InterestedIn requesterInterest,
            InterestedIn candidateInterest
    ) {
        if (requesterInterest == null || candidateInterest == null) {
            return false;
        }

        return requesterInterest == candidateInterest
                || requesterInterest == InterestedIn.BOTH
                || candidateInterest == InterestedIn.BOTH;
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
