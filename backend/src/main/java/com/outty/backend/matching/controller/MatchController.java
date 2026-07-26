package com.outty.backend.matching.controller;

import com.outty.backend.matching.dto.request.SwipeRequest;
import com.outty.backend.matching.dto.response.SwipeResponse;
import com.outty.backend.matching.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/{userId}/swipe")
    public ResponseEntity<SwipeResponse> handleSwipe(
            @PathVariable Long userId,
            @RequestBody SwipeRequest request) {

        SwipeResponse response = matchService.processSwipe(userId, request);
        return ResponseEntity.ok(response);
    }
}
