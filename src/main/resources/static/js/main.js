document.addEventListener("DOMContentLoaded", function () {

    const currentPath = window.location.pathname;

    document.querySelectorAll(".nav-links a").forEach(function (link) {

        const href = new URL(link.href).pathname;

        if (href === currentPath) {

            link.classList.add("active");

        }

    });

});


const progressBar = document.getElementById("scroll-progress");

window.addEventListener("scroll", () => {

    const scrollTop =
        document.documentElement.scrollTop;

    const scrollHeight =
        document.documentElement.scrollHeight -
        document.documentElement.clientHeight;

    const progress =
        (scrollTop / scrollHeight) * 100;

    progressBar.style.width = progress + "%";

});