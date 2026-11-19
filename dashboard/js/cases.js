const casesBody = document.getElementById("casesBody");
const newCaseForm = document.getElementById("newCaseForm");
const caseStatus = document.getElementById("caseStatus");

if (casesBody) {
    loadCases();
}

if (newCaseForm) {
    newCaseForm.addEventListener("submit", async function(event) {
        event.preventDefault();
        const refInput = document.getElementById("newSurvivorRef");

        try {
            const created = await apiRequest("/api/cases", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ survivorRef: refInput.value.trim(), status: "open" })
            });
            caseStatus.textContent = "Case " + created.caseID + " opened.";
            refInput.value = "";
            await loadCases();
        } catch (error) {
            console.error("Failed to open case:", error);
            caseStatus.textContent = "Could not open the case. " + error.message;
        }
    });
}

async function loadCases() {
    try {
        const cases = await apiRequest("/api/cases");

        casesBody.innerHTML = "";

        if (cases.length === 0) {
            casesBody.innerHTML = '<tr><td colspan="5">No cases yet. Open one above.</td></tr>';
            return;
        }

        cases.forEach(function(vCase) {
            const row = document.createElement("tr");

            [vCase.caseID, vCase.survivorRef].forEach(function(value) {
                const cell = document.createElement("td");
                cell.textContent = value ?? "N/A";
                row.appendChild(cell);
            });

            const statusCell = document.createElement("td");
            const pill = document.createElement("span");
            pill.className = "status-pill " + (vCase.status === "closed" ? "closed" : "open");
            pill.textContent = vCase.status === "closed" ? "Closed" : "Open";
            statusCell.appendChild(pill);
            row.appendChild(statusCell);

            const openedCell = document.createElement("td");
            openedCell.textContent = formatDate(vCase.openedAt);
            row.appendChild(openedCell);

            const actionsCell = document.createElement("td");
            actionsCell.className = "actions";

            const isClosed = vCase.status === "closed";
            const statusButton = document.createElement("button");
            statusButton.textContent = isClosed ? "Reopen case" : "Close case";
            statusButton.className = "secondary";
            statusButton.addEventListener("click", function() {
                setStatus(vCase, isClosed ? "open" : "closed");
            });

            const editButton = document.createElement("button");
            editButton.textContent = "Edit reference";
            editButton.className = "secondary";
            editButton.addEventListener("click", function() {
                editReference(vCase);
            });

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.className = "danger";
            deleteButton.addEventListener("click", function() {
                deleteCase(vCase);
            });

            actionsCell.append(statusButton, editButton, deleteButton);
            row.appendChild(actionsCell);
            casesBody.appendChild(row);
        });

    } catch (error) {
        console.error("Failed to load cases:", error);
        casesBody.innerHTML = '<tr><td colspan="5">Error loading cases.</td></tr>';
    }
}

async function updateCase(vCase, changes) {
    await apiRequest("/api/cases/" + vCase.caseID, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(changes)
    });
    await loadCases();
}

async function setStatus(vCase, status) {
    try {
        await updateCase(vCase, { status: status });
    } catch (error) {
        console.error("Failed to change status:", error);
        alert("Could not change the case status. " + error.message);
    }
}

async function editReference(vCase) {
    const newRef = prompt("Survivor reference (up to 100 characters):", vCase.survivorRef ?? "");
    if (newRef === null) {
        return;
    }
    try {
        await updateCase(vCase, { survivorRef: newRef });
    } catch (error) {
        console.error("Failed to update case:", error);
        alert("Could not update the case. " + error.message);
    }
}

async function deleteCase(vCase) {
    if (!confirm("Delete case " + vCase.caseID + "? This cannot be undone.")) {
        return;
    }

    try {
        await apiRequest("/api/cases/" + vCase.caseID, { method: "DELETE" });
        await loadCases();
    } catch (error) {
        console.error("Failed to delete case:", error);
        if (error.message.includes("409")) {
            alert("This case has devices, assignments, or audit records, so it can't be deleted. Close it instead.");
        } else {
            alert("Could not delete the case. " + error.message);
        }
    }
}
