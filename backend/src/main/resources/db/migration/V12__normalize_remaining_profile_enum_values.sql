-- Normalize legacy display-form enum values to canonical names.
-- Only updates rows that are not already valid enum constants.

UPDATE profiles
SET relationship_goal = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(relationship_goal), '-', ''), ' ', ''))
         IN ('FRIENDSHIPS', 'FRIENDSHIP') THEN 'FRIENDSHIPS'
    WHEN UPPER(REPLACE(REPLACE(TRIM(relationship_goal), '-', ''), ' ', ''))
         IN ('RELATIONSHIPS', 'RELATIONSHIP', 'LONGTERMRELATIONSHIP', 'LONGTERMRELATIONSHIPS')
         THEN 'RELATIONSHIPS'
    WHEN UPPER(REPLACE(REPLACE(TRIM(relationship_goal), '-', ''), ' ', '')) = 'BOTH'
         THEN 'BOTH'
    ELSE relationship_goal
END
WHERE relationship_goal IS NOT NULL
  AND relationship_goal NOT IN ('FRIENDSHIPS', 'RELATIONSHIPS', 'BOTH');

UPDATE profiles
SET interested_in = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(interested_in), '-', ''), ' ', '')) IN ('MEN', 'MAN') THEN 'MEN'
    WHEN UPPER(REPLACE(REPLACE(TRIM(interested_in), '-', ''), ' ', '')) IN ('WOMEN', 'WOMAN') THEN 'WOMEN'
    WHEN UPPER(REPLACE(REPLACE(TRIM(interested_in), '-', ''), ' ', '')) = 'BOTH' THEN 'BOTH'
    ELSE interested_in
END
WHERE interested_in IS NOT NULL
  AND interested_in NOT IN ('MEN', 'WOMEN', 'BOTH');

UPDATE profile_adventures
SET adventure_type = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(adventure_type), '-', ''), ' ', '')) = 'SKIING' THEN 'SKIING'
    WHEN UPPER(REPLACE(REPLACE(TRIM(adventure_type), '-', ''), ' ', '')) = 'BACKPACKING' THEN 'BACKPACKING'
    WHEN UPPER(REPLACE(REPLACE(TRIM(adventure_type), '-', ''), ' ', '')) = 'TRAVELING' THEN 'TRAVELING'
    WHEN UPPER(REPLACE(REPLACE(TRIM(adventure_type), '-', ''), ' ', '')) = 'HIKING' THEN 'HIKING'
    WHEN UPPER(REPLACE(REPLACE(TRIM(adventure_type), '-', ''), ' ', '')) = 'CAMPING' THEN 'CAMPING'
    WHEN UPPER(REPLACE(REPLACE(TRIM(adventure_type), '-', ''), ' ', '')) = 'KAYAKING' THEN 'KAYAKING'
    WHEN UPPER(REPLACE(REPLACE(TRIM(adventure_type), '-', ''), ' ', '')) = 'CLIMBING' THEN 'CLIMBING'
    ELSE adventure_type
END
WHERE adventure_type IS NOT NULL
  AND adventure_type NOT IN (
    'SKIING', 'BACKPACKING', 'TRAVELING', 'HIKING', 'CAMPING', 'KAYAKING', 'CLIMBING'
  );

UPDATE profile_adventures
SET skill_level = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(skill_level), '-', ''), ' ', '')) = 'BEGINNER' THEN 'BEGINNER'
    WHEN UPPER(REPLACE(REPLACE(TRIM(skill_level), '-', ''), ' ', '')) = 'INTERMEDIATE' THEN 'INTERMEDIATE'
    WHEN UPPER(REPLACE(REPLACE(TRIM(skill_level), '-', ''), ' ', '')) = 'ADVANCED' THEN 'ADVANCED'
    WHEN UPPER(REPLACE(REPLACE(TRIM(skill_level), '-', ''), ' ', '')) = 'EXPERT' THEN 'EXPERT'
    ELSE skill_level
END
WHERE skill_level IS NOT NULL
  AND skill_level NOT IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT');
