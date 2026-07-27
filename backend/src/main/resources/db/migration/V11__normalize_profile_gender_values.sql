-- Normalize legacy display-form gender values to canonical enum names.
-- Only updates rows that are not already valid Gender enum constants.

UPDATE profiles
SET gender = CASE
    WHEN UPPER(REPLACE(REPLACE(TRIM(gender), '-', ''), ' ', '')) = 'MALE' THEN 'MALE'
    WHEN UPPER(REPLACE(REPLACE(TRIM(gender), '-', ''), ' ', '')) = 'FEMALE' THEN 'FEMALE'
    WHEN UPPER(REPLACE(REPLACE(TRIM(gender), '-', ''), ' ', '')) = 'NONBINARY' THEN 'NONBINARY'
    WHEN UPPER(REPLACE(REPLACE(TRIM(gender), '-', ''), ' ', '')) IN (
        'PREFERNOT',
        'PREFERNOTTOSAY',
        'PREFERSNOTTOSAY'
    ) THEN 'PREFERNOT'
    ELSE gender
END
WHERE gender IS NOT NULL
  AND gender NOT IN ('MALE', 'FEMALE', 'NONBINARY', 'PREFERNOT');
