const scanHistoryBody = document.getElementById("scanHistoryBody");
const uploadDeviceSelect = document.getElementById("uploadDevice");

let devicesById = {};

if (scanHistoryBody) {
    loadPage();
}

async function loadPage() {
    await loadDevices();
    await loadScanHistory();
}

function deviceLabel(deviceID) {
    const device = devicesById[deviceID];
    return device ? (device.model || device.platform || "Device") + " (#" + deviceID + ")" : "#" + deviceID;
}

async function loadDevices() {
    try {
        const devices = await apiRequest("/api/devices");
        devicesById = {};
        uploadDeviceSelect.innerHTML = "";

        devices.forEach(function(device) {
            devicesById[device.deviceID] = device;
            const option = document.createElement("option");
            option.value = device.deviceID;
            option.textContent = deviceLabel(device.deviceID) + ", case " + (device.caseID ?? "N/A");
            uploadDeviceSelect.appendChild(option);
        });

        if (devices.length === 0) {
            const option = document.createElement("option");
            option.value = "";
            option.textContent = "No devices - add one on the Devices page";
            uploadDeviceSelect.appendChild(option);
        }
    } catch (error) {
        console.error("Failed to load devices:", error);
    }
}

async function loadScanHistory() {
    try {
        const scans = await apiRequest("/api/scans");

        scanHistoryBody.innerHTML = "";

        if (scans.length === 0) {
            scanHistoryBody.innerHTML = '<tr><td colspan="5">No scans yet.</td></tr>';
            return;
        }

        scans.forEach(function(scan) {
            const row = document.createElement("tr");

            [scan.scanID, formatDate(scan.started_at), deviceLabel(scan.deviceID), scan.scan_status ?? "N/A"]
                .forEach(function(value) {
                    const cell = document.createElement("td");
                    cell.textContent = value;
                    row.appendChild(cell);
                });

            const linkCell = document.createElement("td");
            const link = document.createElement("a");
            link.href = "findings.html?scanId=" + encodeURIComponent(scan.scanID);
            link.textContent = "View findings";
            linkCell.appendChild(link);
            row.appendChild(linkCell);

            scanHistoryBody.appendChild(row);
        });
    } catch (error) {
        console.error("Failed to load scan history:", error);
        scanHistoryBody.innerHTML = '<tr><td colspan="5">Error loading scan history.</td></tr>';
    }
}

const uploadScanForm = document.getElementById("uploadScanForm");

if (uploadScanForm) {
    uploadScanForm.addEventListener("submit", async function(event) {
        event.preventDefault();

        const scanFile = document.getElementById("scanFile");
        const uploadStatus = document.getElementById("uploadStatus");
        const uploadButton = document.getElementById("uploadScan");

        if (!uploadDeviceSelect.value) {
            uploadStatus.textContent = "Add the device on the Devices page first.";
            return;
        }
        if (scanFile.files.length === 0) {
            uploadStatus.textContent = "Please select a JSON file.";
            return;
        }

        uploadButton.disabled = true;
        uploadStatus.textContent = "Uploading scan...";

        try {
            const scanData = JSON.parse(await scanFile.files[0].text());

            const result = await apiRequest("/api/devices/" + uploadDeviceSelect.value + "/scans", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(scanData)
            });

            if (result.scan_id == null) {
                throw new Error("Server did not return a scan ID.");
            }

            uploadStatus.textContent = "Scan analyzed. Opening report...";
            scanFile.value = "";
            window.location.href = "reports.html?scanId=" + encodeURIComponent(result.scan_id);

        } catch (error) {
            console.error("Scan upload failed:", error);
            uploadStatus.textContent = "Upload failed: " + error.message;
        } finally {
            uploadButton.disabled = false;
        }
    });
}
