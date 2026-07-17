package com.outty.backend.matching.provider;

import com.outty.backend.matching.dto.response.PotentialMatchResponse;
import com.outty.backend.profile.entity.enums.InterestedIn;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MockPotentialMatchProvider implements PotentialMatchProvider {

    private static final List<PotentialMatchResponse> DEMO_CANDIDATES = List.of(
            new PotentialMatchResponse(
                    9001L,
                    "Avery",
                    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=800&q=80",
                    "Atlanta",
                    "Georgia",
                    "United States",
                    "Non-binary",
                    LocalDate.of(1992, 4, 15),
                    "Weekend hiker who enjoys discovering new trails.",
                    InterestedIn.BOTH,
                    "Open to friendship or a long-term relationship",
                    true
            ),
            new PotentialMatchResponse(
                    9002L,
                    "Jordan",
                    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80",
                    "Savannah",
                    "Georgia",
                    "United States",
                    "Male",
                    LocalDate.of(1989, 8, 22),
                    "Kayaking enthusiast and occasional camper.",
                    InterestedIn.FRIENDSHIPS,
                    "Meet new outdoor friends",
                    true
            ),
            new PotentialMatchResponse(
                    9003L,
                    "Morgan",
                    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=800&q=80",
                    "Denver",
                    "Colorado",
                    "United States",
                    "Female",
                    LocalDate.of(1994, 2, 10),
                    "Always ready for a scenic hike or weekend road trip.",
                    InterestedIn.FRIENDSHIPS,
                    "Build meaningful friendships",
                    true
            ),
            new PotentialMatchResponse(
                    9004L,
                    "Taylor",
                    "https://images.unsplash.com/photo-1527980965255-d3b416303d12?auto=format&fit=crop&w=800&q=80",
                    "Toronto",
                    "Ontario",
                    "Canada",
                    "Prefer not to say",
                    null,
                    "Nature photographer who enjoys exploring national parks.",
                    InterestedIn.BOTH,
                    "Meet people who enjoy the outdoors",
                    true
            ),
            new PotentialMatchResponse(
                    9005L,
                    "Riley",
                    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=800&q=80",
                    "Atlanta",
                    "Georgia",
                    "United States",
                    "Female",
                    LocalDate.of(1991, 11, 5),
                    "Trail runner looking for someone to share new experiences.",
                    InterestedIn.RELATIONSHIPS,
                    "Long-term relationship",
                    true
            )
    );

    @Override
    public List<PotentialMatchResponse> getCandidates() {
        return DEMO_CANDIDATES;
    }
}
