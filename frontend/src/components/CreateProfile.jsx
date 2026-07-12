import ProfileForm from './ProfileForm'
import { createProfile } from '../services/profileApi'

function CreateProfile() {
  const handleCreateProfile = async (formData) => {
    await createProfile(1, formData, null)
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
