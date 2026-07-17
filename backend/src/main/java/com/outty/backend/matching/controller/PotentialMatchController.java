package com.outty.backend.matching.controller;

import com.outty.backend.matching.dto.response.PotentialMatchResponse;
import com.outty.backend.matching.service.PotentialMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class PotentialMatchController {

    private final PotentialMatchService potentialMatchService;

    @GetMapping("/{userId}/potential-matches")
    public ResponseEntity<List<PotentialMatchResponse>> getPotentialMatches(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                potentialMatchService.getPotentialMatches(userId)
        );
    }
}
