const API_BASE = window.location.origin;

async function apiRequest(endpoint, options = {}) {
    // sessionStorage (not localStorage) so closing the tab logs out, which matters on a shared computer
    const token = sessionStorage.getItem("argusToken");
    const headers = { ...(options.headers || {}) };
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const response = await fetch(API_BASE + endpoint, { ...options, headers });

    if (response.status === 401) {
        window.location.href = "login.html";
        throw new Error("Server error: 401");
    }

    if (!response.ok) {
        // keep the status code in the message (pages check for "409") and add the server's reason
        let reason = "";
        try {
            reason = (await response.json()).error ?? "";
        } catch (error) {
            // no JSON body
        }
        throw new Error("Server error: " + response.status + (reason ? " - " + reason : ""));
    }

    if (response.status === 204) {
        return null;
    }

    return await response.json();
}

async function logout() {
    try {
        await apiRequest("/api/logout", { method: "POST" });
    } catch (error) {
        // already logged out on the server; still clear this tab
    }
    sessionStorage.removeItem("argusToken");
    sessionStorage.removeItem("argusUser");
    window.location.href = "login.html";
}

/** "2026-11-18T19:23:45" -> "Nov 18, 2026, 7:23 PM" */
function formatDate(value) {
    if (!value) {
        return "N/A";
    }
    const date = new Date(value);
    return isNaN(date) ? value : date.toLocaleString(undefined, {
        month: "short", day: "numeric", year: "numeric", hour: "numeric", minute: "2-digit"
    });
}
