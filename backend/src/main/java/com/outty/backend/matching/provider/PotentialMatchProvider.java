package com.outty.backend.matching.provider;

import com.outty.backend.matching.dto.response.PotentialMatchResponse;

import java.util.List;

public interface PotentialMatchProvider {
    List<PotentialMatchResponse> getCandidates();
}
