export async function createProfile(userId, profileData, token) {
  console.log({ userId, profileData, token })

  return Promise.resolve({
    success: true,
    message: 'Mock profile created',
  })
}

export async function getProfile(userId, token) {
  console.log({ userId, token })

  return Promise.resolve({
    id: 1,
    userId,
    city: 'Atlanta',
    state: 'Georgia',
    country: 'United States',
    gender: 'Male',
    birthDate: '1990-01-01',
    bio: 'Outdoor enthusiast looking for new adventures.',
    interestedIn: 'BOTH',
    relationshipGoal: 'Long-term relationship',
    createdAt: '2026-07-11T12:00:00',
    updatedAt: '2026-07-11T12:00:00',
  })
}

export async function updateProfile(userId, profileData, token) {
  console.log({ userId, profileData, token })

  return Promise.resolve({
    id: 1,
    userId,
    ...profileData,
    createdAt: '2026-07-11T12:00:00',
    updatedAt: new Date().toISOString(),
  })
}
