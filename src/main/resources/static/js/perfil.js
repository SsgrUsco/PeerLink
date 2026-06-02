const profileAuth = PeerlinkApp.ensureRole();
const profileConsole = PeerlinkApp.bindConsole("consoleOutput");
const profileFeedback = PeerlinkApp.bindFeedback("profileFeedback");

if (profileAuth) {
    PeerlinkApp.renderNavbar("navbarContainer");
    PeerlinkApp.applyI18n();
    bootProfile();
}

async function bootProfile() {
    const backToPanelBtn = document.getElementById("profileBackToPanelBtn");
    if (backToPanelBtn) {
        backToPanelBtn.href = PeerlinkApp.withLang(PeerlinkApp.panelForRole(profileAuth.role));
    }
    document.getElementById("profileForm").addEventListener("submit", saveProfile);
    document.getElementById("passwordForm").addEventListener("submit", savePassword);
    await loadProfile();
}

async function loadProfile() {
    try {
        const data = await PeerlinkApp.api("/api/usuarios/me");
        document.getElementById("perfilNombre").value = data.nombreCompleto || "";
        document.getElementById("perfilCorreo").value = data.correo || "";
        document.getElementById("perfilRol").value = data.rol || "";
        profileFeedback.info("feedback_profile_loaded");
        profileConsole.print(data);
    } catch (error) {
        profileFeedback.error(error);
        profileConsole.printError(error);
    }
}

async function saveProfile(event) {
    event.preventDefault();
    try {
        const data = await PeerlinkApp.api("/api/usuarios/me", {
            method: "PUT",
            body: JSON.stringify({
                nombreCompleto: document.getElementById("perfilNombre").value.trim(),
                correo: document.getElementById("perfilCorreo").value.trim()
            })
        });
        PeerlinkApp.updateStoredProfile(data);
        PeerlinkApp.renderNavbar("navbarContainer");
        profileFeedback.success("feedback_profile_saved");
        profileConsole.print(data);
    } catch (error) {
        profileFeedback.error(error);
        profileConsole.printError(error);
    }
}

async function savePassword(event) {
    event.preventDefault();
    if (!event.currentTarget.checkValidity()) {
        event.currentTarget.classList.add("was-validated");
        return;
    }
    try {
        await PeerlinkApp.api("/api/usuarios/me/password", {
            method: "PATCH",
            body: JSON.stringify({
                passwordActual: document.getElementById("passwordActual").value,
                passwordNueva: document.getElementById("passwordNueva").value
            })
        });
        event.currentTarget.reset();
        event.currentTarget.classList.remove("was-validated");
        profileFeedback.success("password_updated");
        profileConsole.print({ message: "password_updated" });
    } catch (error) {
        profileFeedback.error(error);
        profileConsole.printError(error);
    }
}
