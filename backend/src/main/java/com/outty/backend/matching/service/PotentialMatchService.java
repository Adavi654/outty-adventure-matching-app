package com.outty.backend.matching.service;

import com.outty.backend.matching.dto.response.PotentialMatchResponse;

import java.util.List;

public interface PotentialMatchService {
    List<PotentialMatchResponse> getPotentialMatches(Long userId);
}
