import { useState } from "react";

const MAX_PHOTOS = 10;
const EMPTY_FORM_VALUES = {
  city: '',
  state: '',
  country: '',
  gender: '',
  birthDate: '',
  bio: '',
  interestedIn: '',
  relationshipGoal: '',
  instagramUrl: '',
  facebookUrl: '',
  xUrl: '',
}

function ProfileForm({
  mode = "create",
  initialValues = EMPTY_FORM_VALUES,
  onSubmit,
  isLoading = false,
}) {
  const [formData, setFormData] = useState({
    ...initialValues,
    photos: initialValues?.photos || [],
  });
  const [statusMessage, setStatusMessage] = useState("");
  const [photoError, setPhotoError] = useState("");

  const [formData, setFormData] = useState({
    ...EMPTY_FORM_VALUES,
    ...initialValues,
    instagramUrl: initialValues?.instagramUrl ?? '',
    facebookUrl: initialValues?.facebookUrl ?? '',
    xUrl: initialValues?.xUrl ?? '',
  })
  const [statusMessage, setStatusMessage] = useState('')

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handlePhotoSelection = (event) => {
    const selectedFiles = Array.from(event.target.files || []);
    const nextPhotos = [...(formData.photos || [])];

    if (selectedFiles.length + nextPhotos.length > MAX_PHOTOS) {
      setPhotoError(`You can upload up to ${MAX_PHOTOS} photos.`);
      return;
    }

    selectedFiles.forEach((file) => {
      nextPhotos.push(URL.createObjectURL(file));
    });

    setFormData((prev) => ({
      ...prev,
      photos: nextPhotos,
    }));
    setPhotoError("");
    event.target.value = "";
  };

  const removePhoto = (photoToRemove) => {
    setFormData((prev) => ({
      ...prev,
      photos: (prev.photos || []).filter((photo) => photo !== photoToRemove),
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setStatusMessage("");

    try {
      await onSubmit({ ...formData, photos: formData.photos || [] });

      if (mode === "update") {
        setStatusMessage("Profile updated successfully.");
      } else {
        setStatusMessage("Profile created successfully.");
      }
    } catch {
      setStatusMessage("Unable to create profile.");
    }
  };

  const buttonText = isLoading
    ? "Saving..."
    : mode === "update"
      ? "Save Changes"
      : "Save Profile";

  return (
    <form className="profile-form" onSubmit={handleSubmit}>
      <div className="form-field">
        <label htmlFor="city">City</label>
        <input
          id="city"
          name="city"
          type="text"
          value={formData.city}
          onChange={handleChange}
        />
      </div>

      <div className="form-field">
        <label htmlFor="state">State</label>
        <input
          id="state"
          name="state"
          type="text"
          value={formData.state}
          onChange={handleChange}
        />
      </div>

      <div className="form-field">
        <label htmlFor="country">Country</label>
        <input
          id="country"
          name="country"
          type="text"
          value={formData.country}
          onChange={handleChange}
        />
      </div>

      <div className="form-field">
        <label htmlFor="gender">Gender</label>
        <select
          id="gender"
          name="gender"
          value={formData.gender}
          onChange={handleChange}
        >
          <option value="">Select gender</option>
          <option value="MALE">Male</option>
          <option value="FEMALE">Female</option>
          <option value="NONBINARY">Non-binary</option>
          <option value="PREFERNOT">Prefer not to say</option>
        </select>
      </div>

      <div className="form-field">
        <label htmlFor="birthDate">Birth Date</label>
        <input
          id="birthDate"
          name="birthDate"
          type="date"
          value={formData.birthDate}
          onChange={handleChange}
        />
      </div>

      <div className="form-field">
        <label htmlFor="bio">Bio</label>
        <textarea
          id="bio"
          name="bio"
          maxLength={500}
          rows={4}
          value={formData.bio}
          onChange={handleChange}
        />
      </div>

      <div className="form-field">
        <label htmlFor="relationshipGoal">Relationship Goal</label>
        <select
          id="relationshipGoal"
          name="relationshipGoal"
          value={formData.relationshipGoal}
          onChange={handleChange}
        >
          <option value="">Select interest</option>
          <option value="FRIENDSHIPS">Friendships</option>
          <option value="RELATIONSHIPS">Relationships</option>
          <option value="BOTH">Both</option>
        </select>
      </div>

      <div className="form-field">
        <label htmlFor="interestedIn">Interested In</label>
        <select
          id="interestedIn"
          name="interestedIn"
          value={formData.interestedIn}
          onChange={handleChange}
        >
          <option value="">Select interest</option>
          <option value="MEN">Men</option>
          <option value="WOMEN">Women</option>
          <option value="BOTH">Both</option>
        </select>
      </div>

      <div className="form-field">
        <label htmlFor="photo-upload">Upload Photos</label>
        <input
          id="photo-upload"
          type="file"
          accept="image/*"
          multiple
          onChange={handlePhotoSelection}
        />
        <p className="helper-text">
          Choose up to {MAX_PHOTOS} photos. You can remove any selected photo
          before saving.
        </p>
        {photoError && (
          <p className="status-message error-text">{photoError}</p>
        )}
        {formData.photos?.length > 0 && (
          <div className="photo-preview-list">
            {formData.photos.map((photo, index) => (
              <div className="photo-preview-card" key={`${photo}-${index}`}>
                <img src={photo} alt={`Profile preview ${index + 1}`} />
                <button
                  type="button"
                  className="remove-photo-button"
                  onClick={() => removePhoto(photo)}
                >
                  Remove
                </button>
              </div>
            ))}
          </div>
        )}
        <label htmlFor="instagramUrl">Instagram URL</label>
        <input
          id="instagramUrl"
          name="instagramUrl"
          type="url"
          placeholder="https://instagram.com/username"
          value={formData.instagramUrl}
          onChange={handleChange}
        />
      </div>

      <div className="form-field">
        <label htmlFor="facebookUrl">Facebook URL</label>
        <input
          id="facebookUrl"
          name="facebookUrl"
          type="url"
          placeholder="https://facebook.com/username"
          value={formData.facebookUrl}
          onChange={handleChange}
        />
      </div>

      <div className="form-field">
        <label htmlFor="xUrl">X URL</label>
        <input
          id="xUrl"
          name="xUrl"
          type="url"
          placeholder="https://x.com/username"
          value={formData.xUrl}
          onChange={handleChange}
        />
      </div>

      <button
        className="save-profile-button"
        type="submit"
        disabled={isLoading}
      >
        {buttonText}
      </button>

      {statusMessage && <p>{statusMessage}</p>}
    </form>
  );
}

export default ProfileForm;
