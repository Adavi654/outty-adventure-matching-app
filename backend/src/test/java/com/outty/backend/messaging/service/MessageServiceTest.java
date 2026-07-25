package com.outty.backend.messaging.service;

import com.outty.backend.auth.repository.UserRepository;
import com.outty.backend.messaging.dto.response.MessageResponse;
import com.outty.backend.messaging.entity.Message;
import com.outty.backend.messaging.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void getMessagesBetweenUsersReturnsMessagesInChronologicalOrder() {
        Message first = Message.builder()
                .id(1L)
                .senderId(1L)
                .receiverId(2L)
                .content("Hello")
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 0))
                .build();

        Message second = Message.builder()
                .id(2L)
                .senderId(2L)
                .receiverId(1L)
                .content("Hi there")
                .createdAt(LocalDateTime.of(2024, 1, 1, 10, 1))
                .build();

        when(messageRepository.findMessagesBetweenUsers(1L, 2L)).thenReturn(List.of(first, second));

        List<MessageResponse> messages = messageService.getMessagesBetweenUsers(1L, 2L);

        assertEquals(2, messages.size());
        assertEquals("Hello", messages.get(0).content());
        assertEquals("Hi there", messages.get(1).content());
    }
}
