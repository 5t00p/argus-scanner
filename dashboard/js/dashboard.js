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
            .sort((a, b) => new Date(b.started_at) - new Date(a.started_at))[0];

        lastScanElement.textContent = latestScan ? formatDate(latestScan.started_at) : "Unknown";

        const findingsByScan = await Promise.all(
            scans.map(scan => apiRequest("/api/scans/" + scan.scanID + "/findings"))
        );
        const findings = findingsByScan.flat();

        // same labels the findings page shows, so the numbers always agree
        threatsElement.textContent = findings.length;
        highRiskElement.textContent = findings.filter(finding =>
            finding.classification === "Known stalkerware" || finding.classification === "High risk"
        ).length;

    } catch (error) {
        console.error("Failed to load dashboard:", error);
        threatsElement.textContent = "Error";
        highRiskElement.textContent = "Error";
        lastScanElement.textContent = "N/A";
    }
}

loadDashboard();
