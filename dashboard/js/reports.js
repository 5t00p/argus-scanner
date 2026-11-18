document.addEventListener("DOMContentLoaded", function () {
    const scanSelect = document.getElementById("reportScanSelect");
    const viewButton = document.getElementById("generateReport");
    const printButton = document.getElementById("printReport");
    const status = document.getElementById("reportStatus");
    const report = document.getElementById("generatedReport");
    let completedScans = [];

    loadCompletedScans();

    async function loadCompletedScans() {
        status.textContent = "Loading completed scans...";
        scanSelect.disabled = true;
        viewButton.disabled = true;
        printButton.disabled = true;

        try {
            const scans = await apiRequest("/api/scans");
            if (!Array.isArray(scans)) {
                throw new Error("Invalid scan response");
            }

            completedScans = scans.filter(scan => String(scan.scan_status ?? scan.status).toLowerCase() === "analyzed"
            );

            scanSelect.replaceChildren();
            const placeholder = document.createElement("option");
            placeholder.value = "";
            placeholder.textContent = "-- Select a Scan --";
            scanSelect.appendChild(placeholder);

            completedScans.forEach(scan => {

                const option = document.createElement("option");
                option.value = scan.scanID;
                const date = formatDate(scan.started_at);

                option.textContent = `Scan #${scan.scanID} - Device #${scan.deviceID} - ${date}`;

                scanSelect.appendChild(option);
            });

            if (completedScans.length === 0) {
                status.textContent = "No completed scans are available.";
                return;
            }

            scanSelect.disabled = false;
            status.textContent = `${completedScans.length} completed scan(s) available.`;

            const params = new URLSearchParams(window.location.search);
            const requestedId = params.get("scanId");

            if (requestedId && completedScans.some(s => String(s.scanID) === requestedId)) {
                scanSelect.value = requestedId;
                viewButton.disabled = false;
                await generateReport();
            }

        } catch (error) {

            console.error(error);

            status.textContent = "Unable to load scans: " + error.message;
        }
    }




    scanSelect.addEventListener("change", function () {
        report.hidden = true;
        printButton.disabled = true;

        viewButton.disabled = !scanSelect.value;

        status.textContent = scanSelect.value ? "Click View Report to display the selected scan." : "Select a completed scan.";
    });

    viewButton.addEventListener("click", generateReport);

    printButton.addEventListener("click", function () {

        if (!report.hidden) {
            document.body.classList.add("printing-report");
            window.print();
        }
    });

    window.addEventListener("afterprint", function () {
        document.body.classList.remove("printing-report");
    });


    async function generateReport() {

        const selectedScan = completedScans.find(scan => String(scan.scanID) === scanSelect.value
        );

        if (!selectedScan) {
            status.textContent = "Please select a scan.";
            return;
        }

        viewButton.disabled = true;
        printButton.disabled = true;
        report.hidden = true;
        status.textContent = "Generating security report...";

        try {

            const [device, findings] = await Promise.all([
                apiRequest("/api/devices/" + selectedScan.deviceID
                ),
                apiRequest("/api/scans/" + selectedScan.scanID + "/findings"
                )
            ]);

            if (!device || !Array.isArray(findings)) {
                throw new Error("Invalid device or findings response");
            }

            document.getElementById("reportScanId").textContent = selectedScan.scanID;
            document.getElementById("reportDate").textContent = formatDate(selectedScan.started_at);
            document.getElementById("reportScanStatus").textContent = selectedScan.scan_status ?? "Unknown";
            document.getElementById("reportDeviceId").textContent = device.deviceID ?? "Unknown";
            document.getElementById("reportDeviceModel").textContent = device.model || "Unknown model";
            document.getElementById("reportPlatform").textContent = device.platform || "Unknown platform";


            const summaryList = document.getElementById("reportFindingSummary");


            document.getElementById("reportFindingSummary").textContent = findings.length === 0 ? "No suspicious activities detected." : findings.map(finding => finding.package_name || "Unknown application").join(", ");

            const highestScore = Math.max(
                0, ...findings.map(finding => Number(finding.risk_score) || 0)
            );

            const hasHighRiskFinding = findings.some(finding => {

                const classification = String(finding.classification || "").trim().toLowerCase();

                return classification === "known stalkerware" || classification === "high risk";
            });

            let riskLevel;

            if (hasHighRiskFinding || highestScore >= 75) {

                riskLevel = "High";

            } else if (findings.length > 0) {

                riskLevel = "Medium";

            } else {

                riskLevel = "Low";
            }

            document.getElementById("reportRiskLevel").textContent = riskLevel;


            const knownList = document.getElementById("knownStalkerwareList");

            const highList = document.getElementById("highRiskList");

            const suspiciousList = document.getElementById("suspiciousList");

            knownList.replaceChildren();
            highList.replaceChildren();
            suspiciousList.replaceChildren();

            findings.forEach(finding => {

                const classification = String(finding.classification || "").trim().toLowerCase();
                let targetList;

                if (classification === "known stalkerware") {
                    targetList = knownList;
                } else if (classification === "high risk") {
                    targetList = highList;
                } else {
                    targetList = suspiciousList;
                }

                const item = document.createElement("li");
                const heading = document.createElement("strong");
                heading.textContent = finding.package_name || "Unknown application";
                item.appendChild(heading);

                const score = document.createElement("span");
                score.textContent =" - Risk Score: " + (finding.risk_score ?? "N/A");

                item.appendChild(score);

                if (finding.reason) {
                    const reason = document.createElement("p");
                    reason.textContent = finding.reason;
                    item.appendChild(reason);
                }

                targetList.appendChild(item);
            });

            [knownList, highList, suspiciousList].forEach(list => {

                if (list.children.length === 0) {
                    const item = document.createElement("li");
                    item.textContent = "No findings.";
                    list.appendChild(item);
                }
            });

            report.hidden = false;
            printButton.disabled = false;

            status.textContent = `Report generated for Scan #${selectedScan.scanID}. ` + `${findings.length} finding(s) retrieved.`;

        } catch (error) {

            console.error("Report generation failed:", error);
            status.textContent = "Unable to generate report: " + error.message;

        } finally {
            viewButton.disabled = !scanSelect.value;
        }
    }

    function formatDate(value) {

        if (!value) {
            return "Unknown date";
        }

        const date = new Date(value);

        if (Number.isNaN(date.getTime())) {
            return value;
        }
        
        return date.toLocaleString();
    }

});
