const casesBody = document.getElementById("casesBody");

if (casesBody) {
    loadCases();
}

async function loadCases() {
    try {
        const cases = await apiRequest("/api/cases");

        casesBody.innerHTML = "";

        if (cases.length === 0) {
            casesBody.innerHTML = '<tr><td colspan="4">No cases available.</td></tr>';
            return;
        }

        cases.forEach(function(vCase) {
            const row = document.createElement("tr");

            const values = [
                vCase.caseID,
                vCase.status,
                vCase.survivorRef
            ];

            values.forEach(function(value) {
                const cell = document.createElement("td");
                cell.textContent = value ?? "N/A";
                row.appendChild(cell);
            });


            const actionsCell = document.createElement("td");

            const editButton = document.createElement("button");
            editButton.textContent = "Edit";

            editButton.addEventListener("click", function() {
                showEditForm(vCase);
            });

            
            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";

            deleteButton.addEventListener("click", function() {
                deleteCase(vCase);
            });

            actionsCell.appendChild(editButton);
            actionsCell.appendChild(deleteButton);
            row.appendChild(actionsCell);



            casesBody.appendChild(row);
        });

    } catch (error) {
        console.error("Failed to load cases:", error);
        casesBody.innerHTML = '<tr><td colspan="4">Error loading cases.</td></tr>';
    }
}


async function showEditForm(vCase) {
    const newStatus = prompt(
        "Enter the new case status:",
        vCase.status ?? ""
    );

    if (newStatus === null) {
        return;
    }

    const newSurvivorRef = prompt(
        "Enter the new survivor reference:",
        vCase.survivorRef ?? ""
    );

    if (newSurvivorRef === null) {
        return;
    }

    const updatedCase = {
        status: newStatus,
        survivorRef: newSurvivorRef
    };

    try {
        await apiRequest("/api/cases/" + vCase.caseID, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updatedCase)
        });

        alert("Case updated successfully!");

        await loadCases();

    } catch (error) {
        console.error("Failed to update case:", error);
        alert("Failed to update case.");
    }
}


async function deleteCase(vCase) {
    const confirmed = confirm(
        "Are you sure you want to delete case " + vCase.caseID + "?"
    );

    if (!confirmed) {
        return;
    }

    try {
        await apiRequest("/api/cases/" + vCase.caseID, {
            method: "DELETE"
        });

        alert("Case deleted successfully!");
        await loadCases();

    } catch (error) {
        console.error("Failed to delete case:", error);

        if (error.message.includes("409")) {
            alert("This case cannot be deleted because it has associated records.");
        } else {
            alert("Failed to delete case.");
        }
    }
}

