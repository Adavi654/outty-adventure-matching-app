package com.outty.backend.matching.repository;

import com.outty.backend.matching.entity.UserInteraction;
import com.outty.backend.matching.entity.enums.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {

    Optional<UserInteraction> findByActorIdAndTargetId(Long actorId, Long targetId);

    boolean existsByActorIdAndTargetIdAndDecision(Long actorId, Long targetId, InteractionType decision);

    @Query("""
        SELECT i1.targetId 
        FROM UserInteraction i1 
        JOIN UserInteraction i2 ON i1.actorId = i2.targetId AND i1.targetId = i2.actorId
        WHERE i1.actorId = :userId 
          AND i1.decision = 'INTERESTED' 
          AND i2.decision = 'INTERESTED'
    """)
    List<Long> findMutualMatchUserIds(@Param("userId") Long userId);
}