ALTER TABLE profiles
    DROP COLUMN display_name,
    DROP COLUMN location;

ALTER TABLE profiles
    ADD COLUMN city VARCHAR(100) NOT NULL,
    ADD COLUMN state VARCHAR(100) NOT NULL,
    ADD COLUMN country VARCHAR(100) NOT NULL,
    ADD COLUMN gender VARCHAR(50) NOT NULL,
    ADD COLUMN birth_date DATE,
    ADD COLUMN bio VARCHAR(500) NOT NULL,
    ADD COLUMN interested_in VARCHAR(20) NOT NULL,
    ADD COLUMN relationship_goal VARCHAR(100) NOT NULL;

ALTER TABLE profiles
    DROP CONSTRAINT profiles_user_id_fkey;

ALTER TABLE profiles
    ADD CONSTRAINT profiles_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
