CREATE TABLE profile_photos (
   profile_id BIGINT NOT NULL,
   photo_url VARCHAR(2048) NOT NULL,

   CONSTRAINT fk_profile_photos_profile
      FOREIGN KEY (profile_id)
         REFERENCES profiles(id)
         ON DELETE CASCADE
);