const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api/v1";

// 1. ADD THIS FUNCTION to fetch mutual matches from your MatchController
export async function getMatches(userId, token) {
  const response = await fetch(`${API_BASE_URL}/matches/${userId}/matches`, {
    method: "GET",
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });

  if (!response.ok) {
    throw new Error("Unable to load matched users");
  }

  return response.json(); // Returns array of matched user IDs: [2, 5, 8]
}

export async function getChatUsers(userId, token) {
  const response = await fetch(`${API_BASE_URL}/messages/users/${userId}`, {
    method: "GET",
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });

  if (!response.ok) {
    throw new Error("Unable to load chat users");
  }

  return response.json();
}

export async function getMessages(userA, userB, token) {
  const response = await fetch(`${API_BASE_URL}/messages/${userA}/${userB}`, {
    method: "GET",
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });

  if (!response.ok) {
    throw new Error("Unable to load messages");
  }

  return response.json();
}

export async function sendMessage(payload, token) {
  const response = await fetch(`${API_BASE_URL}/messages`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error("Unable to send message");
  }

  return response.json();
}
