PeerlinkApp.applyI18n();

const authShell = PeerlinkApp.getAuth();
if (authShell.role && document.getElementById("navbarContainer")) {
    PeerlinkApp.renderNavbar("navbarContainer");
} else {
    document.querySelectorAll(".peerlink-language-select").forEach(PeerlinkApp.bindLanguageSelect);
}
