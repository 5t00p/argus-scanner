const scanSelect = document.getElementById("reportScanSelect");
const viewButton = document.getElementById("generateReport");
const printButton = document.getElementById("printReport");
const reportStatus = document.getElementById("reportStatus");
const report = document.getElementById("generatedReport");

const knownList = document.getElementById("knownStalkerwareList");
const highList = document.getElementById("highRiskList");
const suspiciousList = document.getElementById("suspiciousList");

let completedScans = [];

if (scanSelect) {
    loadCompletedScans();
}

async function loadCompletedScans() {
    try {
        const scans = await apiRequest("/api/scans");
        completedScans = scans.filter(function(scan) {
            return scan.scan_status === "analyzed";
        });

        scanSelect.innerHTML = '<option value="">Select a scan</option>';
        completedScans.forEach(function(scan) {
            const option = document.createElement("option");
            option.value = scan.scanID;
            option.textContent = "Scan #" + scan.scanID + " - Device #" + scan.deviceID + " - " + formatDate(scan.started_at);
            scanSelect.appendChild(option);
        });

        if (completedScans.length === 0) {
            reportStatus.textContent = "No completed scans yet.";
            return;
        }
        scanSelect.disabled = false;
        reportStatus.textContent = "Select a scan to view its report.";

        // the scans page sends you here with ?scanId= after an upload
        const scanId = new URLSearchParams(window.location.search).get("scanId");
        if (scanId && completedScans.some(scan => String(scan.scanID) === scanId)) {
            scanSelect.value = scanId;
            showReport();
        }
    } catch (error) {
        console.error("Failed to load scans:", error);
        reportStatus.textContent = "Could not load scans.";
    }
}

scanSelect.addEventListener("change", function() {
    report.hidden = true;
    printButton.disabled = true;
    viewButton.disabled = !scanSelect.value;
});

viewButton.addEventListener("click", showReport);

printButton.addEventListener("click", function() {
    document.body.classList.add("printing-report");
    window.print();
});

window.addEventListener("afterprint", function() {
    document.body.classList.remove("printing-report");
});

async function showReport() {
    const scan = completedScans.find(s => String(s.scanID) === scanSelect.value);
    if (!scan) {
        return;
    }

    viewButton.disabled = true;
    reportStatus.textContent = "Loading report...";

    try {
        const [device, findings] = await Promise.all([
            apiRequest("/api/devices/" + scan.deviceID),
            apiRequest("/api/scans/" + scan.scanID + "/findings")
        ]);

        document.getElementById("reportScanId").textContent = scan.scanID;
        document.getElementById("reportDate").textContent = formatDate(scan.started_at);
        document.getElementById("reportScanStatus").textContent = scan.scan_status;
        document.getElementById("reportDeviceId").textContent = device.deviceID;
        document.getElementById("reportDeviceModel").textContent = device.model || "Unknown model";
        document.getElementById("reportPlatform").textContent = device.platform === "ios" ? "iOS" : "Android";
        document.getElementById("reportFindingSummary").textContent = findings.length === 0
            ? "No flagged apps."
            : findings.length + " flagged app(s): " + findings.map(f => f.package_name).join(", ");
        document.getElementById("reportRiskLevel").textContent = riskLevel(findings);

        fillList(knownList, findings.filter(f => f.classification === "Known stalkerware"));
        fillList(highList, findings.filter(f => f.classification === "High risk"));
        fillList(suspiciousList, findings.filter(f => f.classification === "Suspicious"));

        report.hidden = false;
        printButton.disabled = false;
        reportStatus.textContent = "";
    } catch (error) {
        console.error("Failed to load report:", error);
        reportStatus.textContent = "Could not load the report.";
    } finally {
        viewButton.disabled = false;
    }
}

// the server already labels each finding, so the overall level just follows the worst label
function riskLevel(findings) {
    if (findings.some(f => f.classification === "Known stalkerware" || f.classification === "High risk")) {
        return "High";
    }
    return findings.length > 0 ? "Medium" : "Low";
}

function fillList(list, findings) {
    list.innerHTML = "";

    if (findings.length === 0) {
        list.innerHTML = "<li>None.</li>";
        return;
    }

    findings.forEach(function(finding) {
        const item = document.createElement("li");

        const name = document.createElement("strong");
        name.textContent = finding.package_name;

        const score = document.createElement("span");
        score.textContent = " - risk " + finding.risk_score + "%";

        const reason = document.createElement("p");
        reason.textContent = finding.reason;

        item.append(name, score, reason);
        list.appendChild(item);
    });
}