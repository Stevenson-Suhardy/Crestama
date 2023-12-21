const togglePassword = document.querySelector("#togglePassword");
const password = document.querySelector("#password");

const toggleConfirmPassword = document.querySelector("#toggleConfirmPassword");
const confirmPassword = document.querySelector("#confirmPassword");

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

toggleConfirmPassword.addEventListener("click", (e) => {
    const type = confirmPassword.getAttribute("type") === "password" ? "text" : "password";

    confirmPassword.setAttribute("type", type);

    if (type === "password") {
        toggleConfirmPassword.className = "fa-solid fa-eye";
    }
    else {
        toggleConfirmPassword.className = "fa-solid fa-eye-slash";
    }
})