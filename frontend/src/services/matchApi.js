const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

export async function getPotentialMatches(userId, token) {
  const headers = {}

  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const response = await fetch(
    `${API_BASE_URL}/users/${userId}/potential-matches`,
    {
      method: 'GET',
      headers,
    },
  )

  if (!response.ok) {
    const errorText = await response.text()
    const requestError = new Error(errorText)
    requestError.status = response.status
    throw requestError
  }

  return response.json()
}
