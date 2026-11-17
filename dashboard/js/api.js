const API_BASE = "";

async function apiRequest(endpoint, options = {}) {
    const response = await fetch(API_BASE + endpoint, options);

    if (!response.ok) {
        throw new Error("Server error: " + response.status);
    }

    if (response.status === 204) {
        return null;
    }

    return await response.json();
}

