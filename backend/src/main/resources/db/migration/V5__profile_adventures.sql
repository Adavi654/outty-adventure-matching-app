CREATE TABLE profile_adventures (
    id BIGSERIAL PRIMARY KEY,
    profile_id BIGINT NOT NULL,
    adventure_type VARCHAR(50) NOT NULL,
    skill_level VARCHAR(50) NOT NULL,

    CONSTRAINT fk_profile_adventures_profile
        FOREIGN KEY (profile_id)
            REFERENCES profiles(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_profile_adventure
        UNIQUE (profile_id, adventure_type)
);