package com.outty.backend.matching.service;

import com.outty.backend.matching.dto.request.SwipeRequest;
import com.outty.backend.matching.dto.response.SwipeResponse;
import com.outty.backend.matching.entity.UserInteraction;
import com.outty.backend.matching.entity.enums.InteractionType;
import com.outty.backend.matching.repository.UserInteractionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchService {

    private final UserInteractionRepository interactionRepository;

    public MatchService(UserInteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    @Transactional
    public SwipeResponse processSwipe(Long actorId, SwipeRequest request) {
        InteractionType decisionType = InteractionType.valueOf(request.decision().toUpperCase());

        UserInteraction interaction = interactionRepository
            .findByActorIdAndTargetId(actorId, request.targetId())
            .orElseGet(() -> new UserInteraction(actorId, request.targetId(), decisionType));

        interaction.setDecision(decisionType);
        interactionRepository.save(interaction);

        if (decisionType == InteractionType.REJECT) {
            return new SwipeResponse(false, null);
        }

        boolean isMutualMatch = interactionRepository.existsByActorIdAndTargetIdAndDecision(
            request.targetId(),
            actorId,
            InteractionType.INTERESTED
        );

        return new SwipeResponse(isMutualMatch, isMutualMatch ? request.targetId() : null);
    }
}
