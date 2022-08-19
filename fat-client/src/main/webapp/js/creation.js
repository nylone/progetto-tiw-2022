{
    const form = document.getElementById("create-meeting-form");
    form.addEventListener("submit", (e) => {
        e.preventDefault();
        e.stopPropagation();

        if (checkForm()) {
            const data = new FormData(form);
            request.open("post", endpoint, true);
            request.send(data);
        } else {
            //todo add failed check errors
        }
    });

    const checkForm = function () {
        const formData = new FormData(form);
        const title = String(formData.get("title"));
        const start = luxon.DateTime.fromISO(formData.get("start"));
        const minutes = Number(formData.get("duration"));
        const capacity = Number(formData.get("capacity"));

        return (form.reportValidity() &&
            title && title.length > 0 &&
            start.isValid && start > luxon.DateTime.now() &&
            minutes && minutes > 0 &&
            capacity && capacity > 0);
    }

    // handles submission of a new meeting
    const endpoint = "/create";
    const request = new XMLHttpRequest();

    request.onreadystatechange = () => {
        if (request.readyState === 4) {
            if (request.status === 200) {
                // todo if server adds the meeting successfully
            }
            else {
                // todo if server rejects the meeting submission
            }
        }
    };
}