package com.outty.backend.messaging.repository;

import com.outty.backend.messaging.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "((m.senderId = :userA AND m.receiverId = :userB) OR " +
            "(m.senderId = :userB AND m.receiverId = :userA)) " +
            "ORDER BY m.createdAt ASC")
    List<Message> findMessagesBetweenUsers(@Param("userA") Long userA, @Param("userB") Long userB);
}
