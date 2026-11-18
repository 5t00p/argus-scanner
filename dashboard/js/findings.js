const findingsBody = document.getElementById("findingsBody");

const params = new URLSearchParams(window.location.search);
const scanId = params.get("scanId");

document.getElementById("selectedScanId").textContent =
    scanId ?? "None";

if (scanId && /^\d+$/.test(scanId)) {
    loadFindings(scanId);
} else {
    findingsBody.innerHTML =
        '<tr><td colspan="3">No valid scan selected.</td></tr>';
}

async function loadFindings(scanId) {
    try {
        const findings = await apiRequest(
            "/api/scans/" + scanId + "/findings"
        );

        findingsBody.innerHTML = "";

        if (findings.length === 0) {
            findingsBody.innerHTML =
                '<tr><td colspan="3">No flagged applications found.</td></tr>';
            return;
        }

        findings.forEach(function (finding) {
            const row = document.createElement("tr");

            const pckgCell = finding.package_name ?? "N/A";
            const badgeCell = document.createElement("td");
            const badge = document.createElement("span");
            const classification = finding.classification ?? finding.app_family ?? "Unknown";
            const lowerClass = classification.toLowerCase();

            badge.style.padding = "4px 10px";
            badge.style.borderRadius = "9999px";
            badge.style.fontSize = "0.75rem";
            badge.style.fontWeight = "700";
            badge.style.display = "inline-block";
            badge.style.color = "#ffffff";

            if (lowerClass.includes("known stalkerware") || lowerClass.includes("known_stalkerware") || lowerClass.includes("fakestalker")) {
                badge.style.backgroundColor = "#ef4444"; // Red (Meets FakeStalker acceptance criteria)
                badge.textContent = "Known stalkerware";
            } else if (lowerClass.includes("high risk") || lowerClass.includes("high_risk")) {
                badge.style.backgroundColor = "#f97316"; // Orange
                badge.textContent = "High risk";
            } else if (lowerClass.includes("suspicious")) {
                badge.style.backgroundColor = "#f59e0b"; // Amber
                badge.textContent = "Suspicious";
            } else {
                badge.style.backgroundColor = "#6b7280"; // Default Gray
                badge.textContent = classification;
            }
            badgeCell.appendChild(badge);
            row.appendChild(badgeCell);
            const reasonCell = document.createElement("td");
            reasonCell.textContent = finding.reason ?? finding.risk_score ?? "N/A";
            row.appendChild(reasonCell);
            findingsBody.appendChild(row);
        });
    } catch (error) {
        console.error("Failed to load findings:", error);

        findingsBody.innerHTML =
            '<tr><td colspan="3">Error loading findings.</td></tr>';
    }
}
