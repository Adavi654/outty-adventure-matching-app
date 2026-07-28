-- Comprehensive sanitization of all persisted enum-backed VARCHAR columns.
-- Safe to rerun: canonical values are untouched, users/profiles are never deleted,
-- and every remaining non-null enum value is either canonical or replaced/deleted as documented.

-- ---------------------------------------------------------------------------
-- profiles.gender  (Java: Gender — MALE, FEMALE, NONBINARY, PREFERNOT)
-- ---------------------------------------------------------------------------

-- Recognizable legacy display variants.
UPDATE profiles
SET gender = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(gender), '-', ''), ' ', '')) = 'MALE' THEN 'MALE'
    WHEN UPPER(REPLACE(REPLACE(TRIM(gender), '-', ''), ' ', '')) = 'FEMALE' THEN 'FEMALE'
    WHEN UPPER(REPLACE(REPLACE(TRIM(gender), '-', ''), ' ', '')) = 'NONBINARY' THEN 'NONBINARY'
    WHEN UPPER(REPLACE(REPLACE(TRIM(gender), '-', ''), ' ', '')) IN (
        'PREFERNOT', 'PREFERNOTTOSAY', 'PREFERSNOTTOSAY'
    ) THEN 'PREFERNOT'
    ELSE gender
END
WHERE gender IS NOT NULL
  AND gender NOT IN ('MALE', 'FEMALE', 'NONBINARY', 'PREFERNOT');

-- PREFERNOT is the safe fallback when gender text cannot be mapped to a specific value.
UPDATE profiles
SET gender = 'PREFERNOT'
WHERE gender IS NOT NULL
  AND gender NOT IN ('MALE', 'FEMALE', 'NONBINARY', 'PREFERNOT');

-- ---------------------------------------------------------------------------
-- profiles.interested_in  (Java: InterestedIn — MEN, WOMEN, BOTH)
-- ---------------------------------------------------------------------------

-- Recognizable preference values, including relationship-goal text stored in the wrong column.
UPDATE profiles
SET interested_in = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(interested_in), '-', ''), ' ', '')) IN ('MEN', 'MAN') THEN 'MEN'
    WHEN UPPER(REPLACE(REPLACE(TRIM(interested_in), '-', ''), ' ', '')) IN ('WOMEN', 'WOMAN') THEN 'WOMEN'
    WHEN UPPER(REPLACE(REPLACE(TRIM(interested_in), '-', ''), ' ', '')) = 'BOTH' THEN 'BOTH'
    WHEN UPPER(REPLACE(REPLACE(TRIM(interested_in), '-', ''), ' ', '')) IN ('FRIENDSHIPS', 'FRIENDSHIP') THEN 'BOTH'
    WHEN UPPER(REPLACE(REPLACE(TRIM(interested_in), '-', ''), ' ', '')) IN (
        'RELATIONSHIPS', 'RELATIONSHIP', 'LONGTERMRELATIONSHIP', 'LONGTERMRELATIONSHIPS'
    ) THEN 'BOTH'
    ELSE interested_in
END
WHERE interested_in IS NOT NULL
  AND interested_in NOT IN ('MEN', 'WOMEN', 'BOTH');

-- BOTH is the safe fallback for any remaining unknown or cross-column legacy value (e.g. FRIENDSHIPS).
UPDATE profiles
SET interested_in = 'BOTH'
WHERE interested_in IS NOT NULL
  AND interested_in NOT IN ('MEN', 'WOMEN', 'BOTH');

-- ---------------------------------------------------------------------------
-- profiles.relationship_goal  (Java: RelationshipGoal — FRIENDSHIPS, RELATIONSHIPS, BOTH)
-- ---------------------------------------------------------------------------

UPDATE profiles
SET relationship_goal = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(relationship_goal), '-', ''), ' ', '')) IN ('FRIENDSHIPS', 'FRIENDSHIP')
         THEN 'FRIENDSHIPS'
    WHEN UPPER(REPLACE(REPLACE(TRIM(relationship_goal), '-', ''), ' ', '')) IN (
        'RELATIONSHIPS', 'RELATIONSHIP', 'LONGTERMRELATIONSHIP', 'LONGTERMRELATIONSHIPS'
    ) THEN 'RELATIONSHIPS'
    WHEN UPPER(REPLACE(REPLACE(TRIM(relationship_goal), '-', ''), ' ', '')) = 'BOTH' THEN 'BOTH'
    ELSE relationship_goal
END
WHERE relationship_goal IS NOT NULL
  AND relationship_goal NOT IN ('FRIENDSHIPS', 'RELATIONSHIPS', 'BOTH');

-- BOTH is the safe compatibility fallback for unrecognized relationship-goal legacy text.
UPDATE profiles
SET relationship_goal = 'BOTH'
WHERE relationship_goal IS NOT NULL
  AND relationship_goal NOT IN ('FRIENDSHIPS', 'RELATIONSHIPS', 'BOTH');

-- ---------------------------------------------------------------------------
-- profile_adventures.adventure_type  (Java: AdventureType)
-- ---------------------------------------------------------------------------

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

-- Delete only invalid adventure rows; the parent profile is preserved.
DELETE FROM profile_adventures
WHERE adventure_type IS NULL
   OR adventure_type NOT IN (
    'SKIING', 'BACKPACKING', 'TRAVELING', 'HIKING', 'CAMPING', 'KAYAKING', 'CLIMBING'
  );

-- ---------------------------------------------------------------------------
-- profile_adventures.skill_level  (Java: SkillLevel)
-- ---------------------------------------------------------------------------

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

-- Delete only invalid adventure rows; the parent profile is preserved.
DELETE FROM profile_adventures
WHERE skill_level IS NULL
   OR skill_level NOT IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT');

-- ---------------------------------------------------------------------------
-- users.role  (Java: Role — USER, ADMIN)
-- ---------------------------------------------------------------------------

UPDATE users
SET role = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(role), '-', ''), ' ', '')) = 'USER' THEN 'USER'
    WHEN UPPER(REPLACE(REPLACE(TRIM(role), '-', ''), ' ', '')) = 'ADMIN' THEN 'ADMIN'
    ELSE role
END
WHERE role IS NOT NULL
  AND role NOT IN ('USER', 'ADMIN');

-- USER is the safe fallback so accounts remain usable with standard permissions.
UPDATE users
SET role = 'USER'
WHERE role IS NOT NULL
  AND role NOT IN ('USER', 'ADMIN');

-- ---------------------------------------------------------------------------
-- user_interactions.decision  (Java: InteractionType — INTERESTED, REJECT)
-- ---------------------------------------------------------------------------

UPDATE user_interactions
SET decision = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(decision), '-', ''), ' ', '')) IN ('INTERESTED', 'LIKE', 'YES', 'RIGHT')
         THEN 'INTERESTED'
    WHEN UPPER(REPLACE(REPLACE(TRIM(decision), '-', ''), ' ', '')) IN ('REJECT', 'NO', 'PASS', 'LEFT', 'DISLIKE')
         THEN 'REJECT'
    ELSE decision
END
WHERE decision IS NOT NULL
  AND decision NOT IN ('INTERESTED', 'REJECT');

-- REJECT is the safe fallback so corrupt swipe data cannot create false mutual matches.
UPDATE user_interactions
SET decision = 'REJECT'
WHERE decision IS NOT NULL
  AND decision NOT IN ('INTERESTED', 'REJECT');
