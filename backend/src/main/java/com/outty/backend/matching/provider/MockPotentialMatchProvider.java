package com.outty.backend.matching.provider;

import com.outty.backend.matching.dto.response.PotentialMatchResponse;
import com.outty.backend.profile.dto.response.AdventurePreferenceResponse;
import com.outty.backend.profile.entity.enums.AdventureType;
import com.outty.backend.profile.entity.enums.Gender;
import com.outty.backend.profile.entity.enums.InterestedIn;
import com.outty.backend.profile.entity.enums.RelationshipGoal;
import com.outty.backend.profile.entity.enums.SkillLevel;
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
                    "Marietta",
                    "Georgia",
                    "US",
                    Gender.NONBINARY,
                    LocalDate.of(1992, 4, 15),
                    "Weekend hiker who enjoys discovering new trails.",
                    InterestedIn.BOTH,
                    RelationshipGoal.BOTH,
                    List.of(
                            new AdventurePreferenceResponse(AdventureType.HIKING, SkillLevel.INTERMEDIATE),
                            new AdventurePreferenceResponse(AdventureType.CAMPING, SkillLevel.BEGINNER)
                    ),
                    20,
                    true
            ),
            new PotentialMatchResponse(
                    9002L,
                    "Jordan",
                    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=800&q=80",
                    "Kennesaw",
                    "Georgia",
                    "US",
                    Gender.MALE,
                    LocalDate.of(1989, 8, 22),
                    "Kayaking enthusiast and occasional camper.",
                    InterestedIn.WOMEN,
                    RelationshipGoal.FRIENDSHIPS,
                    List.of(
                            new AdventurePreferenceResponse(AdventureType.KAYAKING, SkillLevel.ADVANCED),
                            new AdventurePreferenceResponse(AdventureType.CAMPING, SkillLevel.INTERMEDIATE)
                    ),
                    27,
                    true
            ),
            new PotentialMatchResponse(
                    9003L,
                    "Morgan",
                    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=800&q=80",
                    "Denver",
                    "Colorado",
                    "US",
                    Gender.FEMALE,
                    LocalDate.of(1994, 2, 10),
                    "Always ready for a scenic hike or weekend road trip.",
                    InterestedIn.MEN,
                    RelationshipGoal.FRIENDSHIPS,
                    List.of(
                            new AdventurePreferenceResponse(AdventureType.HIKING, SkillLevel.EXPERT),
                            new AdventurePreferenceResponse(AdventureType.BACKPACKING, SkillLevel.ADVANCED)
                    ),
                    180,
                    true
            ),
            new PotentialMatchResponse(
                    9004L,
                    "Taylor",
                    "https://images.unsplash.com/photo-1527980965255-d3b416303d12?auto=format&fit=crop&w=800&q=80",
                    "Toronto",
                    "Ontario",
                    "Canada",
                    Gender.PREFERNOT,
                    null,
                    "Nature photographer who enjoys exploring national parks.",
                    InterestedIn.BOTH,
                    RelationshipGoal.BOTH,
                    List.of(
                            new AdventurePreferenceResponse(AdventureType.CLIMBING, SkillLevel.INTERMEDIATE),
                            new AdventurePreferenceResponse(AdventureType.TRAVELING, SkillLevel.ADVANCED)
                    ),
                    220,
                    true
            ),
            new PotentialMatchResponse(
                    9005L,
                    "Riley",
                    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=800&q=80",
                    "Athens",
                    "Georgia",
                    "US",
                    Gender.MALE,
                    LocalDate.of(1991, 11, 5),
                    "Trail runner looking for someone to share new experiences.",
                    InterestedIn.MEN,
                    RelationshipGoal.RELATIONSHIPS,
                    List.of(
                            new AdventurePreferenceResponse(AdventureType.HIKING, SkillLevel.INTERMEDIATE),
                            new AdventurePreferenceResponse(AdventureType.TRAVELING, SkillLevel.BEGINNER)
                    ),
                    72,
                    true
            ),
            new PotentialMatchResponse(
                    9006L,
                    "Casey",
                    "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?auto=format&fit=crop&w=800&q=80",
                    "Marietta",
                    "Georgia",
                    "US",
                    Gender.FEMALE,
                    LocalDate.of(1990, 6, 18),
                    "Coffee-fueled climber who loves weekend trail adventures.",
                    InterestedIn.BOTH,
                    RelationshipGoal.BOTH,
                    List.of(
                            new AdventurePreferenceResponse(AdventureType.CLIMBING, SkillLevel.INTERMEDIATE),
                            new AdventurePreferenceResponse(AdventureType.HIKING, SkillLevel.BEGINNER)
                    ),
                    18,
                    true
            ),
            new PotentialMatchResponse(
                    9007L,
                    "Sam",
                    "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=800&q=80",
                    "Atlanta",
                    "Georgia",
                    "US",
                    Gender.MALE,
                    LocalDate.of(1987, 1, 9),
                    "Outdoor photographer looking for a hiking buddy and new memories.",
                    InterestedIn.BOTH,
                    RelationshipGoal.BOTH,
                    List.of(
                            new AdventurePreferenceResponse(AdventureType.HIKING, SkillLevel.ADVANCED),
                            new AdventurePreferenceResponse(AdventureType.CLIMBING, SkillLevel.INTERMEDIATE)
                    ),
                    22,
                    true
            ),
            new PotentialMatchResponse(
                    9008L,
                    "Parker",
                    "https://images.unsplash.com/photo-1507591064344-4c6ce005b128?auto=format&fit=crop&w=800&q=80",
                    "Macon",
                    "Georgia",
                    "US",
                    Gender.MALE,
                    LocalDate.of(1993, 9, 2),
                    "Camping and kayaking enthusiast who enjoys spontaneous road trips.",
                    InterestedIn.BOTH,
                    RelationshipGoal.BOTH,
                    List.of(
                            new AdventurePreferenceResponse(AdventureType.CAMPING, SkillLevel.INTERMEDIATE),
                            new AdventurePreferenceResponse(AdventureType.KAYAKING, SkillLevel.BEGINNER)
                    ),
                    85,
                    true
            ),
            new PotentialMatchResponse(
                    9009L,
                    "Drew",
                    "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=800&q=80",
                    "Atlanta",
                    "Georgia",
                    "US",
                    Gender.FEMALE,
                    LocalDate.of(1992, 3, 14),
                    "Weekend explorer who loves hiking, camping, and climbing with friends.",
                    InterestedIn.BOTH,
                    RelationshipGoal.BOTH,
                    List.of(
                            new AdventurePreferenceResponse(AdventureType.HIKING, SkillLevel.ADVANCED),
                            new AdventurePreferenceResponse(AdventureType.CAMPING, SkillLevel.INTERMEDIATE),
                            new AdventurePreferenceResponse(AdventureType.CLIMBING, SkillLevel.INTERMEDIATE)
                    ),
                    18,
                    true
            )
    );

    @Override
    public List<PotentialMatchResponse> getCandidates() {
        return DEMO_CANDIDATES;
    }
}
