import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import ProfileForm from './ProfileForm'
import { getProfile, createProfile, updateProfile } from '../services/profileApi'

function ProfileManager() {
  const userId = localStorage.getItem('userId')
  const token = localStorage.getItem('authToken')

  const [profile, setProfile] = useState(null)
  const [hasProfile, setHasProfile] = useState(false)
  const [isLoading, setIsLoading] = useState(Boolean(userId))

  useEffect(() => {
    if (!userId) {
      return
    }

    const loadProfile = async () => {
      setIsLoading(true)
      try {
        const existingProfile = await getProfile(userId, token)
        if (existingProfile) {
          setProfile(existingProfile)
          setHasProfile(true)
        }
      } catch {
        setHasProfile(false)
      } finally {
        setIsLoading(false)
      }
    }

    loadProfile()
  }, [userId, token])

  const handleSaveProfile = async (formData) => {
    setIsLoading(true)
    try {
      if (hasProfile) {
        const updatedProfile = await updateProfile(userId, formData, token)
        setProfile(updatedProfile)
      } else {
        const newProfile = await createProfile(userId, formData, token)
        setProfile(newProfile)
        setHasProfile(true)
      }
    } finally {
      setIsLoading(false)
    }
  }

  if (!userId) {
    return (
      <div>
        <h1>My Profile</h1>
        <p>Unable to identify the logged-in user. Please log in again.</p>
      </div>
    )
  }

  return (
    <div>
      {/* 3. Dynamically change headers based on state */}
      <h1>{hasProfile ? 'Update Profile' : 'Create Profile'}</h1>
      <p>
        {hasProfile
          ? 'Review and update your profile information.'
          : 'Welcome to Outty! Complete your profile below.'}
      </p>

      {hasProfile && (
        <p>
          <Link className="matches-link" to="/matches">
            Find Matches
          </Link>
        </p>
      )}

      {isLoading && !profile ? (
        <p>Loading profile details...</p>
      ) : (
        <ProfileForm
          key={hasProfile ? 'update' : 'create'}
          mode={hasProfile ? 'update' : 'create'}
          initialValues={profile || undefined}
          onSubmit={handleSaveProfile}
          isLoading={isLoading}
        />
      )}
    </div>
  )
}

export default ProfileManager