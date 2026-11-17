const devicesBody = document.getElementById("devicesBody");

if (devicesBody) {
    loadDevices();
}

async function loadDevices() {
    try {
        const devices = await apiRequest("/api/devices");

        devicesBody.innerHTML = "";

        if (devices.length === 0) {
            devicesBody.innerHTML = '<tr><td colspan="5">No devices available.</td></tr>';
            return;
        }

        devices.forEach(function(device) {
            const row = document.createElement("tr");

            const values = [
                device.deviceID,
                device.platform,
                device.model,
                device.caseID
            ];

            values.forEach(function(value) {
                const cell = document.createElement("td");
                cell.textContent = value ?? "N/A";
                row.appendChild(cell);
            });


            const actionsCell = document.createElement("td");

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";

            deleteButton.addEventListener("click", function() {
                deleteDevice(device);
            });

            actionsCell.appendChild(deleteButton);
            row.appendChild(actionsCell);
            devicesBody.appendChild(row);

        });

    } catch (error) {
        console.error("Failed to load devices:", error);
        devicesBody.innerHTML = '<tr><td colspan="5">Error loading devices.</td></tr>';
    }
}

async function deleteDevice(device) {
    const confirmed = confirm(
        "Are you sure you want to delete device " + device.deviceID + "?"
    );

    if (!confirmed) {
        return;
    }

    try {
        await apiRequest("/api/devices/" + device.deviceID, {
            method: "DELETE"
        });

        alert("Device deleted successfully!");
        await loadDevices();

    } catch (error) {
        console.error("Failed to delete device:", error);

        if (error.message.includes("409")) {
            alert("This device cannot be deleted because it has associated scans.");
        } else {
            alert("Failed to delete device.");
        }
    }
}

