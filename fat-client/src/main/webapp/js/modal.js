(function () {
    const modal = document.getElementById("modal");
    const openBtn = document.getElementById("modal-open");
    var closeBtn = document.getElementById("modal-close");

    openBtn.onclick = function() {
        modal.style.display = "block";
    }

    closeBtn.onclick = function() {
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }
})()