const profileAuth = PeerlinkApp.ensureRole();
const profileConsole = PeerlinkApp.bindConsole("consoleOutput");

if (profileAuth) {
    PeerlinkApp.renderNavbar("navbarContainer");
    PeerlinkApp.applyI18n();
    bootProfile();
}

async function bootProfile() {
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
        profileConsole.print(data);
    } catch (error) {
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
        profileConsole.print(data);
    } catch (error) {
        profileConsole.printError(error);
    }
}

async function savePassword(event) {
    event.preventDefault();
    try {
        await PeerlinkApp.api("/api/usuarios/me/password", {
            method: "PATCH",
            body: JSON.stringify({
                passwordActual: document.getElementById("passwordActual").value,
                passwordNueva: document.getElementById("passwordNueva").value
            })
        });
        event.currentTarget.reset();
        profileConsole.print({ message: "password_updated" });
    } catch (error) {
        profileConsole.printError(error);
    }
}
