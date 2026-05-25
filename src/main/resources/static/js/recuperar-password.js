const recoveryConsole = PeerlinkApp.bindConsole("consoleOutput");

PeerlinkApp.applyI18n();
document.querySelectorAll(".lang-btn").forEach((button) => {
    button.classList.toggle("active", button.dataset.lang === PeerlinkApp.getLang());
    button.addEventListener("click", () => PeerlinkApp.setLang(button.dataset.lang));
});

const backToLoginLink = document.getElementById("backToLoginLink");
if (backToLoginLink) {
    backToLoginLink.href = PeerlinkApp.withLang("/login.html");
}

document.getElementById("recoveryForm").addEventListener("submit", (event) => {
    event.preventDefault();
    recoveryConsole.print({
        requested: true,
        correo: document.getElementById("recoveryCorreo").value.trim(),
        nextStep: "contact_support_or_admin"
    });
});
