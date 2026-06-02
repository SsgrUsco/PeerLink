const adminAuth = PeerlinkApp.ensureRole("ADMIN");
const adminConsole = PeerlinkApp.bindConsole("consoleOutput");
const adminFeedback = PeerlinkApp.bindFeedback("adminFeedback");
let materiasCache = [];
let usuariosCache = [];
let relacionesCache = [];
let selectedMateriaCardId = null;
const pendingMateriaDeletes = new Map();
const pendingUserDeletes = new Map();
const UNDO_DELETE_DELAY_MS = 12000;

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
    document.getElementById("downloadAdminReportBtn").addEventListener("click", downloadAdminReport);
    document.getElementById("downloadAdminTutorsReportBtn").addEventListener("click", downloadAdminTutorsReport);
    document.getElementById("downloadAdminUsersReportBtn").addEventListener("click", downloadAdminUsersReport);

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
    document.getElementById("adminPasswordResetForm").addEventListener("submit", resetUserPassword);
    document.getElementById("cancelAdminPasswordResetBtn").addEventListener("click", hidePasswordResetForm);
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
    await Promise.all([loadMaterias({ silent: true }), loadUsuarios({ silent: true }), loadTutorMaterias()]);
}

async function downloadAdminReport() {
    try {
        await PeerlinkApp.downloadFile("/api/reportes/admin/resumen.pdf", "peerlink-admin-resumen.pdf");
        adminFeedback.success("feedback_report_downloaded");
    } catch (error) {
        adminFeedback.error(error);
    }
}

async function downloadAdminTutorsReport() {
    try {
        await PeerlinkApp.downloadFile("/api/reportes/admin/tutores.pdf", "peerlink-admin-tutores.pdf");
        adminFeedback.success("feedback_report_downloaded");
    } catch (error) {
        adminFeedback.error(error);
    }
}

async function downloadAdminUsersReport() {
    try {
        await PeerlinkApp.downloadFile("/api/reportes/admin/usuarios.pdf", "peerlink-admin-usuarios.pdf");
        adminFeedback.success("feedback_report_downloaded");
    } catch (error) {
        adminFeedback.error(error);
    }
}

function showAdminTab(tab) {
    const materiasTab = document.getElementById("adminMateriasTab");
    const tutoresTab = document.getElementById("adminTutoresTab");
    const usuariosTab = document.getElementById("adminUsuariosTab");

    materiasTab.classList.toggle("hidden", tab !== "materias");
    tutoresTab.classList.toggle("hidden", tab !== "tutores");
    usuariosTab.classList.toggle("hidden", tab !== "usuarios");

    setToggleButtonState(document.getElementById("adminMateriasTabBtn"), tab === "materias");
    setToggleButtonState(document.getElementById("adminTutoresTabBtn"), tab === "tutores");
    setToggleButtonState(document.getElementById("adminUsuariosTabBtn"), tab === "usuarios");
    updateAdminTopbarTitle(tab);
}

function updateAdminTopbarTitle(tab) {
    const title = document.getElementById("adminTopbarTitle");
    if (!title) {
        return;
    }
    const titleKeyByTab = {
        materias: "admin_hero_title",
        tutores: "admin_hero_title_tutors",
        usuarios: "admin_hero_title_users"
    };
    const titleKey = titleKeyByTab[tab] || "admin_hero_title";
    title.dataset.i18n = titleKey;
    title.textContent = PeerlinkApp.t(titleKey);
}

function showMateriaSubtab(tab) {
    const crear = document.getElementById("adminMateriaCrearSection");
    const buscar = document.getElementById("adminMateriaBuscarSection");
    crear.classList.toggle("hidden", tab !== "crear");
    buscar.classList.toggle("hidden", tab !== "buscar");
    setToggleButtonState(document.getElementById("adminMateriaCrearTabBtn"), tab === "crear");
    setToggleButtonState(document.getElementById("adminMateriaBuscarTabBtn"), tab === "buscar");
}

function setToggleButtonState(button, active) {
    if (!button) {
        return;
    }
    if (!button.dataset.baseClass) {
        button.dataset.baseClass = button.className;
    }
    button.classList.toggle("btn-success", active);
    button.classList.toggle("btn-outline-success", !active);
    button.classList.toggle("btn-white", !active && button.dataset.baseClass.includes("btn-white"));
    button.classList.toggle("text-secondary", !active && button.dataset.baseClass.includes("text-secondary"));
    button.classList.toggle("text-hover-success", !active && button.dataset.baseClass.includes("text-hover-success"));
    button.setAttribute("aria-pressed", String(active));
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
    if (includeAllOption) {
        select.value = "";
    }
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

async function loadMaterias(options = {}) {
    try {
        materiasCache = await PeerlinkApp.api("/api/materias");
        fillMateriaSelect();
        renderMateriasMatches();
        if (!options.silent) {
            adminFeedback.info("feedback_loaded_admin_data");
        }
    } catch (error) {
        adminFeedback.error(error);
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
    document.getElementById("adminMateriasCount").textContent = PeerlinkApp.t("results_count", { count: filtered.length });

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
            scheduleMateriaDelete(Number(button.dataset.deleteMateria));
        });
    });
}

function scheduleMateriaDelete(materiaId) {
    const materia = materiasCache.find((item) => Number(item.id) === Number(materiaId));
    if (!materia) {
        return;
    }

    clearPendingDelete(pendingMateriaDeletes, materiaId);
    materiasCache = materiasCache.filter((item) => Number(item.id) !== Number(materiaId));
    selectedMateriaCardId = null;
    renderMateriasMatches();
    showPendingDeleteUndo({
        containerId: "adminMateriaUndoFeedback",
        messageKey: "feedback_subject_deleted",
        detail: materia.nombre,
        onUndo: () => {
            clearPendingDelete(pendingMateriaDeletes, materiaId);
            materiasCache.push(materia);
            materiasCache.sort((a, b) => a.nombre.localeCompare(b.nombre));
            renderMateriasMatches();
            showRestoredMessage("adminMateriaUndoFeedback");
        }
    });

    const timeoutId = window.setTimeout(async () => {
        pendingMateriaDeletes.delete(materiaId);
        try {
            await PeerlinkApp.api(`/api/materias/${materiaId}`, { method: "DELETE" });
            await Promise.all([loadMaterias(), loadTutorMaterias()]);
        } catch (error) {
            materiasCache.push(materia);
            renderMateriasMatches();
            adminFeedback.error(error);
            adminConsole.printError(error);
        }
    }, UNDO_DELETE_DELAY_MS);
    pendingMateriaDeletes.set(materiaId, timeoutId);
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
        adminFeedback.success("feedback_subject_saved");
        adminConsole.print(response);
        resetMateriaForm();
        await Promise.all([loadMaterias(), loadTutorMaterias()]);
        showAdminTab("materias");
        showMateriaSubtab("buscar");
    } catch (error) {
        adminFeedback.error(error);
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
        adminFeedback.success("feedback_assignment_saved");
        adminConsole.print(response);
        formElement.reset();
        fillTutorSelect();
        fillMateriaSelect();
        await loadTutorMaterias();
    } catch (error) {
        adminFeedback.error(error);
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
    document.getElementById("adminAsignacionesCount").textContent = PeerlinkApp.t("results_count", { count: filtered.length });

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

async function loadUsuarios(options = {}) {
    try {
        usuariosCache = await PeerlinkApp.api("/api/usuarios");
        fillTutorSelect();
        renderUsuariosTable();
        if (!options.silent) {
            adminFeedback.info("feedback_loaded_admin_data");
        }
    } catch (error) {
        adminFeedback.error(error);
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
    document.getElementById("adminUsuariosCount").textContent = PeerlinkApp.t("results_count", { count: filtered.length });
    if (!filtered.length) {
        tbody.innerHTML = `<tr><td colspan="4">${PeerlinkApp.t("empty_users")}</td></tr>`;
        return;
    }

    tbody.innerHTML = filtered.map((usuario) => `
            <tr>
                <td>${PeerlinkApp.escapeHtml(usuario.nombreCompleto || "")}</td>
                <td>${PeerlinkApp.escapeHtml(usuario.correo || "")}</td>
                <td><span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(usuario.rol || "")}</span></td>
                <td class="text-end">
                    <div class="d-inline-flex flex-wrap justify-content-end gap-2">
                        <button class="btn btn-sm btn-outline-success" data-reset-user-id="${usuario.id}" data-reset-user-name="${PeerlinkApp.escapeHtml(usuario.nombreCompleto || usuario.correo || "")}" type="button">${PeerlinkApp.t("action_reset_password")}</button>
                        <button class="btn btn-sm btn-outline-danger" data-delete-user-id="${usuario.id}" data-delete-user-name="${PeerlinkApp.escapeHtml(usuario.nombreCompleto || usuario.correo || "")}" type="button">${PeerlinkApp.t("action_delete")}</button>
                    </div>
                </td>
            </tr>
        `).join("");

    tbody.querySelectorAll("[data-reset-user-id]").forEach((button) => {
        button.addEventListener("click", () => {
            showPasswordResetForm(Number(button.dataset.resetUserId), button.dataset.resetUserName);
        });
    });

    tbody.querySelectorAll("[data-delete-user-id]").forEach((button) => {
        button.addEventListener("click", () => {
            if (!confirm(PeerlinkApp.t("confirm_delete_user", { name: button.dataset.deleteUserName }))) {
                return;
            }
            scheduleUserDelete(Number(button.dataset.deleteUserId));
        });
    });
}

function showPasswordResetForm(userId, userName) {
    const form = document.getElementById("adminPasswordResetForm");
    document.getElementById("adminPasswordResetUserId").value = userId;
    document.getElementById("adminPasswordResetValue").value = "";
    document.getElementById("adminPasswordResetTarget").textContent = PeerlinkApp.t("admin_password_reset_target", { name: userName });
    form.classList.remove("hidden");
    form.classList.remove("was-validated");
    document.getElementById("adminPasswordResetFeedback").classList.add("hidden");
    document.getElementById("adminPasswordResetValue").focus();
}

function hidePasswordResetForm() {
    const form = document.getElementById("adminPasswordResetForm");
    form.reset();
    form.classList.add("hidden");
    form.classList.remove("was-validated");
    document.getElementById("adminPasswordResetFeedback").classList.add("hidden");
}

async function resetUserPassword(event) {
    event.preventDefault();
    const form = event.currentTarget;
    if (!form.checkValidity()) {
        form.classList.add("was-validated");
        return;
    }

    const userId = document.getElementById("adminPasswordResetUserId").value;
    const passwordNueva = document.getElementById("adminPasswordResetValue").value;
    try {
        await PeerlinkApp.api(`/api/usuarios/${userId}/password`, {
            method: "PATCH",
            body: JSON.stringify({ passwordNueva })
        });
        adminFeedback.success("feedback_password_reset");
        hidePasswordResetForm();
    } catch (error) {
        document.getElementById("adminPasswordResetFeedback").className = "alert alert-danger border shadow-sm";
        document.getElementById("adminPasswordResetFeedback").textContent = error.message || PeerlinkApp.t("error_internal");
        document.getElementById("adminPasswordResetFeedback").classList.remove("hidden");
        adminConsole.printError(error);
    }
}

function scheduleUserDelete(userId) {
    const usuario = usuariosCache.find((item) => Number(item.id) === Number(userId));
    if (!usuario) {
        return;
    }

    clearPendingDelete(pendingUserDeletes, userId);
    usuariosCache = usuariosCache.filter((item) => Number(item.id) !== Number(userId));
    renderUsuariosTable();
    showPendingDeleteUndo({
        containerId: "adminUserUndoFeedback",
        messageKey: "feedback_user_deleted",
        detail: usuario.nombreCompleto || usuario.correo,
        onUndo: () => {
            clearPendingDelete(pendingUserDeletes, userId);
            usuariosCache.push(usuario);
            usuariosCache.sort((a, b) => (a.nombreCompleto || "").localeCompare(b.nombreCompleto || ""));
            renderUsuariosTable();
            showRestoredMessage("adminUserUndoFeedback");
        }
    });

    const timeoutId = window.setTimeout(async () => {
        pendingUserDeletes.delete(userId);
        try {
            await PeerlinkApp.api(`/api/usuarios/${userId}`, { method: "DELETE" });
            await Promise.all([loadUsuarios(), loadTutorMaterias()]);
        } catch (error) {
            usuariosCache.push(usuario);
            renderUsuariosTable();
            adminFeedback.error(error);
            adminConsole.printError(error);
        }
    }, UNDO_DELETE_DELAY_MS);
    pendingUserDeletes.set(userId, timeoutId);
}

function clearPendingDelete(map, id) {
    const timeoutId = map.get(id);
    if (timeoutId) {
        window.clearTimeout(timeoutId);
        map.delete(id);
    }
}

function showPendingDeleteUndo({ containerId, messageKey, detail, onUndo }) {
    const node = document.getElementById(containerId) || document.getElementById("adminFeedback");
    if (!node) {
        return;
    }

    node.className = "alert alert-warning border shadow-sm d-flex flex-column flex-md-row align-items-md-center justify-content-between gap-3";
    node.innerHTML = `
        <div>
            <strong>${PeerlinkApp.escapeHtml(PeerlinkApp.t(messageKey))}</strong>
            <div class="small text-secondary">${PeerlinkApp.escapeHtml(detail || "")}</div>
        </div>
        <button type="button" class="btn btn-sm btn-outline-success" data-admin-undo-delete>${PeerlinkApp.escapeHtml(PeerlinkApp.t("action_undo"))}</button>
    `;
    node.classList.remove("hidden");
    node.querySelector("[data-admin-undo-delete]").addEventListener("click", onUndo, { once: true });
}

function showRestoredMessage(containerId) {
    const node = document.getElementById(containerId) || document.getElementById("adminFeedback");
    if (!node) {
        return;
    }

    node.className = "alert alert-success border shadow-sm";
    node.textContent = PeerlinkApp.t("feedback_action_restored");
    node.classList.remove("hidden");
}

function clearUsuariosSearch() {
    document.getElementById("usuariosSearchForm").reset();
    renderUsuariosTable();
}

async function saveUsuario(event) {
    event.preventDefault();
    const formElement = event.currentTarget;
    if (!formElement.checkValidity()) {
        formElement.classList.add("was-validated");
        return;
    }
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
        adminFeedback.success("feedback_user_saved");
        adminConsole.print(response);
        formElement.reset();
        formElement.classList.remove("was-validated");
        await loadUsuarios();
    } catch (error) {
        adminFeedback.error(error);
        adminConsole.printError(error);
    }
}
