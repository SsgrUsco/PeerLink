const adminAuth = PeerlinkApp.ensureRole("ADMIN");
const adminConsole = PeerlinkApp.bindConsole("consoleOutput");
let materiasCache = [];
let usuariosCache = [];

if (adminAuth) {
    PeerlinkApp.renderNavbar("navbarContainer");
    PeerlinkApp.applyI18n();
    bootAdmin();
}

async function bootAdmin() {
    fillLanguageSelect("asignacionIdioma");
    fillFacultySelect("asignacionFacultad");
    document.getElementById("materiaForm").addEventListener("submit", saveMateria);
    document.getElementById("cancelMateriaEdit").addEventListener("click", resetMateriaForm);
    document.getElementById("asignacionForm").addEventListener("submit", asignarTutorMateria);
    document.getElementById("usuarioForm").addEventListener("submit", saveUsuario);
    document.getElementById("refreshTutorMateriasBtn").addEventListener("click", loadTutorMaterias);
    await Promise.all([loadMaterias(), loadUsuarios(), loadTutorMaterias()]);
}

function fillLanguageSelect(elementId) {
    const select = document.getElementById(elementId);
    select.innerHTML = PeerlinkApp.getTutoringLanguages().map((code) => `
        <option value="${code}">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(code))}</option>
    `).join("");
}

function fillFacultySelect(elementId) {
    const select = document.getElementById(elementId);
    select.innerHTML = PeerlinkApp.getFaculties().map((code) => `
        <option value="${code}">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(code))}</option>
    `).join("");
}

async function loadMaterias() {
    try {
        materiasCache = await PeerlinkApp.api("/api/materias");
        fillMateriaSelect();
        const tbody = document.getElementById("materiasTableBody");
        if (!materiasCache.length) {
            tbody.innerHTML = `<tr><td colspan="2">${PeerlinkApp.t("empty_subjects")}</td></tr>`;
            return;
        }

        tbody.innerHTML = materiasCache.map((materia) => `
            <tr>
                <td>${PeerlinkApp.escapeHtml(materia.nombre)}</td>
                <td>
                    <div class="table-actions">
                        <button class="btn btn-sm btn-outline-primary" data-edit-id="${materia.id}" data-edit-name="${PeerlinkApp.escapeHtml(materia.nombre)}" type="button">${PeerlinkApp.t("action_edit")}</button>
                        <button class="btn btn-sm btn-outline-danger" data-delete-id="${materia.id}" type="button">${PeerlinkApp.t("action_delete")}</button>
                    </div>
                </td>
            </tr>
        `).join("");

        tbody.querySelectorAll("[data-edit-id]").forEach((button) => {
            button.addEventListener("click", () => {
                document.getElementById("materiaId").value = button.dataset.editId;
                document.getElementById("materiaNombre").value = button.dataset.editName;
            });
        });

        tbody.querySelectorAll("[data-delete-id]").forEach((button) => {
            button.addEventListener("click", async () => {
                if (!confirm(PeerlinkApp.t("confirm_delete_subject"))) {
                    return;
                }
                try {
                    await PeerlinkApp.api(`/api/materias/${button.dataset.deleteId}`, { method: "DELETE" });
                    await Promise.all([loadMaterias(), loadTutorMaterias()]);
                } catch (error) {
                    adminConsole.printError(error);
                }
            });
        });
    } catch (error) {
        adminConsole.printError(error);
    }
}

async function saveMateria(event) {
    event.preventDefault();
    const id = document.getElementById("materiaId").value;
    const payload = { nombre: document.getElementById("materiaNombre").value.trim() };
    const endpoint = id ? `/api/materias/${id}` : "/api/materias";
    const method = id ? "PUT" : "POST";

    try {
        const response = await PeerlinkApp.api(endpoint, {
            method,
            body: JSON.stringify(payload)
        });
        adminConsole.print(response);
        resetMateriaForm();
        await Promise.all([loadMaterias(), loadTutorMaterias()]);
    } catch (error) {
        adminConsole.printError(error);
    }
}

function resetMateriaForm() {
    document.getElementById("materiaId").value = "";
    document.getElementById("materiaNombre").value = "";
}

function fillMateriaSelect() {
    const select = document.getElementById("asignacionMateriaId");
    select.innerHTML = materiasCache.map((materia) => `
        <option value="${materia.id}">${PeerlinkApp.escapeHtml(materia.nombre)}</option>
    `).join("");
}

function fillTutorSelect() {
    const select = document.getElementById("asignacionTutorId");
    const tutores = usuariosCache.filter((usuario) => usuario.rol === "TUTOR");
    select.innerHTML = tutores.map((usuario) => `
        <option value="${usuario.id}">${PeerlinkApp.escapeHtml(usuario.nombreCompleto)} - ${PeerlinkApp.escapeHtml(usuario.correo)}</option>
    `).join("");
}

async function asignarTutorMateria(event) {
    event.preventDefault();
    const formElement = event.currentTarget;
    const payload = {
        tutorId: Number(document.getElementById("asignacionTutorId").value),
        materiaId: Number(document.getElementById("asignacionMateriaId").value),
        idioma: document.getElementById("asignacionIdioma").value,
        facultad: document.getElementById("asignacionFacultad").value
    };

    try {
        const response = await PeerlinkApp.api("/api/materias/asignar-tutor", {
            method: "POST",
            body: JSON.stringify(payload)
        });
        adminConsole.print(response);
        formElement.reset();
        await loadTutorMaterias();
    } catch (error) {
        adminConsole.printError(error);
    }
}

async function loadTutorMaterias() {
    try {
        const relaciones = await PeerlinkApp.api("/api/materias/tutores-materias");
        const tbody = document.getElementById("tutorMateriasTableBody");
        if (!relaciones.length) {
            tbody.innerHTML = `<tr><td colspan="5">${PeerlinkApp.t("empty_assignments")}</td></tr>`;
            return;
        }

        tbody.innerHTML = relaciones.map((item) => `
            <tr>
                <td>${PeerlinkApp.escapeHtml(item.tutorNombre)}</td>
                <td>${PeerlinkApp.escapeHtml(item.tutorCorreo)}</td>
                <td>${PeerlinkApp.escapeHtml(item.materiaNombre)}</td>
                <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</td>
                <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(item.facultad))}</td>
            </tr>
        `).join("");
    } catch (error) {
        adminConsole.printError(error);
    }
}

async function loadUsuarios() {
    try {
        usuariosCache = await PeerlinkApp.api("/api/usuarios");
        fillTutorSelect();
        const tbody = document.getElementById("usuariosTableBody");
        if (!usuariosCache.length) {
            tbody.innerHTML = `<tr><td colspan="4">${PeerlinkApp.t("empty_users")}</td></tr>`;
            return;
        }

        tbody.innerHTML = usuariosCache.map((usuario) => `
            <tr>
                <td>${PeerlinkApp.escapeHtml(usuario.nombreCompleto || "")}</td>
                <td>${PeerlinkApp.escapeHtml(usuario.correo || "")}</td>
                <td><span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(usuario.rol || "")}</span></td>
                <td>
                    <button class="btn btn-sm btn-outline-danger" data-delete-user-id="${usuario.id}" data-delete-user-name="${PeerlinkApp.escapeHtml(usuario.nombreCompleto || usuario.correo || "")}" type="button">${PeerlinkApp.t("action_delete")}</button>
                </td>
            </tr>
        `).join("");

        tbody.querySelectorAll("[data-delete-user-id]").forEach((button) => {
            button.addEventListener("click", async () => {
                if (!confirm(PeerlinkApp.t("confirm_delete_user", { name: button.dataset.deleteUserName }))) {
                    return;
                }
                try {
                    await PeerlinkApp.api(`/api/usuarios/${button.dataset.deleteUserId}`, { method: "DELETE" });
                    await Promise.all([loadUsuarios(), loadTutorMaterias()]);
                } catch (error) {
                    adminConsole.printError(error);
                }
            });
        });
    } catch (error) {
        adminConsole.printError(error);
    }
}

async function saveUsuario(event) {
    event.preventDefault();
    const formElement = event.currentTarget;
    const payload = {
        nombreCompleto: document.getElementById("usuarioNombre").value.trim(),
        correo: document.getElementById("usuarioCorreo").value.trim(),
        password: document.getElementById("usuarioPassword").value,
        rol: document.getElementById("usuarioRol").value
    };

    try {
        const response = await PeerlinkApp.api("/api/usuarios", {
            method: "POST",
            body: JSON.stringify(payload)
        });
        adminConsole.print(response);
        formElement.reset();
        await loadUsuarios();
    } catch (error) {
        adminConsole.printError(error);
    }
}
