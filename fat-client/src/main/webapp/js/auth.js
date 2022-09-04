(function () {
    {
        // handles login box
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
            } else {
                const data = new FormData(e.target.closest("form"));
                request.open("post", endpoint, true);
                request.send(data);
            }
        });
    }

    {
        // handles register box
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
            } else {
                const data = new FormData(e.target.closest("form"));
                // server only needs the password
                data.delete("pass-confirm");
                request.open("post", endpoint, true);
                request.send(data);
            }
        });
    }

    // hanldes form fields input checking
    {
        const siEmail = document.getElementById("sign-in-email");
        const siPass = document.getElementById("sign-in-pass");
        const suEmail = document.getElementById("sign-up-email");
        const suUname = document.getElementById("sign-up-uname");
        const suPass = document.getElementById("sign-up-pass");
        const suConfPass = document.getElementById("sign-up-confpass");

        siEmail.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1)
                elem.setCustomValidity("Field is required.");
            else if (value.length > 255)
                elem.setCustomValidity("e-mail is too long.");
            else if (
                !value.match(
                    "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
                )
            )
                elem.setCustomValidity("Not a valid e-mail address.");
            else elem.setCustomValidity("");
        };

        siPass.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1)
                elem.setCustomValidity("Field is required.");
            else elem.setCustomValidity("");
        };

        suEmail.onchange = siEmail.onchange;

        suUname.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1)
                elem.setCustomValidity("Field is required.");
            else if (value.length > 20)
                elem.setCustomValidity("username is too long.");
            else if (
                !value.match(
                    "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$"
                )
            )
                elem.setCustomValidity("Not a valid username.");
            else elem.setCustomValidity("");
        };

        suPass.onchange = (e) => {
            const elem = e.target;
            const value = String(elem.value);
            if (!value || value.length < 1)
                elem.setCustomValidity("Field is required.");
            else if (
                !value.match(
                    "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$"
                )
            )
                elem.setCustomValidity("Not a valid password.");
            else elem.setCustomValidity("");

            suConfPass.onchange();
        };

        suConfPass.onchange = (_e) => {
            const elem = suConfPass;
            const value = String(elem.value);
            if (!value || value.length < 1)
                elem.setCustomValidity("Field is required.");
            else if (
                !value.match(
                    "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$"
                )
            )
                elem.setCustomValidity("Not a valid password.");
            else if (!(value === String(suPass.value)))
                elem.setCustomValidity("Passwords do not match.");
            else elem.setCustomValidity("");
        };
    }
})();
