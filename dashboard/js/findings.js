const findingsBody = document.getElementById("findingsBody");

// Read the scan ID from the URL
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

        findings.forEach(function(finding) {
            const row = document.createElement("tr");

            // Use textContent to safely display server data
            [
                finding.package_name,
                finding.risk_score,
                finding.reason
            ].forEach(function(value) {
                const cell = document.createElement("td");
                cell.textContent = value ?? "N/A";
                row.appendChild(cell);
            });

            findingsBody.appendChild(row);
        });

    } catch (error) {
        console.error("Failed to load findings:", error);

        findingsBody.innerHTML =
            '<tr><td colspan="3">Error loading findings.</td></tr>';
    }
}
