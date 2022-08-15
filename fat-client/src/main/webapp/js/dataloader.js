(function () {
    { // handles own meetings table
        const type = "created";
        const data = new FormData();
        data.append("type", type);
        const endpoint = "/meetings";
        const request = new XMLHttpRequest();
        const table = document.getElementById("created-meetings-table");

        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) {
                    const meetings = JSON.parse(request.responseText);
                    if (meetings.length > 0) {
                        table.parentElement.classList.remove("hidden");
                        meetings.forEach((meeting) => {
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
                        });
                    }
                }
            }
        };

        request.open("post", endpoint, true);
        request.send(data);
    }

    { // handles invited meetings table
        const type = "invited";
        const data = new FormData();
        data.append("type", type);
        const endpoint = "/meetings";
        const request = new XMLHttpRequest();
        const table = document.getElementById("created-meetings-table");

        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) {
                    const meetings = JSON.parse(request.responseText);
                    if (meetings.length > 0) {
                        table.parentElement.classList.remove("hidden");
                        meetings.forEach((meeting) => {
                            const row = document.createElement("tr");
                            table.append(row);
                            const title = row.insertCell();
                            const date = row.insertCell();
                            const time = row.insertCell();
                            const duration = row.insertCell();
                            const capacity = row.insertCell();
                            const admin = row.insertCell();


                            title.innerText = meeting.title;
                            date.innerText = meeting.date;
                            time.innerText = meeting.time;
                            duration.innerText = meeting.duration;
                            capacity.innerText = meeting.capacity;
                            admin.innerText = meeting.admin.email;
                        });
                    }
                }
            }
        };

        request.open("post", endpoint, true);
        request.send(data);
    }
})()