

async function loadSignatures() {
    const status = document.getElementById("signatureStatus");
    const list = document.getElementById("signatureList");

    try {
        const signatures = await apiRequest("/api/signatures");

        list.innerHTML = "";

        if (!Array.isArray(signatures)) {
            throw new Error("Invalid signatures response");
        }

        if (signatures.length === 0) {
            status.textContent = "No detection signatures found.";
            return;
        }

        status.textContent = `Showing ${signatures.length} detection signatures.`;

        signatures.forEach(function(signature) {
            const item = document.createElement("li");

            const packageName = document.createElement("strong");
            packageName.textContent = signature.packageName ?? "Unknown package";

            const details = document.createElement("p");
            details.textContent =
                "ID: " + (signature.signaturesID ?? "N/A") +
                " | Type: " + (signature.type ?? "N/A") +
                " | Family: " + (signature.appFamily ?? "N/A") +
                " | Certificate Hash: " + (signature.certHash ?? "N/A");

            item.appendChild(packageName);
            item.appendChild(details);
            list.appendChild(item);
        });

    } catch (error) {
        console.error("Failed to load signatures:", error);
        status.textContent = "Unable to load detection signatures.";
    }
}

loadSignatures();
