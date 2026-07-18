CREATE TABLE profiles (
   id BIGSERIAL PRIMARY KEY,
   user_id BIGINT NOT NULL UNIQUE,
   birth_date DATE,
   gender VARCHAR(30),
   interested_in VARCHAR(30),
   bio VARCHAR(500),
   occupation VARCHAR(100),
   city VARCHAR(100),
   state VARCHAR(100),
   country VARCHAR(100),
   relationship_goal VARCHAR(50),
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

   CONSTRAINT fk_profiles_user
      FOREIGN KEY (user_id)
         REFERENCES users(id)
         ON DELETE CASCADE
);
