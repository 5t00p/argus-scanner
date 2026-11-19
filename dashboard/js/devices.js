const devicesBody = document.getElementById("devicesBody");
const newDeviceForm = document.getElementById("newDeviceForm");
const deviceCaseSelect = document.getElementById("deviceCase");
const deviceStatus = document.getElementById("deviceStatus");

let casesById = {};

if (devicesBody) {
    loadPage();
}

async function loadPage() {
    await loadCases();
    await loadDevices();
}

function caseLabel(caseID) {
    const vCase = casesById[caseID];
    return vCase ? "Case " + caseID + " - " + (vCase.survivorRef ?? "") : "Case " + (caseID ?? "N/A");
}

async function loadCases() {
    try {
        const cases = await apiRequest("/api/cases");
        casesById = {};
        deviceCaseSelect.innerHTML = "";

        cases.forEach(function(vCase) {
            casesById[vCase.caseID] = vCase;
            if (vCase.status === "closed") {
                return; // new devices only go on open cases
            }
            const option = document.createElement("option");
            option.value = vCase.caseID;
            option.textContent = caseLabel(vCase.caseID);
            deviceCaseSelect.appendChild(option);
        });

        if (deviceCaseSelect.options.length === 0) {
            const option = document.createElement("option");
            option.value = "";
            option.textContent = "No open cases - open one on the Cases page";
            deviceCaseSelect.appendChild(option);
        }
    } catch (error) {
        console.error("Failed to load cases:", error);
        deviceStatus.textContent = "Could not load cases.";
    }
}

async function loadDevices() {
    try {
        const devices = await apiRequest("/api/devices");

        devicesBody.innerHTML = "";

        if (devices.length === 0) {
            devicesBody.innerHTML = '<tr><td colspan="5">No devices yet. Add one above.</td></tr>';
            return;
        }

        devices.forEach(function(device) {
            const row = document.createElement("tr");

            [
                device.deviceID,
                device.platform === "ios" ? "iOS" : "Android",
                device.model,
                caseLabel(device.caseID)
            ].forEach(function(value) {
                const cell = document.createElement("td");
                cell.textContent = value ?? "N/A";
                row.appendChild(cell);
            });

            const actionsCell = document.createElement("td");
            actionsCell.className = "actions";

            const pairButton = document.createElement("button");
            pairButton.textContent = "Pair scanner";
            pairButton.addEventListener("click", function() {
                pairScanner(device);
            });

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.className = "danger";
            deleteButton.addEventListener("click", function() {
                deleteDevice(device);
            });

            actionsCell.append(pairButton, deleteButton);
            row.appendChild(actionsCell);
            devicesBody.appendChild(row);
        });

    } catch (error) {
        console.error("Failed to load devices:", error);
        devicesBody.innerHTML = '<tr><td colspan="5">Error loading devices.</td></tr>';
    }
}

if (newDeviceForm) {
    newDeviceForm.addEventListener("submit", async function(event) {
        event.preventDefault();
        if (!deviceCaseSelect.value) {
            deviceStatus.textContent = "Open a case first, then add the device to it.";
            return;
        }
        const modelInput = document.getElementById("deviceModel");

        try {
            const created = await apiRequest("/api/devices", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    platform: document.getElementById("devicePlatform").value,
                    model: modelInput.value.trim(),
                    caseID: Number(deviceCaseSelect.value)
                })
            });
            deviceStatus.textContent = "Device " + created.deviceID + " added to " + caseLabel(created.caseID) + ".";
            modelInput.value = "";
            await loadDevices();
        } catch (error) {
            console.error("Failed to add device:", error);
            deviceStatus.textContent = "Could not add the device. " + error.message;
        }
    });
}

async function pairScanner(device) {
    const box = document.getElementById("pairingBox");
    try {
        const result = await apiRequest("/api/devices/" + device.deviceID + "/pairing-code", { method: "POST" });

        document.getElementById("pairingCode").textContent = result.code;
        document.getElementById("pairingInfo").textContent =
            "For " + (device.model || "device " + device.deviceID) + " (" + caseLabel(device.caseID) + "). " +
            "Enter this code in the Argus scanner app on the survivor's phone, then tap Scan device. " +
            "It works once and expires in " + result.expires_in_minutes + " minutes.";
        box.hidden = false;
        box.scrollIntoView({ behavior: "smooth", block: "center" });
    } catch (error) {
        console.error("Failed to create pairing code:", error);
        alert("Could not create a pairing code. " + error.message);
    }
}

async function deleteDevice(device) {
    if (!confirm("Delete device " + device.deviceID + "? This cannot be undone.")) {
        return;
    }

    try {
        await apiRequest("/api/devices/" + device.deviceID, { method: "DELETE" });
        await loadDevices();
    } catch (error) {
        console.error("Failed to delete device:", error);
        if (error.message.includes("409")) {
            alert("This device has scans, so it can't be deleted.");
        } else {
            alert("Could not delete the device. " + error.message);
        }
    }
}
