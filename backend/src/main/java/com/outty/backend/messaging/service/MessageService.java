package com.outty.backend.messaging.service;

import com.outty.backend.messaging.dto.request.MessageRequest;
import com.outty.backend.messaging.dto.response.MessageResponse;

import java.util.List;

public interface MessageService {
    List<MessageResponse> getMessagesBetweenUsers(Long userA, Long userB);
    List<Long> getChatUsers(Long currentUserId);
    MessageResponse saveMessage(MessageRequest request);
}
