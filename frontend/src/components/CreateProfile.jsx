import ProfileForm from './ProfileForm'
import { createProfile } from '../services/profileApi'

function CreateProfile() {
  const userId = localStorage.getItem('userId')
  const token = localStorage.getItem('authToken')

  if (!userId) {
    return (
      <div>
        <h1>Create Profile</h1>
        <p>Unable to identify the logged-in user. Please log in again.</p>
      </div>
    )
  }

  const handleCreateProfile = async (formData) => {
    await createProfile(userId, formData, token)
  }

  return (
    <div>
      <h1>Create Profile</h1>
      <p>Welcome to Outty! Complete your profile below.</p>
      <ProfileForm mode="create" onSubmit={handleCreateProfile} />
    </div>
  )
}

export default CreateProfile
