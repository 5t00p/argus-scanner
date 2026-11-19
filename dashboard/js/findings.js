const findingsBody = document.getElementById("findingsBody");
const scanContext = document.getElementById("scanContext");

const scanId = new URLSearchParams(window.location.search).get("scanId");

if (scanId && /^\d+$/.test(scanId)) {
    loadScanContext(scanId);
    loadFindings(scanId);
} else {
    scanContext.textContent = "No scan selected.";
    findingsBody.innerHTML = '<tr><td colspan="3">No valid scan selected.</td></tr>';
}

/** "Scan 2 - Pixel emulator (case 1) - Nov 18, 2026, 7:23 PM" so the advocate knows whose phone this was */
async function loadScanContext(scanId) {
    try {
        const [scans, devices] = await Promise.all([apiRequest("/api/scans"), apiRequest("/api/devices")]);
        const scan = scans.find(s => String(s.scanID) === scanId);
        const device = scan && devices.find(d => d.deviceID === scan.deviceID);
        scanContext.textContent = "Scan " + scanId +
            (device ? " - " + (device.model || device.platform) + " (case " + device.caseID + ")" : "") +
            (scan ? " - " + formatDate(scan.started_at) : "");
    } catch (error) {
        scanContext.textContent = "Scan " + scanId;
    }
}

function badgeFor(classification) {
    const badge = document.createElement("span");
    const label = String(classification ?? "").toLowerCase();
    badge.className = "badge " +
        (label === "known stalkerware" ? "known" : label === "high risk" ? "high" : label === "suspicious" ? "suspicious" : "other");
    badge.textContent = classification ?? "Unknown";
    return badge;
}

async function loadFindings(scanId) {
    try {
        const findings = await apiRequest("/api/scans/" + scanId + "/findings");

        findingsBody.innerHTML = "";

        if (findings.length === 0) {
            findingsBody.innerHTML = '<tr><td colspan="3">No suspicious apps found on this device.</td></tr>';
            return;
        }

        findings.forEach(function(finding) {
            const row = document.createElement("tr");

            const appCell = document.createElement("td");
            appCell.className = "app-name";
            appCell.textContent = finding.package_name ?? "N/A";

            const riskCell = document.createElement("td");
            const score = document.createElement("span");
            score.className = "risk-score";
            score.textContent = (finding.risk_score ?? "?") + "%";
            riskCell.append(badgeFor(finding.classification), score);

            const reasonCell = document.createElement("td");
            String(finding.reason ?? "").split("; ").filter(Boolean).forEach(function(reason) {
                const chip = document.createElement("span");
                chip.className = "reason";
                chip.textContent = reason;
                reasonCell.appendChild(chip);
            });

            row.append(appCell, riskCell, reasonCell);
            findingsBody.appendChild(row);
        });
    } catch (error) {
        console.error("Failed to load findings:", error);
        findingsBody.innerHTML = '<tr><td colspan="3">Error loading findings.</td></tr>';
    }
}
