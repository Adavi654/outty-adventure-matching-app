const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

function buildHeaders(token, includeJson = false) {
  const headers = {}

  if (includeJson) {
    headers['Content-Type'] = 'application/json'
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  return headers
}

async function handleResponse(response) {
  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(errorText)
  }

  return response.json()
}

export async function createProfile(userId, profileData, token) {
  const response = await fetch(`${API_BASE_URL}/users/${userId}/profile`, {
    method: 'POST',
    headers: buildHeaders(token, true),
    body: JSON.stringify(profileData),
  })

  return handleResponse(response)
}

export async function getProfile(userId, token) {
  const response = await fetch(`${API_BASE_URL}/users/${userId}/profile`, {
    method: 'GET',
    headers: buildHeaders(token),
  })

  return handleResponse(response)
}

export async function updateProfile(userId, profileData, token) {
  const response = await fetch(`${API_BASE_URL}/users/${userId}/profile`, {
    method: 'PUT',
    headers: buildHeaders(token, true),
    body: JSON.stringify(profileData),
  })

  return handleResponse(response)
}
