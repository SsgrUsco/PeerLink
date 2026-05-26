PeerlinkApp.applyI18n();

const loginForm = document.getElementById("loginForm");
const loginAlert = document.getElementById("loginAlert");
const togglePasswordBtn = document.getElementById("togglePasswordBtn");
const togglePasswordIcon = document.getElementById("togglePasswordIcon");
const passwordInput = document.getElementById("password");
const submitButton = document.getElementById("loginSubmitBtn");
const submitSpinner = document.getElementById("loginSubmitSpinner");

document.getElementById("goRegisterLink").href = PeerlinkApp.withLang("/registro.html");
document.getElementById("goRecoveryLink").href = PeerlinkApp.withLang("/recuperar-password.html");

PeerlinkApp.bindLanguageSelect("loginLanguageSelect");

const auth = PeerlinkApp.getAuth();
if (auth.token && auth.role) {
    PeerlinkApp.redirectToPanel(auth.role);
}

togglePasswordBtn.addEventListener("click", () => {
    const reveal = passwordInput.type === "password";
    passwordInput.type = reveal ? "text" : "password";
    togglePasswordIcon.className = reveal ? "bi bi-eye-slash" : "bi bi-eye";
});

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    hideAlert();
    if (!loginForm.checkValidity()) {
        loginForm.classList.add("was-validated");
        return;
    }

    setLoading(true);
    const payload = {
        correo: document.getElementById("correo").value.trim(),
        password: document.getElementById("password").value
    };

    try {
        const response = await PeerlinkApp.api("/api/auth/login", {
            method: "POST",
            body: JSON.stringify(payload)
        }, false);
        PeerlinkApp.saveAuth(response);
        PeerlinkApp.redirectToPanel(response.rol);
    } catch (error) {
        showAlert(error?.message || PeerlinkApp.t("feedback_login_error"));
    } finally {
        setLoading(false);
    }
});

function setLoading(isLoading) {
    submitButton.disabled = isLoading;
    submitSpinner.classList.toggle("d-none", !isLoading);
}

function showAlert(message) {
    loginAlert.textContent = message;
    loginAlert.classList.remove("d-none");
}

function hideAlert() {
    loginAlert.textContent = "";
    loginAlert.classList.add("d-none");
}
