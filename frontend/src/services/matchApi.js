const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api/v1";

export async function getPotentialMatches(userId, token) {
  const headers = {};

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(
    `${API_BASE_URL}/users/${userId}/potential-matches`,
    {
      method: "GET",
      headers,
    },
  );

  if (!response.ok) {
    const errorText = await response.text();
    const requestError = new Error(errorText);
    requestError.status = response.status;
    throw requestError;
  }

  return response.json();
}

export async function sendSwipeDecision(userId, targetId, decision, token) {
  const headers = {
    "Content-Type": "application/json",
  };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(
    `${API_BASE_URL}/matches/${userId}/swipe`,
    {
      method: "POST",
      headers,
      body: JSON.stringify({ targetId, decision }),
    }
  );

  if (!response.ok) {
    const errorText = await response.text();
    const requestError = new Error(errorText);
    requestError.status = response.status;
    throw requestError;
  }

  return response.json();
}

export async function getMatches(userId, token) {
  const response = await fetch(`${API_BASE_URL}/${userId}/matches`, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error("Failed to fetch matches");
  }

  return response.json(); // returns array of matched user IDs e.g. [2, 5, 8]
}
