document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("login-form");
    const loginMessage = document.getElementById("login-message");

    if (!loginForm || !loginMessage) {
        return;
    }

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;

        if (!email || !password) {
            loginMessage.textContent =
                "Please enter your email and password.";
            return;
        }

        try {
            const response = await fetch("/api/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email: email, password: password })
            });
            const result = await response.json();

            if (!response.ok) {
                loginMessage.textContent = result.error || "Login failed.";
                return;
            }

            sessionStorage.setItem("argusToken", result.token);
            sessionStorage.setItem("argusUser", result.name || email);
            window.location.href = "index.html";
        } catch (error) {
            console.error("Login failed:", error);
            loginMessage.textContent = "Could not reach the server.";
        }
    });
});
