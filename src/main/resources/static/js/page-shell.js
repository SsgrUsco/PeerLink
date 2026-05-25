PeerlinkApp.applyI18n();

const authShell = PeerlinkApp.getAuth();
if (authShell.token && document.getElementById("navbarContainer")) {
    PeerlinkApp.renderNavbar("navbarContainer");
} else {
    document.querySelectorAll(".lang-btn").forEach((button) => {
        button.classList.toggle("active", button.dataset.lang === PeerlinkApp.getLang());
        button.addEventListener("click", () => PeerlinkApp.setLang(button.dataset.lang));
    });
}
