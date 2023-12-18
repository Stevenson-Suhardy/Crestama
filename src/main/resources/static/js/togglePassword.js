const togglePassword = document.querySelector("#togglePassword");

const password = document.querySelector("#password");

togglePassword.addEventListener("click", (e) => {
    const type = password.getAttribute("type") === "password" ? "text" : "password";

    password.setAttribute("type", type);

    if (type === "password") {
        togglePassword.className = "fa-solid fa-eye";
    }
    else {
        togglePassword.className = "fa-solid fa-eye-slash";
    }
})