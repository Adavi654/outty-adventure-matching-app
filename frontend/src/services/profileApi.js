export async function createProfile(userId, profileData, token) {
  console.log({ userId, profileData, token })

  return Promise.resolve({
    success: true,
    message: 'Mock profile created',
  })
}
