import { useState } from 'react'

const EMPTY_FORM_VALUES = {
  city: '',
  state: '',
  country: '',
  gender: '',
  birthDate: '',
  bio: '',
  interestedIn: '',
  relationshipGoal: '',
}

function ProfileForm({
  mode = 'create',
  initialValues = EMPTY_FORM_VALUES,
  onSubmit,
  isLoading = false,
}) {
  const [formData, setFormData] = useState(initialValues)
  const [statusMessage, setStatusMessage] = useState('')

  const handleChange = (event) => {
    const { name, value } = event.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setStatusMessage('')

    try {
      await onSubmit(formData)

      if (mode === 'update') {
        setStatusMessage('Profile updated successfully.')
      } else {
        setStatusMessage('Profile created successfully.')
      }
    } catch {
      setStatusMessage('Unable to create profile.')
    }
  }

  const buttonText = isLoading
    ? 'Saving...'
    : mode === 'update'
      ? 'Save Changes'
      : 'Save Profile'

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
          <option value="Male">Male</option>
          <option value="Female">Female</option>
          <option value="Non-binary">Non-binary</option>
          <option value="Prefer not to say">Prefer not to say</option>
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
        <label htmlFor="interestedIn">Interested In</label>
        <select
          id="interestedIn"
          name="interestedIn"
          value={formData.interestedIn}
          onChange={handleChange}
        >
          <option value="">Select interest</option>
          <option value="FRIENDSHIPS">Friendships</option>
          <option value="RELATIONSHIPS">Relationships</option>
          <option value="BOTH">Both</option>
        </select>
      </div>

      <div className="form-field">
        <label htmlFor="relationshipGoal">Relationship Goal</label>
        <input
          id="relationshipGoal"
          name="relationshipGoal"
          type="text"
          value={formData.relationshipGoal}
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
  )
}

export default ProfileForm
