(function () {

    { // handles login box
        const id = "sign-in-form";
        const endpoint = "/login";
        const request = new XMLHttpRequest();

        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) window.location.assign("home.html");
                else {
                    document.getElementById("failed-sing-in").hidden = false;
                }
            }
        };

        document.getElementById(id).addEventListener("submit", (e) => {
            e.preventDefault();
            if (!e.target.closest("form").reportValidity()) {
                e.stopPropagation();
            }
            const data = new FormData(e.target.closest("form"));
            request.open("post", endpoint, true);
            request.send(data);
        });
    }

    { // handles register box
        const id = "sign-up-form";
        const endpoint = "/register";
        const request = new XMLHttpRequest();

        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) window.location.assign("home.html");
                else {
                    document.getElementById("failed-sing-up").hidden = false;
                }
            }
        };

        document.getElementById(id).addEventListener("submit", (e) => {
            e.preventDefault();
            if (!e.target.closest("form").reportValidity()) {
                e.stopPropagation();
            }
            const data = new FormData(e.target.closest("form"));
            const pass = data.get("pass");
            const confPass = data.get("pass-confirm");
            if (data.get("email").match("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])") &&
                pass.match("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$") && pass === confPass) {
                // server only needs the password
                data.delete("pass-confirm");
                request.open("post", endpoint, true);
                request.send(data);
            } else {
                document.getElementById("failed-sing-up").hidden = false;
            }
        });
    }
})()