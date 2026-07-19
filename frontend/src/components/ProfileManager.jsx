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
        {/* ... Location and Info Grid sections ... */}
        <section className="location">
           <p>📍 {profile.city}, {profile.state}, {profile.country}</p>
        </section>
        
        <div className="info-grid">...</div>
        
        {(profile.instagramUrl || profile.facebookUrl || profile.xUrl) && (
          <section className="social-links-section">
            <h3>Social Links</h3>
            <div className="social-links">
              {profile.instagramUrl && <a href={profile.instagramUrl}>Instagram</a>}
              {profile.facebookUrl && <a href={profile.facebookUrl}>Facebook</a>}
              {profile.xUrl && <a href={profile.xUrl}>X</a>}
            </div>
          </section>
        )}

        <section className="bio-section">
          <h3>About Me</h3>
          <p className="bio-text">{profile.bio}</p>
        </section>

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

export default ProfileManager;
