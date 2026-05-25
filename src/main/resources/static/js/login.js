const loginConsole = PeerlinkApp.bindConsole("consoleOutput");

PeerlinkApp.applyI18n();

document.querySelectorAll(".lang-btn").forEach((button) => {
    button.classList.toggle("active", button.dataset.lang === PeerlinkApp.getLang());
    button.addEventListener("click", () => PeerlinkApp.setLang(button.dataset.lang));
});

document.getElementById("goRegistroLink").href = PeerlinkApp.withLang("/registro.html");
document.getElementById("goRecoveryLink").href = PeerlinkApp.withLang("/recuperar-password.html");

const auth = PeerlinkApp.getAuth();
if (auth.token && auth.role) {
    PeerlinkApp.redirectToPanel(auth.role);
}

document.getElementById("loginForm").addEventListener("submit", async (event) => {
    event.preventDefault();
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
        loginConsole.print(response);
        PeerlinkApp.redirectToPanel(response.rol);
    } catch (error) {
        loginConsole.printError(error);
    }
});
