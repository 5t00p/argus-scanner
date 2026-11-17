const scanHistoryBody = document.getElementById("scanHistoryBody");

if (scanHistoryBody) {
    loadScanHistory();
}

async function loadScanHistory() {
    try {
        
        const scans = await apiRequest("/api/scans");

        scanHistoryBody.innerHTML = "";

        if (scans.length === 0) {
            scanHistoryBody.innerHTML =
                '<tr><td colspan="5">No previous scans available.</td></tr>';
            return;
        }

        scans.forEach(function(scan) {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${scan.scanID}</td>
                <td>${scan.started_at ?? "N/A"}</td>
                <td>${scan.deviceID}</td>
                <td>${scan.scan_status ?? "N/A"}</td>
                <td><a href="findings.html?scanId=${scan.scanID}">View Findings</a></td>
            `;

            scanHistoryBody.appendChild(row);
        });
        //basic error handling
    } catch (error) {
        console.error("Failed to load scan history:", error);

        scanHistoryBody.innerHTML =
            '<tr><td colspan="5">Error loading scan history.</td></tr>';
    }
}


const uploadScanForm = document.getElementById("uploadScanForm");

if (uploadScanForm) {
    uploadScanForm.addEventListener("submit", async function(event) {
        event.preventDefault();

        const scanFile = document.getElementById("scanFile");
        const uploadStatus = document.getElementById("uploadStatus");
        const uploadButton = document.getElementById("uploadScan");

        if (scanFile.files.length === 0) {
            uploadStatus.textContent = "Please select a JSON file.";
            return;
        }

        uploadButton.disabled = true;
        uploadStatus.textContent = "Uploading scan...";

        try {
            const fileText = await scanFile.files[0].text();

            const scanData = JSON.parse(fileText);

            const result = await apiRequest("/api/devices/1/scans", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(scanData)
            });

            uploadStatus.textContent =
                "Scan uploaded successfully! Scan ID: " + result.scan_id;

            await loadScanHistory();

            scanFile.value = "";

        } catch (error) {
            console.error("Scan upload failed:", error);
            uploadStatus.textContent =
                "Upload failed: " + error.message;
        } finally {
            uploadButton.disabled = false;
        }
    });
}


