import { useEffect, useState } from 'react'
import ProfileForm from './ProfileForm'
import { getProfile, updateProfile } from '../services/profileApi'

const userId = 1
const token = 'mock-jwt-token'

function UpdateProfile() {
  const [profile, setProfile] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const loadProfile = async () => {
      setIsLoading(true)
      setError(null)

      try {
        const existingProfile = await getProfile(userId, token)
        setProfile(existingProfile)
      } catch {
        setError('Unable to load profile.')
      } finally {
        setIsLoading(false)
      }
    }

    loadProfile()
  }, [])

  const handleUpdateProfile = async (formData) => {
    setIsLoading(true)

    try {
      const updatedProfile = await updateProfile(userId, formData, token)
      setProfile(updatedProfile)
    } catch (err) {
      throw err
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div>
      <h1>Update Profile</h1>
      <p>Review and update your profile information.</p>

      {isLoading && !profile && <p>Loading profile...</p>}
      {error && <p>{error}</p>}
      {profile && (
        <ProfileForm
          mode="update"
          initialValues={profile}
          onSubmit={handleUpdateProfile}
          isLoading={isLoading}
        />
      )}
    </div>
  )
}

export default UpdateProfile
