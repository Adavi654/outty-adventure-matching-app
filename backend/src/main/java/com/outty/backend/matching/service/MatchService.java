package com.outty.backend.matching.service;

import com.outty.backend.matching.dto.request.SwipeRequest;
import com.outty.backend.matching.dto.response.SwipeResponse;
import com.outty.backend.matching.entity.UserInteraction;
import com.outty.backend.matching.entity.enums.InteractionType;
import com.outty.backend.matching.repository.UserInteractionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.outty.backend.profile.repository.ProfileRepository; 
import com.outty.backend.profile.entity.Profile;

import java.util.Locale;
import java.util.List;

@Service
public class MatchService {

    private final UserInteractionRepository interactionRepository;

    public MatchService(UserInteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    @Transactional
    public SwipeResponse processSwipe(Long actorId, SwipeRequest request) {
        if (request == null || request.targetId() == null) {
            throw new IllegalArgumentException("Target ID cannot be null");
        }

        InteractionType decisionType = parseInteractionType(request.decision());

        UserInteraction interaction = interactionRepository
            .findByActorIdAndTargetId(actorId, request.targetId())
            .orElse(null);

        if (interaction == null) {
            interaction = new UserInteraction(actorId, request.targetId(), decisionType);
        } else {
            interaction.setDecision(decisionType);
        }

        interactionRepository.saveAndFlush(interaction);

        if (decisionType != InteractionType.INTERESTED) {
            return new SwipeResponse(false, null);
        }

        boolean isMutualMatch = interactionRepository.existsByActorIdAndTargetIdAndDecision(
            request.targetId(),
            actorId,
            InteractionType.INTERESTED
        );

        return new SwipeResponse(isMutualMatch, isMutualMatch ? request.targetId() : null);
    }

    private InteractionType parseInteractionType(String rawDecision) {
        if (rawDecision == null) return InteractionType.REJECT;

        String norm = rawDecision.trim().toUpperCase(Locale.ROOT);
        return switch (norm) {
            case "INTERESTED", "LIKE", "YES", "RIGHT" -> InteractionType.INTERESTED;
            default -> InteractionType.REJECT;
        };
    }

    @Transactional(readOnly = true)
    public List<Long> getMatchedUserIds(Long userId) {
        return interactionRepository.findMutualMatchUserIds(userId);
    }
}
