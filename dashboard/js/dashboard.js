
async function loadDashboard() {
    const lastScanElement = document.getElementById("lastScan");
    const threatsElement = document.getElementById("threatsDetected");
    const highRiskElement = document.getElementById("highRiskFindings");

    try {
        const scans = await apiRequest("/api/scans");

        if (scans.length === 0) {
            lastScanElement.textContent = "Never";
            threatsElement.textContent = "0";
            highRiskElement.textContent = "0";
            return;
        }

        const latestScan = scans
            .filter(scan => scan.started_at)
            .sort((a, b) =>
                new Date(b.started_at) - new Date(a.started_at)
            )[0];

        if (latestScan) {
            const scanDate = new Date(latestScan.started_at);

            lastScanElement.textContent = isNaN(scanDate.getTime())
                ? latestScan.started_at
                : scanDate.toLocaleString();
        } else {
            lastScanElement.textContent = "Unknown";
        }

        const findingsByScan = await Promise.all(
            scans.map(scan =>
                apiRequest("/api/scans/" + scan.scanID + "/findings")
            )
        );

        let totalThreats = 0;
        let highRiskFindings = 0;

        findingsByScan.forEach(function(findings) {
            totalThreats += findings.length;

            findings.forEach(function(finding) {

                if (String(finding.classification || "")trim().toLowerCase() === "high risk") {
                    highRiskFindings++;
                }
            });

        threatsElement.textContent = totalThreats;
        highRiskElement.textContent = highRiskFindings;

    } catch (error) {
        console.error("Failed to load dashboard:", error);

        threatsElement.textContent = "Error";
        highRiskElement.textContent = "Error";
        appsElement.textContent = "N/A";
    }
}

loadDashboard();
