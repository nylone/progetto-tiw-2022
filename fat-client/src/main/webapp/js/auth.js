(function () {

    { // handles login box
        const id = "sign-in-form";
        const endpoint = "/login";
        const request = new XMLHttpRequest();

        request.onreadystatechange = () => {
            if (request.readyState === 4) {
                if (request.status === 200) window.location.assign("home.html");
                else {
                    document.getElementById("failed-sing-in").classList.remove("hidden");
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
                    document.getElementById("failed-sing-up").classList.remove("hidden");
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
            if (pass.match("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$") && pass === confPass) {
                // server only needs the password
                data.delete("pass-confirm");
                request.open("post", endpoint, true);
                request.send(data);
            } else {
                document.getElementById("failed-sing-up").classList.remove("hidden");
            }
        });
    }
})()