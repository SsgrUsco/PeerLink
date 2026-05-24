const registerConsole = PeerlinkApp.bindConsole("consoleOutput");

PeerlinkApp.applyI18n();

document.querySelectorAll(".lang-btn").forEach((button) => {
    button.classList.toggle("active", button.dataset.lang === PeerlinkApp.getLang());
    button.addEventListener("click", () => PeerlinkApp.setLang(button.dataset.lang));
});

document.getElementById("goLoginLink").href = PeerlinkApp.withLang("/login.html");

document.getElementById("registerForm").addEventListener("submit", async (event) => {
    event.preventDefault();
    const formElement = event.currentTarget;
    const form = new FormData(formElement);
    const payload = Object.fromEntries(form.entries());

    try {
        const response = await PeerlinkApp.api("/api/usuarios/registro", {
            method: "POST",
            body: JSON.stringify(payload)
        }, false);
        registerConsole.print(response);
        formElement.reset();
    } catch (error) {
        registerConsole.printError(error);
    }
});
