const adminAuth = PeerlinkApp.ensureRole("ADMIN");
const adminConsole = PeerlinkApp.bindConsole("consoleOutput");
let materiasCache = [];
let usuariosCache = [];
let relacionesCache = [];
let selectedMateriaCardId = null;

if (adminAuth) {
    PeerlinkApp.renderNavbar("navbarContainer");
    PeerlinkApp.applyI18n();
    bootAdmin();
}

async function bootAdmin() {
    fillLanguageSelect("materiaIdioma", false);
    fillFacultySelect("materiaFacultad", false);
    fillLanguageSelect("materiasIdiomaFilter", true);
    fillFacultySelect("materiasFacultadFilter", true);

    document.getElementById("adminMateriasTabBtn").addEventListener("click", () => showAdminTab("materias"));
    document.getElementById("adminTutoresTabBtn").addEventListener("click", () => showAdminTab("tutores"));
    document.getElementById("adminUsuariosTabBtn").addEventListener("click", () => showAdminTab("usuarios"));

    document.getElementById("adminMateriaCrearTabBtn").addEventListener("click", () => showMateriaSubtab("crear"));
    document.getElementById("adminMateriaBuscarTabBtn").addEventListener("click", () => showMateriaSubtab("buscar"));

    document.getElementById("materiaForm").addEventListener("submit", saveMateria);
    document.getElementById("cancelMateriaEdit").addEventListener("click", resetMateriaForm);
    document.getElementById("materiasSearchForm").addEventListener("submit", (event) => {
        event.preventDefault();
        renderMateriasMatches();
    });
    document.getElementById("clearMateriasSearchBtn").addEventListener("click", clearMateriaSearch);

    document.getElementById("asignacionForm").addEventListener("submit", asignarTutorMateria);
    document.getElementById("tutorSearch").addEventListener("input", fillTutorSelect);
    document.getElementById("materiaSearch").addEventListener("input", fillMateriaSelect);
    document.getElementById("usuarioForm").addEventListener("submit", saveUsuario);
    document.getElementById("usuariosSearchForm").addEventListener("submit", (event) => {
        event.preventDefault();
        renderUsuariosTable();
    });
    document.getElementById("usuariosFilterInput").addEventListener("input", renderUsuariosTable);
    document.getElementById("usuariosRolFilter").addEventListener("change", renderUsuariosTable);
    document.getElementById("clearUsuariosSearchBtn").addEventListener("click", clearUsuariosSearch);
    document.getElementById("refreshTutorMateriasBtn").addEventListener("click", loadTutorMaterias);
    document.getElementById("asignacionesFilterInput").addEventListener("input", renderTutorMateriasTable);

    resetMateriaForm();
    showAdminTab("materias");
    showMateriaSubtab("crear");
    await Promise.all([loadMaterias(), loadUsuarios(), loadTutorMaterias()]);
}

function showAdminTab(tab) {
    const materiasTab = document.getElementById("adminMateriasTab");
    const tutoresTab = document.getElementById("adminTutoresTab");
    const usuariosTab = document.getElementById("adminUsuariosTab");

    materiasTab.classList.toggle("hidden", tab !== "materias");
    tutoresTab.classList.toggle("hidden", tab !== "tutores");
    usuariosTab.classList.toggle("hidden", tab !== "usuarios");

    document.getElementById("adminMateriasTabBtn").className = tab === "materias" ? "btn btn-success" : "btn btn-outline-success";
    document.getElementById("adminTutoresTabBtn").className = tab === "tutores" ? "btn btn-success" : "btn btn-outline-success";
    document.getElementById("adminUsuariosTabBtn").className = tab === "usuarios" ? "btn btn-success" : "btn btn-outline-success";
}

function showMateriaSubtab(tab) {
    const crear = document.getElementById("adminMateriaCrearSection");
    const buscar = document.getElementById("adminMateriaBuscarSection");
    crear.classList.toggle("hidden", tab !== "crear");
    buscar.classList.toggle("hidden", tab !== "buscar");
    document.getElementById("adminMateriaCrearTabBtn").className = tab === "crear" ? "btn btn-success" : "btn btn-outline-success";
    document.getElementById("adminMateriaBuscarTabBtn").className = tab === "buscar" ? "btn btn-success" : "btn btn-outline-success";
}

function fillLanguageSelect(elementId, includeAllOption) {
    const select = document.getElementById(elementId);
    const options = includeAllOption
        ? [`<option value="">${PeerlinkApp.escapeHtml(PeerlinkApp.t("select_all_languages"))}</option>`]
        : [];

    PeerlinkApp.getTutoringLanguages().forEach((code) => {
        options.push(`<option value="${code}">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(code))}</option>`);
    });

    select.innerHTML = options.join("");
}

function fillFacultySelect(elementId, includeAllOption) {
    const select = document.getElementById(elementId);
    const options = includeAllOption
        ? [`<option value="">${PeerlinkApp.escapeHtml(PeerlinkApp.t("select_all_faculties"))}</option>`]
        : [];

    PeerlinkApp.getFaculties().forEach((code) => {
        options.push(`<option value="${code}">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(code))}</option>`);
    });

    select.innerHTML = options.join("");
}

async function loadMaterias() {
    try {
        materiasCache = await PeerlinkApp.api("/api/materias");
        fillMateriaSelect();
        renderMateriasMatches();
    } catch (error) {
        adminConsole.printError(error);
    }
}

function getFilteredMaterias() {
    const text = document.getElementById("materiasFilterInput").value.trim().toLowerCase();
    const idioma = document.getElementById("materiasIdiomaFilter").value;
    const facultad = document.getElementById("materiasFacultadFilter").value;

    return materiasCache.filter((materia) => {
        const haystack = `${materia.nombre} ${PeerlinkApp.labelForLanguage(materia.idioma)} ${PeerlinkApp.labelForFaculty(materia.facultad)}`.toLowerCase();
        return (!text || haystack.includes(text))
            && (!idioma || materia.idioma === idioma)
            && (!facultad || materia.facultad === facultad);
    });
}

function renderMateriasMatches() {
    const container = document.getElementById("materiasMatchesContainer");
    const filtered = getFilteredMaterias();

    if (!filtered.length) {
        container.innerHTML = `<div class="col-12"><div class="alert alert-light border mb-0">${PeerlinkApp.t("empty_subjects")}</div></div>`;
        return;
    }

    container.innerHTML = filtered.map((materia) => {
        const expanded = selectedMateriaCardId === materia.id;
        return `
            <div class="col-lg-6">
                <article class="card border-0 shadow-sm h-100 cursor-pointer ${expanded ? "border border-success" : ""}" data-materia-card="${materia.id}">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start gap-3 mb-3">
                            <div>
                                <h3 class="h5 mb-1">${PeerlinkApp.escapeHtml(materia.nombre)}</h3>
                                <div class="d-flex flex-wrap gap-2">
                                    <span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(materia.idioma))}</span>
                                    <span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(materia.facultad))}</span>
                                </div>
                            </div>
                            <span class="badge text-bg-success-subtle border border-success-subtle">${expanded ? PeerlinkApp.t("state_editing") : PeerlinkApp.t("state_match")}</span>
                        </div>
                        <div class="${expanded ? "" : "hidden"}" data-materia-actions="${materia.id}">
                            <div class="d-flex flex-wrap gap-2">
                                <button class="btn btn-sm btn-outline-primary" type="button" data-edit-materia="${materia.id}">${PeerlinkApp.t("action_edit")}</button>
                                <button class="btn btn-sm btn-outline-danger" type="button" data-delete-materia="${materia.id}">${PeerlinkApp.t("action_delete")}</button>
                            </div>
                        </div>
                    </div>
                </article>
            </div>
        `;
    }).join("");

    container.querySelectorAll("[data-materia-card]").forEach((card) => {
        card.addEventListener("click", (event) => {
            if (event.target.closest("button")) {
                return;
            }
            const id = Number(card.dataset.materiaCard);
            selectedMateriaCardId = selectedMateriaCardId === id ? null : id;
            renderMateriasMatches();
        });
    });

    container.querySelectorAll("[data-edit-materia]").forEach((button) => {
        button.addEventListener("click", () => {
            const materia = materiasCache.find((item) => item.id === Number(button.dataset.editMateria));
            if (!materia) {
                return;
            }
            document.getElementById("materiaId").value = materia.id;
            document.getElementById("materiaNombre").value = materia.nombre;
            document.getElementById("materiaIdioma").value = materia.idioma;
            document.getElementById("materiaFacultad").value = materia.facultad;
            showAdminTab("materias");
            showMateriaSubtab("crear");
        });
    });

    container.querySelectorAll("[data-delete-materia]").forEach((button) => {
        button.addEventListener("click", async () => {
            if (!confirm(PeerlinkApp.t("confirm_delete_subject"))) {
                return;
            }
            try {
                await PeerlinkApp.api(`/api/materias/${button.dataset.deleteMateria}`, { method: "DELETE" });
                selectedMateriaCardId = null;
                await Promise.all([loadMaterias(), loadTutorMaterias()]);
            } catch (error) {
                adminConsole.printError(error);
            }
        });
    });
}

function clearMateriaSearch() {
    document.getElementById("materiasSearchForm").reset();
    selectedMateriaCardId = null;
    renderMateriasMatches();
}

async function saveMateria(event) {
    event.preventDefault();
    const id = document.getElementById("materiaId").value;
    const payload = {
        nombre: document.getElementById("materiaNombre").value.trim(),
        idioma: document.getElementById("materiaIdioma").value,
        facultad: document.getElementById("materiaFacultad").value
    };
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
        showAdminTab("materias");
        showMateriaSubtab("buscar");
    } catch (error) {
        adminConsole.printError(error);
    }
}

function resetMateriaForm() {
    document.getElementById("materiaId").value = "";
    document.getElementById("materiaNombre").value = "";
    document.getElementById("materiaIdioma").value = "es";
    document.getElementById("materiaFacultad").value = "CIENCIAS_EXACTAS_Y_NATURALES";
}

function fillMateriaSelect() {
    const select = document.getElementById("asignacionMateriaId");
    const search = document.getElementById("materiaSearch").value.trim().toLowerCase();
    const filtered = materiasCache.filter((materia) => {
        const haystack = `${materia.nombre} ${PeerlinkApp.labelForLanguage(materia.idioma)} ${PeerlinkApp.labelForFaculty(materia.facultad)}`.toLowerCase();
        return !search || haystack.includes(search);
    });
    select.innerHTML = filtered.map((materia) => `
        <option value="${materia.id}">${PeerlinkApp.escapeHtml(materia.nombre)} · ${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(materia.idioma))} · ${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(materia.facultad))}</option>
    `).join("");
}

function fillTutorSelect() {
    const select = document.getElementById("asignacionTutorId");
    const search = document.getElementById("tutorSearch").value.trim().toLowerCase();
    const tutores = usuariosCache.filter((usuario) => usuario.rol === "TUTOR");
    const filtered = tutores.filter((usuario) => {
        const haystack = `${usuario.nombreCompleto} ${usuario.correo}`.toLowerCase();
        return !search || haystack.includes(search);
    });
    select.innerHTML = filtered.map((usuario) => `
        <option value="${usuario.id}">${PeerlinkApp.escapeHtml(usuario.nombreCompleto)} - ${PeerlinkApp.escapeHtml(usuario.correo)}</option>
    `).join("");
}

async function asignarTutorMateria(event) {
    event.preventDefault();
    const formElement = event.currentTarget;
    const payload = {
        tutorId: Number(document.getElementById("asignacionTutorId").value),
        materiaId: Number(document.getElementById("asignacionMateriaId").value)
    };

    try {
        const response = await PeerlinkApp.api("/api/materias/asignar-tutor", {
            method: "POST",
            body: JSON.stringify(payload)
        });
        adminConsole.print(response);
        formElement.reset();
        fillTutorSelect();
        fillMateriaSelect();
        await loadTutorMaterias();
    } catch (error) {
        adminConsole.printError(error);
    }
}

async function loadTutorMaterias() {
    try {
        relacionesCache = await PeerlinkApp.api("/api/materias/tutores-materias");
        renderTutorMateriasTable();
    } catch (error) {
        adminConsole.printError(error);
    }
}

function renderTutorMateriasTable() {
    const tbody = document.getElementById("tutorMateriasTableBody");
    const filterValue = document.getElementById("asignacionesFilterInput").value.trim().toLowerCase();
    const filtered = relacionesCache.filter((item) => {
        const haystack = `${item.tutorNombre} ${item.tutorCorreo} ${item.materiaNombre} ${item.idioma} ${item.facultad}`.toLowerCase();
        return !filterValue || haystack.includes(filterValue);
    });

    if (!filtered.length) {
        tbody.innerHTML = `<tr><td colspan="5">${PeerlinkApp.t("empty_assignments")}</td></tr>`;
        return;
    }

    tbody.innerHTML = filtered.map((item) => `
        <tr>
            <td>${PeerlinkApp.escapeHtml(item.tutorNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(item.tutorCorreo)}</td>
            <td>${PeerlinkApp.escapeHtml(item.materiaNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(item.facultad))}</td>
        </tr>
    `).join("");
}

async function loadUsuarios() {
    try {
        usuariosCache = await PeerlinkApp.api("/api/usuarios");
        fillTutorSelect();
        renderUsuariosTable();
    } catch (error) {
        adminConsole.printError(error);
    }
}

function getFilteredUsuarios() {
    const filterValue = document.getElementById("usuariosFilterInput").value.trim().toLowerCase();
    const rol = document.getElementById("usuariosRolFilter").value;

    return usuariosCache.filter((usuario) => {
        const haystack = `${usuario.nombreCompleto || ""} ${usuario.correo || ""} ${usuario.rol || ""}`.toLowerCase();
        return (!filterValue || haystack.includes(filterValue))
            && (!rol || usuario.rol === rol);
    });
}

function renderUsuariosTable() {
    const tbody = document.getElementById("usuariosTableBody");
    const filtered = getFilteredUsuarios();
    if (!filtered.length) {
        tbody.innerHTML = `<tr><td colspan="4">${PeerlinkApp.t("empty_users")}</td></tr>`;
        return;
    }

    tbody.innerHTML = filtered.map((usuario) => `
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
}

function clearUsuariosSearch() {
    document.getElementById("usuariosSearchForm").reset();
    renderUsuariosTable();
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
