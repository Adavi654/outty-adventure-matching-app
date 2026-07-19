import { useEffect, useState } from "react";
import ProfileForm from "./ProfileForm";
import {
  getProfile,
  createProfile,
  updateProfile,
} from "../services/profileApi";
import { formatEnum } from "../utils/formatters";

function ProfileManager() {
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("authToken");

  const [profile, setProfile] = useState(null);
  const [hasProfile, setHasProfile] = useState(false);
  const [isLoading, setIsLoading] = useState(Boolean(userId));
  const [isEditing, setIsEditing] = useState(false);
  const [isGalleryOpen, setIsGalleryOpen] = useState(false);

  useEffect(() => {
    if (!userId) {
      return;
    }

    const loadProfile = async () => {
      setIsLoading(true);
      try {
        const existingProfile = await getProfile(userId, token);
        if (existingProfile) {
          setProfile(existingProfile);
          setHasProfile(true);
        }
      } catch {
        setHasProfile(false);
      } finally {
        setIsLoading(false);
      }
    };

    loadProfile();
  }, [userId, token]);

  const handleSaveProfile = async (formData) => {
    setIsLoading(true);
    const token = localStorage.getItem("authToken");

    const payload = {
      ...formData,
      userId: parseInt(userId, 10),
      photos: formData.photos || [],
    };

    try {
      if (hasProfile) {
        const updatedProfile = await updateProfile(userId, payload, token);
        setProfile(updatedProfile);
      } else {
        const newProfile = await createProfile(userId, payload, token);
        setProfile(newProfile);
        setHasProfile(true);
      }
      setIsEditing(false);
    } catch (err) {
      console.error("Save failed:", err);
      alert("Save failed. Check console for details.");
    } finally {
      setIsLoading(false);
    }
  };

  const photos = profile?.photos || [];

  if (!userId) {
    return (
      <div>
        <h1>My Profile</h1>
        <p>Unable to identify the logged-in user. Please log in again.</p>
      </div>
    );
  }

  return (
    <div>
      <h1>{hasProfile ? "My Profile" : "Create Profile"}</h1>

      {hasProfile && !isEditing ? (
        <div className="profile-view">
          {/* Add the rest of your profile fields here */}
          <section className="location">
            <p>
              📍 {profile.city}, {profile.state}, {profile.country}
            </p>
          </section>

          <div className="info-grid">
            <div className="info-item">
              <strong>Gender:</strong> {formatEnum(profile.gender)}
            </div>
            <div className="info-item">
              <strong>Interested in:</strong> {formatEnum(profile.interestedIn)}
            </div>
            <div className="info-item">
              <strong>Goals:</strong> {formatEnum(profile.relationshipGoal)}
            </div>
            <div className="info-item">
              <strong>Birth Date:</strong> {profile.birthDate}
            </div>
          </div>

          <div className="profile-view-actions">
            <button
              className="secondary-action-button"
              onClick={() => setIsGalleryOpen(true)}
            >
              View Photo Gallery
            </button>
          </div>

          {isGalleryOpen && (
            <div
              className="modal-backdrop"
              role="dialog"
              aria-modal="true"
              aria-labelledby="photo-gallery-title"
              onClick={() => setIsGalleryOpen(false)}
            >
              <div className="modal-card" onClick={(event) => event.stopPropagation()}>
                <div className="modal-header">
                  <h3 id="photo-gallery-title">Photo Gallery</h3>
                  <button
                    className="modal-close-button"
                    onClick={() => setIsGalleryOpen(false)}
                    aria-label="Close photo gallery"
                  >
                    ×
                  </button>
                </div>

                {photos.length > 0 ? (
                  <div className="photo-gallery-grid">
                    {photos.map((photo, index) => (
                      <div className="photo-gallery-card" key={`${photo}-${index}`}>
                        <img src={photo} alt={`Profile photo ${index + 1}`} />
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="helper-text">No photos have been added to this profile yet.</p>
                )}
              </div>
            </div>
          )}

          <section className="bio-section">
            <h3>About Me</h3>
            <p className="bio-text">{profile.bio}</p>
          </section>

          <button onClick={() => setIsEditing(true)}>Edit Profile</button>
        </div>
      ) : (
        <ProfileForm
          mode={hasProfile ? "update" : "create"}
          initialValues={profile || undefined}
          onSubmit={handleSaveProfile}
          isLoading={isLoading}
        />
      )}
    </div>
  );
        {(profile.instagramUrl || profile.facebookUrl || profile.xUrl) && (
          <section className="social-links-section">
            <h3>Social Links</h3>
            <div className="social-links">
              {profile.instagramUrl && (
                <a
                  className="social-link-button"
                  href={profile.instagramUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  Instagram
                </a>
              )}
              {profile.facebookUrl && (
                <a
                  className="social-link-button"
                  href={profile.facebookUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  Facebook
                </a>
              )}
              {profile.xUrl && (
                <a
                  className="social-link-button"
                  href={profile.xUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  X
                </a>
              )}
            </div>
          </section>
        )}

        <button onClick={() => setIsEditing(true)}>Edit Profile</button>
      </div>
    ) : (
      <ProfileForm 
        mode={hasProfile ? 'update' : 'create'}
        initialValues={profile || undefined} 
        onSubmit={handleSaveProfile}
        isLoading={isLoading}
      />
    )}
  </div>
);
}

export default ProfileManager;
