let confirmDeleteUrl = "";

const confirmModal = document.getElementById("confirmModal");

const confirmTitle = document.getElementById("confirmTitle");

const confirmMessage = document.getElementById("confirmMessage");

const confirmCancelBtn = document.getElementById("confirmCancel");

const confirmDeleteBtn = document.getElementById("confirmDelete");

document.addEventListener("click", function (e) {

    const deleteLink = e.target.closest(".delete-link");

    if (!deleteLink) {
        return;
    }

    e.preventDefault();

    confirmDeleteUrl = deleteLink.href;

    confirmTitle.textContent = "Confirm Delete";

    confirmMessage.textContent =
        "Are you sure you want to delete this record?";

    confirmModal.classList.add("show");

});

confirmCancelBtn.addEventListener("click", function () {

    confirmModal.classList.remove("show");

});

confirmDeleteBtn.addEventListener("click", function () {

    window.location.href = confirmDeleteUrl;

});

confirmModal.addEventListener("click", function (e) {

    if (e.target === confirmModal) {

        confirmModal.classList.remove("show");

    }

});

document.addEventListener("keydown", function (e) {

    if (e.key === "Escape") {

        confirmModal.classList.remove("show");

    }

});