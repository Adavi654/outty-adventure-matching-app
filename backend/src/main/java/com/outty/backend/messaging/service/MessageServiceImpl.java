package com.outty.backend.messaging.service;

import com.outty.backend.auth.repository.UserRepository;
import com.outty.backend.messaging.dto.request.MessageRequest;
import com.outty.backend.messaging.dto.response.MessageResponse;
import com.outty.backend.messaging.entity.Message;
import com.outty.backend.messaging.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public List<MessageResponse> getMessagesBetweenUsers(Long userA, Long userB) {
        return messageRepository.findMessagesBetweenUsers(userA, userB).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<Long> getChatUsers(Long currentUserId) {
        return userRepository.findAll().stream()
                .map(user -> user.getId())
                .filter(id -> !id.equals(currentUserId))
                .sorted()
                .toList();
    }

    @Override
    @Transactional
    public MessageResponse saveMessage(MessageRequest request) {
        Message message = Message.builder()
                .senderId(request.senderId())
                .receiverId(request.receiverId())
                .content(request.content())
                .build();

        Message saved = messageRepository.save(message);
        return toResponse(saved);
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getReceiverId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
