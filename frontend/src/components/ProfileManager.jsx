import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import ProfileForm from "./ProfileForm";
import {
  getProfile,
  createProfile,
  updateProfile,
} from "../services/profileApi";
import {
  getMatches,
  getMessages,
  sendMessage,
} from "../services/messageApi";
import { formatEnum } from "../utils/formatters";

function ProfileManager() {
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("authToken");

  const [profile, setProfile] = useState(null);
  const [hasProfile, setHasProfile] = useState(false);
  const [isLoading, setIsLoading] = useState(Boolean(userId));
  const [isEditing, setIsEditing] = useState(false);
  const [isGalleryOpen, setIsGalleryOpen] = useState(false);
  const [isChatOpen, setIsChatOpen] = useState(false);
  const [chatUsers, setChatUsers] = useState([]);
  const [chatUserNames, setChatUserNames] = useState({});
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [messages, setMessages] = useState([]);
  const [draft, setDraft] = useState("");
  const [chatError, setChatError] = useState("");

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

  useEffect(() => {
    if (!isChatOpen || !userId) {
      return;
    }

    const loadChatUsers = async () => {
      try {
        // SWAP: Call getMatches instead of getChatUsers
        const users = await getMatches(userId, token);
        setChatUsers(users);

        if (!selectedUserId && users.length > 0) {
          setSelectedUserId(users[0]);
        }

        const resolvedNames = await Promise.all(
          users.map(async (contactId) => {
            try {
              const profileData = await getProfile(contactId, token);
              const displayName = [profileData.firstName, profileData.lastName]
                .filter(Boolean)
                .join(" ");
              return [contactId, displayName || `User ${contactId}`];
            } catch {
              return [contactId, `User ${contactId}`];
            }
          }),
        );

        setChatUserNames(Object.fromEntries(resolvedNames));
      } catch (err) {
        setChatError("Unable to load chat partners.");
      }
    };

    loadChatUsers();
    const intervalId = window.setInterval(loadChatUsers, 5000);
    return () => window.clearInterval(intervalId);
  }, [isChatOpen, userId, token, selectedUserId]);

  useEffect(() => {
    if (!isChatOpen || !userId || !selectedUserId) {
      return;
    }

    const loadMessages = async () => {
      try {
        const history = await getMessages(userId, selectedUserId, token);
        setMessages(history);
        setChatError("");
      } catch {
        setChatError("Unable to load conversation history.");
      }
    };

    loadMessages();
    const intervalId = window.setInterval(loadMessages, 5000);
    return () => window.clearInterval(intervalId);
  }, [isChatOpen, userId, token, selectedUserId]);

  const handleSaveProfile = async (formData) => {
    setIsLoading(true);
    const token = localStorage.getItem("authToken");

    const payload = {
      ...formData,
      userId: parseInt(userId, 10),
      matchDistanceMiles: formData.matchDistanceMiles === '' ? null : Number(formData.matchDistanceMiles),
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

  const handleSendMessage = async (event) => {
    event.preventDefault();
    if (!draft.trim() || !userId || !selectedUserId) {
      return;
    }

    try {
      await sendMessage(
        {
          senderId: Number(userId),
          receiverId: Number(selectedUserId),
          content: draft.trim(),
        },
        token,
      );
      setDraft("");
      const history = await getMessages(userId, selectedUserId, token);
      setMessages(history);
    } catch {
      setChatError("Unable to send message right now.");
    }
  };

  if (!userId) {
    return (
      <div className="profile-page">
        <h1>My Profile</h1>
        <p className="profile-empty-state">
          Unable to identify the logged-in user. Please log in again.
        </p>
      </div>
    );
  }

  return (
    <div className="profile-page">
      <h1>{hasProfile ? "My Profile" : "Create Profile"}</h1>

      {hasProfile && !isEditing ? (
        <div className="profile-view">
          <div className="profile-view-actions profile-view-actions--top">
            <button
              type="button"
              className="secondary-action-button"
              onClick={() => setIsChatOpen(true)}
            >
              Chat
            </button>
          </div>

          <section className="profile-card profile-header">
            <p className="profile-location">
              <span className="profile-location-icon" aria-hidden="true">
                📍
              </span>
              {profile.city}, {profile.state}, {profile.country}
            </p>
            <div className="info-grid">
              <div className="info-item">
                <span className="info-label">Gender</span>
                <span className="info-value">{formatEnum(profile.gender)}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Interested in</span>
                <span className="info-value">
                  {formatEnum(profile.interestedIn)}
                </span>
              </div>
              <div className="info-item">
                <span className="info-label">Goals</span>
                <span className="info-value">
                  {formatEnum(profile.relationshipGoal)}
                </span>
              </div>
              <div className="info-item">
                <span className="info-label">Birth date</span>
                <span className="info-value">{profile.birthDate}</span>
              </div>
              {profile.matchDistanceMiles != null && (
                <div className="info-item">
                  <span className="info-label">Match distance</span>
                  <span className="info-value">
                    {profile.matchDistanceMiles} miles
                  </span>
                </div>
              )}
            </div>
          </section>

          <section className="profile-card bio-section">
            <h3 className="profile-section-title">About Me</h3>
            <p className="bio-text">
              {profile.bio || "No bio added yet."}
            </p>
          </section>

          <section className="profile-card adventures-section">
            <h3 className="profile-section-title">Adventure Interests</h3>
            {profile.adventures?.length > 0 ? (
              <div className="adventure-tags">
                {profile.adventures.map(({ adventureType, skillLevel }) => (
                  <div className="adventure-tag" key={adventureType}>
                    <span className="adventure-tag-name">
                      {formatEnum(adventureType)}
                    </span>
                    <span className="adventure-tag-skill">
                      {formatEnum(skillLevel)}
                    </span>
                  </div>
                ))}
              </div>
            ) : (
              <p className="profile-empty-state">
                No adventure interests added yet.
              </p>
            )}
          </section>

          {(profile.instagramUrl || profile.facebookUrl || profile.xUrl) && (
            <section className="profile-card social-links-section">
              <h3 className="profile-section-title">Social Links</h3>
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

          <section className="profile-card profile-photos-section">
            <h3 className="profile-section-title">Photos</h3>
            {photos.length > 0 ? (
              <p className="profile-photo-count">
                {photos.length} photo{photos.length === 1 ? "" : "s"} on your
                profile
              </p>
            ) : (
              <p className="profile-empty-state">
                No photos have been added yet.
              </p>
            )}
            <div className="profile-view-actions">
              <button
                type="button"
                className="secondary-action-button"
                onClick={() => setIsGalleryOpen(true)}
              >
                View Photo Gallery
              </button>
            </div>
          </section>

          {isGalleryOpen && (
            <div
              className="modal-backdrop"
              role="dialog"
              aria-modal="true"
              aria-labelledby="photo-gallery-title"
              onClick={() => setIsGalleryOpen(false)}
            >
              <div className="modal-card photo-gallery-modal" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                  <h3 id="photo-gallery-title">Photo Gallery</h3>
                  <button
                    type="button"
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
                      <div
                        className="photo-gallery-card"
                        key={`${photo}-${index}`}
                      >
                        <img src={photo} alt={`Profile photo ${index + 1}`} />
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="profile-empty-state">
                    No photos have been added yet.
                  </p>
                )}
              </div>
            </div>
          )}

          <Link className="matches-cta" to="/matches">
            Find Matches
          </Link>

          {isChatOpen && (
            <div
              className="modal-backdrop"
              role="dialog"
              aria-modal="true"
              onClick={() => setIsChatOpen(false)}
            >
              <div
                className="modal-card chat-modal"
                onClick={(e) => e.stopPropagation()}
              >
                <div className="modal-header">
                  <h3>Messages</h3>
                  <button
                    type="button"
                    className="modal-close-button"
                    onClick={() => setIsChatOpen(false)}
                  >
                    ×
                  </button>
                </div>

                <div className="chat-layout">
                  <div className="chat-user-list">
                    {chatUsers.length === 0 ? (
                      <p className="helper-text">No chat partners yet.</p>
                    ) : (
                      chatUsers.map((userIdValue) => (
                        <button
                          key={userIdValue}
                          type="button"
                          className={`chat-user-button ${selectedUserId === userIdValue ? "chat-user-button--active" : ""}`}
                          onClick={() => setSelectedUserId(userIdValue)}
                        >
                          {chatUserNames[userIdValue] || `User ${userIdValue}`}
                        </button>
                      ))
                    )}
                  </div>

                  <div className="chat-panel">
                    {chatError && <p className="error-text">{chatError}</p>}
                    <div className="chat-history">
                      {messages.length === 0 ? (
                        <p className="helper-text">Start the conversation.</p>
                      ) : (
                        messages.map((message) => (
                          <div
                            key={message.id}
                            className={`chat-bubble ${message.senderId === Number(userId) ? "chat-bubble--self" : ""}`}
                          >
                            <div>{message.content}</div>
                            <small>
                              {message.createdAt
                                ? new Date(message.createdAt).toLocaleString()
                                : ""}
                            </small>
                          </div>
                        ))
                      )}
                    </div>

                    <form className="chat-compose" onSubmit={handleSendMessage}>
                      <input
                        value={draft}
                        onChange={(event) => setDraft(event.target.value)}
                        placeholder="Type a message"
                      />
                      <button type="submit">Send</button>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          )}

          <div className="profile-view-actions">
            <button
              type="button"
              className="secondary-action-button"
              onClick={() => setIsEditing(true)}
            >
              Edit Profile
            </button>
          </div>
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
}

export default ProfileManager;
