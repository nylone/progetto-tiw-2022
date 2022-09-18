{
    { // handles logout
        const id = "logout";
        const endpoint = "/logout";
        const request = new XMLHttpRequest();

        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) window.location.assign("auth.html");
            }
        };

        document.getElementById(id).onclick = () => {
            request.open("post", endpoint, true);
            request.send();
        };
    }

    let loadData;
    let users = [];
    {   // DATA LOADING FROM SERVER
        function _loadMeetings(type, tableID, emptyID, formatter) {
            const data = new FormData();
            data.append("type", type);
            const endpoint = "/meetings";
            const request = new XMLHttpRequest();
            const table = document.getElementById(tableID);
            const emptyMSG = document.getElementById(emptyID);

            request.onreadystatechange = () => {
                if (request.readyState === 4) {
                    if (request.status === 200) {
                        while (table.rows.length > 1) {
                            table.deleteRow(1);
                        }

                        const meetings = JSON.parse(request.responseText);
                        if (meetings.length > 0) {
                            emptyMSG.hidden = true;
                            table.hidden = false;
                            meetings.forEach((meeting) => formatter(meeting, table));
                        } else {
                            emptyMSG.hidden = false;
                            table.hidden = true;
                        }
                    }
                }
            };

            request.open("post", endpoint, true);
            request.send(data);
        }

        function loadCreatedMeetings() {
            _loadMeetings("created", "created-meetings-table", "no-own-meetings-message", (meeting, table) => {
                const row = document.createElement("tr");
                table.append(row);
                const title = row.insertCell();
                const date = row.insertCell();
                const time = row.insertCell();
                const duration = row.insertCell();
                const capacity = row.insertCell();

                title.innerText = meeting.title;
                date.innerText = luxon.DateTime.fromMillis(meeting.start).toLocaleString(luxon.DateTime.DATE_MED_WITH_WEEKDAY);
                time.innerText = luxon.DateTime.fromMillis(meeting.start).toLocaleString(luxon.DateTime.TIME_SIMPLE);
                duration.innerText = String(Math.floor(Number(meeting.duration) / 60)) + " hours " + String(Math.floor(Number(meeting.duration) % 60)) + " minutes";
                capacity.innerText = meeting.capacity;
            })
        }

        function loadInvitedMeetings() {
            _loadMeetings("invited", "invited-meetings-table", "no-other-meetings-message", (meeting, table) => {
                const row = document.createElement("tr");
                table.append(row);

                const admin = row.insertCell();
                const title = row.insertCell();
                const date = row.insertCell();
                const time = row.insertCell();
                const duration = row.insertCell();
                const capacity = row.insertCell();

                title.innerText = meeting.title;
                date.innerText = luxon.DateTime.fromMillis(meeting.start).toLocaleString(luxon.DateTime.DATE_MED_WITH_WEEKDAY);
                time.innerText = luxon.DateTime.fromMillis(meeting.start).toLocaleString(luxon.DateTime.TIME_SIMPLE);
                duration.innerText = String(Math.floor(Number(meeting.duration) / 60)) + " hours " + String(Math.floor(Number(meeting.duration) % 60)) + " minutes";
                capacity.innerText = meeting.capacity;
                admin.innerText = meeting.admin.uname;
            })
        }

        function loadUsers() {
            const endpoint = "/users";
            const request = new XMLHttpRequest();
            const table = document.getElementById("select-users-table");

            request.onreadystatechange = () => {
                if (request.readyState === 4) {
                    if (request.status === 200) {
                        while (table.rows.length > 1) {
                            table.deleteRow(1);
                        }

                        users = JSON.parse(request.responseText);
                        if (users.length > 0) {
                            document.getElementById("no-users-message").hidden = true;
                            table.hidden = false;

                            users.forEach((user) => {
                                const row = document.createElement("tr");
                                const checkbox = document.createElement("input");
                                const uname = document.createElement("label");

                                table.append(row);
                                row.insertCell().appendChild(checkbox);
                                row.insertCell().appendChild(uname);

                                checkbox.id = user.id;
                                checkbox.value = user.id;
                                checkbox.name = "selected";
                                checkbox.type = "checkbox";
                                checkbox.onchange = checkSelected;

                                uname.setAttribute("for", user.id);
                                uname.innerText = user.uname;
                            });
                        } else {
                            document.getElementById("no-users-message").hidden = false;
                            table.hidden = true;
                        }
                    }
                }
            };

            request.open("post", endpoint, true);
            request.send();
        }

        loadData = () => {
            loadUsers();
            loadCreatedMeetings();
            loadInvitedMeetings();
        }

        const refresh = document.getElementById("refresh-meetings")
        refresh.onclick = reload;
        refresh.onkeydown = reload;
    }

    let openModal;
    let closeModal;
    {
        // MODAL
        const modal = document.getElementById("modal");
        const openBtn = document.getElementById("modal-open");
        openModal = function () {
            modal.style.display = "block";
            document.getElementById("meeting-creation-aborted").hidden = true;
        };
        openBtn.onclick = openModal;
        openBtn.onkeydown = openModal;
        const closeBtn = document.getElementById("modal-close");
        closeModal = function () {
            modal.style.display = "none";
        };
        closeBtn.onclick = closeModal;
        closeBtn.onkeydown = closeModal;

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function (event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        }
    }

    let checkTitle;
    let checkStart;
    let checkDuration;
    let checkCapacity;
    let checkSelected;
    {   // MEETING CREATION
        const form = document.getElementById("create-meeting-form");
        const formTitle = document.getElementById("form-title");
        const formStart = document.getElementById("form-start");
        const formDuration = document.getElementById("form-duration");
        const formCapacity = document.getElementById("form-capacity");
        let formSelectedValidity;

        checkTitle = () => {
            const title = String(formTitle.value);
            if (!title || title.length < 1) formTitle.setCustomValidity("Field is required.");
            else if (title.length > 50) formTitle.setCustomValidity("Title is too long.");
            else formTitle.setCustomValidity("");
        };
        checkStart = () => {
            const start = luxon.DateTime.fromISO(formStart.value);
            if (!start.isValid) formStart.setCustomValidity("Field does not match a valid Date-Time format.");
            else if (start < luxon.DateTime.now()) formStart.setCustomValidity("Meetings cannot start in the past.");
            else formStart.setCustomValidity("");
        };
        checkDuration = () => {
            const minutes = Number(formDuration.value);
            if (!minutes) formDuration.setCustomValidity("Field is required.");
            else if (minutes < 1) formDuration.setCustomValidity("Duration must be greater than 1 minute.");
            else formDuration.setCustomValidity("");
        };
        checkCapacity = () => {
            const capacity = Number(formCapacity.value);
            if (!capacity) formCapacity.setCustomValidity("Field is required.");
            else if (capacity < 1) formCapacity.setCustomValidity("Meetings must have at least 1 participant.");
            else formCapacity.setCustomValidity("");
        };
        checkSelected = () => {
            const formData = new FormData(form);
            const selected = formData.getAll("selected");
            const messageSelectUsers = document.getElementById("select-users-message");
            const messageTooManyUsers = document.getElementById("too-many-users-message");
            const numberTooManyUsers = document.getElementById("too-many-users-number");
            const capacity = Number(formCapacity.value);

            if (selected.length < 1) {
                messageSelectUsers.hidden = false;
                messageTooManyUsers.hidden = true;
                formSelectedValidity = false;
            } else if (capacity && selected.length > capacity) {
                messageSelectUsers.hidden = true;
                messageTooManyUsers.hidden = false;
                numberTooManyUsers.innerText = String(selected.length - capacity);
                formSelectedValidity = false;
            } else if (selected.every((userid) => users.map((v) => Number(v.id)).includes(Number(userid)))) {
                messageSelectUsers.hidden = true;
                messageTooManyUsers.hidden = true;
                formSelectedValidity = true;
            } else {
                formSelectedValidity = false;
            }
        };

        formTitle.onchange = checkTitle;
        formStart.onchange = checkStart;
        formDuration.onchange = checkDuration;
        formCapacity.onchange = () => {
            checkCapacity();
            checkSelected();
        };

        let failsCounter = 0;

        const handleFailure = function (skip) {
            if (skip || ++failsCounter === 3) {
                failsCounter = 0;
                form.reset();
                checkTitle();
                checkStart();
                checkDuration();
                checkCapacity();
                checkSelected();
                document.getElementById("meeting-creation-aborted").hidden = false;
                closeModal();
            }
        }

        form.addEventListener("submit", (e) => {
            e.preventDefault();
            e.stopPropagation();

            if (form.reportValidity()) {
                if (formSelectedValidity) {
                    const data = new FormData(form);
                    data.set("start", luxon.DateTime.fromISO(formStart.value).toISO());
                    request.open("post", endpoint, true);
                    request.send(data);
                } else {
                    handleFailure();
                }
            }
        });

        // handles submission of a new meeting
        const endpoint = "/create";
        const request = new XMLHttpRequest();

        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) {
                    form.reset();
                    reload();
                    closeModal();
                } else {
                    handleFailure(true);
                }
            }
        };
    }

    function reload() {
        loadData();
        checkTitle();
        checkStart();
        checkDuration();
        checkCapacity();
        checkSelected();
    }

    reload();
}
