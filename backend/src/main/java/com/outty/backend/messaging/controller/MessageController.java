package com.outty.backend.messaging.controller;

import com.outty.backend.messaging.dto.request.MessageRequest;
import com.outty.backend.messaging.dto.response.MessageResponse;
import com.outty.backend.messaging.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Long>> getChatUsers(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getChatUsers(userId));
    }

    @GetMapping("/{userA}/{userB}")
    public ResponseEntity<List<MessageResponse>> getMessagesBetweenUsers(
            @PathVariable Long userA,
            @PathVariable Long userB
    ) {
        return ResponseEntity.ok(messageService.getMessagesBetweenUsers(userA, userB));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> saveMessage(@Valid @RequestBody MessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.saveMessage(request));
    }
}
