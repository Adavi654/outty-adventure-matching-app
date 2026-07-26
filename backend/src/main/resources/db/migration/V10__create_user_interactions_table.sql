CREATE TABLE IF NOT EXISTS user_interactions (
    id BIGSERIAL PRIMARY KEY,
    actor_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    decision VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    CONSTRAINT uk_actor_target UNIQUE (actor_id, target_id)
);

CREATE INDEX IF NOT EXISTS idx_user_interactions_reciprocity 
ON user_interactions (target_id, actor_id, decision);