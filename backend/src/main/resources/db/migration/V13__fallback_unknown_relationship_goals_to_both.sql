-- Unknown legacy relationship_goal values (e.g. "Whatever") cannot be mapped to a
-- specific enum constant. BOTH is used as the safe compatibility fallback so Hibernate
-- can load profiles without failing on unrecognized legacy display text.

UPDATE profiles
SET relationship_goal = 'BOTH'
WHERE relationship_goal IS NOT NULL
  AND relationship_goal NOT IN ('FRIENDSHIPS', 'RELATIONSHIPS', 'BOTH');
