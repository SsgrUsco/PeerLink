const registerConsole = PeerlinkApp.bindConsole("consoleOutput");
const registerFeedback = PeerlinkApp.bindFeedback("registerFeedback");

PeerlinkApp.applyI18n();

const registerForm = document.getElementById("registerForm");
const registerAlert = document.getElementById("registerAlert");
const togglePasswordBtn = document.getElementById("toggleRegisterPasswordBtn");
const togglePasswordIcon = document.getElementById("toggleRegisterPasswordIcon");
const passwordInput = document.getElementById("password");
const submitButton = document.getElementById("registerSubmitBtn");
const submitSpinner = document.getElementById("registerSubmitSpinner");

PeerlinkApp.bindLanguageSelect("registerLanguageSelect");

document.getElementById("goLoginLink").href = PeerlinkApp.withLang("/login.html");

togglePasswordBtn.addEventListener("click", () => {
    const reveal = passwordInput.type === "password";
    passwordInput.type = reveal ? "text" : "password";
    togglePasswordIcon.className = reveal ? "bi bi-eye-slash" : "bi bi-eye";
});

registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    hideAlert();
    if (!registerForm.checkValidity()) {
        registerForm.classList.add("was-validated");
        return;
    }

    setLoading(true);
    const formElement = event.currentTarget;
    const form = new FormData(formElement);
    const payload = Object.fromEntries(form.entries());

    try {
        const response = await PeerlinkApp.api("/api/usuarios/registro", {
            method: "POST",
            body: JSON.stringify(payload)
        }, false);
        registerFeedback.success("feedback_register_success");
        registerConsole.print(response);
        formElement.reset();
        registerForm.classList.remove("was-validated");
    } catch (error) {
        showAlert(error?.message || "No fue posible completar el registro.");
        registerFeedback.error(error);
        registerConsole.printError(error);
    } finally {
        setLoading(false);
    }
});

function setLoading(isLoading) {
    submitButton.disabled = isLoading;
    submitSpinner.classList.toggle("d-none", !isLoading);
}

function showAlert(message) {
    registerAlert.textContent = message;
    registerAlert.classList.remove("d-none");
}

function hideAlert() {
    registerAlert.textContent = "";
    registerAlert.classList.add("d-none");
}
