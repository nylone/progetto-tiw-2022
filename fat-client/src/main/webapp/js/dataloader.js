(function () {
    // declarations
    function _loadMeetings(type, tableID, formatter) {
        const data = new FormData();
        data.append("type", type);
        const endpoint = "/meetings";
        const request = new XMLHttpRequest();
        const table = document.getElementById(tableID);

        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) {
                    const meetings = JSON.parse(request.responseText);
                    if (meetings.length > 0) {
                        while (table.rows.length > 1) {
                            table.deleteRow(1);
                        }
                        table.parentElement.classList.remove("hide");
                        meetings.forEach((meeting) => formatter(meeting, table));
                    }
                }
            }
        };

        request.open("post", endpoint, true);
        request.send(data);
    }
    function loadCreatedMeetings() {
        _loadMeetings("created", "created-meetings-table", (meeting, table) => {
            const row = document.createElement("tr");
            table.append(row);
            const title = row.insertCell();
            const date = row.insertCell();
            const time = row.insertCell();
            const duration = row.insertCell();
            const capacity = row.insertCell();

            title.innerText = meeting.title;
            date.innerText = meeting.date;
            time.innerText = meeting.time;
            duration.innerText = meeting.duration;
            capacity.innerText = meeting.capacity;
        })
    }
    function loadInvitedMeetings() {
        _loadMeetings("invited", "invited-meetings-table", (meeting, table) => {
            const row = document.createElement("tr");
            table.append(row);
            const admin = row.insertCell();
            const title = row.insertCell();
            const date = row.insertCell();
            const time = row.insertCell();
            const duration = row.insertCell();
            const capacity = row.insertCell();

            title.innerText = meeting.title;
            date.innerText = meeting.date;
            time.innerText = meeting.time;
            duration.innerText = meeting.duration;
            capacity.innerText = meeting.capacity;
            admin.innerText = meeting.admin.email;
        })
    }

    function loadUsers() {
        const endpoint = "/users";
        const request = new XMLHttpRequest();
        const table = document.getElementById("select-users-table");

        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) {
                    const meetings = JSON.parse(request.responseText);
                    if (meetings.length > 0) {
                        while (table.rows.length > 1) {
                            table.deleteRow(1);
                        }
                        table.parentElement.classList.remove("hide");
                        meetings.forEach((user) => {
                            const row = document.createElement("tr");
                            const checkbox = document.createElement("input");
                            const email = document.createElement("label");

                            table.append(row);
                            row.insertCell().appendChild(checkbox);
                            row.insertCell().appendChild(email);

                            checkbox.id = user.id;
                            checkbox.value = user.id;
                            checkbox.name = "selected";
                            checkbox.type = "checkbox";

                            email.setAttribute("for", user.id);
                            email.innerText = user.email;
                        });
                    }
                }
            }
        };

        request.open("post", endpoint, true);
        request.send();
    }


    // execution
    {
        const load = () => {
          loadUsers();
          loadCreatedMeetings();
          loadInvitedMeetings();
        }

        const refresh = document.getElementById("refresh-meetings")
        refresh.onclick = load;
        refresh.onkeydown = load;

        load();
    }
})()