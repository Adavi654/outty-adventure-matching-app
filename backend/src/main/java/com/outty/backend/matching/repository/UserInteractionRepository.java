package com.outty.backend.matching.repository;

import com.outty.backend.matching.entity.UserInteraction;
import com.outty.backend.matching.entity.enums.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {

    boolean existsByActorIdAndTargetIdAndDecision(Long actorId, Long targetId, InteractionType decision);

    Optional<UserInteraction> findByActorIdAndTargetId(Long actorId, Long targetId);
}