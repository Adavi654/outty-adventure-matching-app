package com.outty.backend.matching.controller;

import com.outty.backend.matching.dto.request.SwipeRequest;
import com.outty.backend.matching.dto.response.SwipeResponse;
import com.outty.backend.matching.service.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {

    private static final Logger log = LoggerFactory.getLogger(MatchController.class);

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/{userId}/swipe")
    public ResponseEntity<?> handleSwipe(
            @PathVariable Long userId,
            @RequestBody SwipeRequest request) {

        log.info("Received swipe request for actorId: {}, payload: {}", userId, request);

        try {
            SwipeResponse response = matchService.processSwipe(userId, request);
            log.info("Swipe processed successfully. IsMatch: {}", response.isMatch());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing swipe for actorId: {}, targetId: {}", userId, request != null ? request.targetId() : null, e);
            return ResponseEntity.badRequest().body("Error processing swipe: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/matches")
    public ResponseEntity<List<Long>> getMatches(@PathVariable Long userId) {
        List<Long> matchedUserIds = matchService.getMatchedUserIds(userId);
        return ResponseEntity.ok(matchedUserIds);
    }
}
