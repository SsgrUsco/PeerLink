const tutorAuth = PeerlinkApp.ensureRole("TUTOR");
const tutorConsole = PeerlinkApp.bindConsole("consoleOutput");
const tutorFeedback = PeerlinkApp.bindFeedback("tutorFeedback");

let tutorReservations = [];
let tutorOffers = [];
let materiasCatalog = [];
let selectedTutorMateria = null;
let tutorWeekStart = startOfWeek(new Date());
let tutorScheduleView = "calendar";
const hiddenRespondedRequests = new Set();

if (tutorAuth) {
    PeerlinkApp.renderNavbar("navbarContainer");
    PeerlinkApp.applyI18n();
    bootTutor();
}

async function bootTutor() {
    fillLanguageSelect("solicitudIdioma", true);
    fillFacultySelect("solicitudFacultad", true);
    fillFacultySelect("crearFacultad", true);
    fillLanguageSelect("materiaTutorIdioma", true);
    fillFacultySelect("materiaTutorFacultad", true);

    document.getElementById("tutorSolicitudesTabBtn").addEventListener("click", () => showTutorTab("solicitudes"));
    document.getElementById("tutorCrearTabBtn").addEventListener("click", () => showTutorTab("crear"));
    document.getElementById("tutorHorarioTabBtn").addEventListener("click", () => showTutorTab("horario"));

    document.getElementById("filtrosSolicitudesForm").addEventListener("submit", (event) => {
        event.preventDefault();
        renderTutoriasTable();
    });
    document.getElementById("clearSolicitudesBtn").addEventListener("click", () => {
        document.getElementById("filtrosSolicitudesForm").reset();
        document.getElementById("ocultarRespondidas").checked = true;
        renderTutoriasTable();
    });
    document.getElementById("refreshReservasBtn").addEventListener("click", loadTutorias);
    document.getElementById("ocultarRespondidas").addEventListener("change", renderTutoriasTable);

    document.getElementById("crearBusquedaForm").addEventListener("submit", (event) => {
        event.preventDefault();
        renderCrearMaterias();
    });
    document.getElementById("clearCrearBtn").addEventListener("click", () => {
        document.getElementById("crearBusquedaForm").reset();
        renderCrearMaterias();
    });
    document.getElementById("refreshTutorMateriasBtn").addEventListener("click", loadTutorSources);
    document.getElementById("tutorOfertaForm").addEventListener("submit", createTutorOffer);

    document.getElementById("filtrosHorarioTutorForm").addEventListener("submit", (event) => {
        event.preventDefault();
        renderTutorSchedule();
    });
    document.getElementById("clearMateriasTutorBtn").addEventListener("click", () => {
        document.getElementById("filtrosHorarioTutorForm").reset();
        renderTutorSchedule();
    });
    document.getElementById("refreshHorarioBtn").addEventListener("click", loadTutorias);
    document.getElementById("downloadTutorReportBtn").addEventListener("click", downloadTutorReport);

    document.getElementById("tutorCalendarViewBtn").addEventListener("click", () => setTutorScheduleView("calendar"));
    document.getElementById("tutorListViewBtn").addEventListener("click", () => setTutorScheduleView("list"));
    document.getElementById("prevWeekBtn").addEventListener("click", () => changeTutorWeek(-7));
    document.getElementById("nextWeekBtn").addEventListener("click", () => changeTutorWeek(7));
    document.getElementById("thisWeekBtn").addEventListener("click", () => {
        tutorWeekStart = startOfWeek(new Date());
        syncTutorWeekPicker();
        renderTutorCalendar(getFilteredTutorScheduleItems());
    });
    document.getElementById("weekPicker").addEventListener("change", (event) => {
        tutorWeekStart = startOfWeek(event.target.value ? new Date(`${event.target.value}T00:00:00`) : new Date());
        syncTutorWeekPicker();
        renderTutorCalendar(getFilteredTutorScheduleItems());
    });

    setInitialOfferDateTime();
    syncTutorWeekPicker();
    showTutorTab("solicitudes");
    await Promise.all([loadTutorias(), loadTutorSources()]);
}

async function downloadTutorReport() {
    try {
        await PeerlinkApp.downloadFile(
            `/api/reportes/mis-tutorias.pdf${buildTutorReportQuery()}`,
            "peerlink-mis-tutorias.pdf"
        );
        tutorFeedback.success("feedback_report_downloaded");
    } catch (error) {
        tutorFeedback.error(error);
    }
}

function buildTutorReportQuery() {
    const params = new URLSearchParams();
    const idioma = document.getElementById("materiaTutorIdioma").value;
    const facultad = document.getElementById("materiaTutorFacultad").value;
    const fecha = document.getElementById("materiaTutorFecha").value;
    if (idioma) params.set("idioma", idioma);
    if (facultad) params.set("facultad", facultad);
    if (fecha) {
        params.set("desde", `${fecha}T00:00:00`);
        const hasta = new Date(`${fecha}T00:00:00`);
        hasta.setDate(hasta.getDate() + 1);
        params.set("hasta", toDateTimeLocalValue(hasta));
    }
    const query = params.toString();
    return query ? `?${query}` : "";
}

function showTutorTab(tab) {
    const solicitudesTab = document.getElementById("tutorSolicitudesTab");
    const crearTab = document.getElementById("tutorCrearTab");
    const horarioTab = document.getElementById("tutorHorarioTab");
    const solicitudesBtn = document.getElementById("tutorSolicitudesTabBtn");
    const crearBtn = document.getElementById("tutorCrearTabBtn");
    const horarioBtn = document.getElementById("tutorHorarioTabBtn");

    solicitudesTab.classList.toggle("hidden", tab !== "solicitudes");
    crearTab.classList.toggle("hidden", tab !== "crear");
    horarioTab.classList.toggle("hidden", tab !== "horario");
    setToggleButtonState(solicitudesBtn, tab === "solicitudes");
    setToggleButtonState(crearBtn, tab === "crear");
    setToggleButtonState(horarioBtn, tab === "horario");
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

function setInitialOfferDateTime() {
    const input = document.getElementById("tutorMateriaFechaHora");
    const minDate = new Date();
    minDate.setMinutes(minDate.getMinutes() + 5);
    input.min = toDateTimeLocalValue(minDate);
    input.value = toDateTimeLocalValue(minDate);
}

async function loadTutorias() {
    try {
        tutorReservations = await PeerlinkApp.api("/api/reservas/mis-tutorias");
        renderTutoriasTable();
        renderTutorSchedule();
        tutorFeedback.info("feedback_loaded_tutor_data");
    } catch (error) {
        tutorFeedback.error(error);
        tutorConsole.printError(error);
    }
}

async function loadTutorSources() {
    try {
        const [materias, offers] = await Promise.all([
            PeerlinkApp.api("/api/materias"),
            PeerlinkApp.api("/api/materias/mis-materias")
        ]);
        materiasCatalog = materias;
        tutorOffers = offers.filter((item) => item.fechaHora);
        renderCrearMaterias();
        renderTutorOffersTable();
        tutorFeedback.info("feedback_loaded_tutor_data");
    } catch (error) {
        tutorFeedback.error(error);
        tutorConsole.printError(error);
    }
}

function getFilteredTutorRequests() {
    const search = normalizeText(document.getElementById("solicitudBusqueda").value);
    const idioma = document.getElementById("solicitudIdioma").value;
    const facultad = document.getElementById("solicitudFacultad").value;
    const fecha = document.getElementById("solicitudFecha").value;
    const hora = document.getElementById("solicitudHora").value;
    const hideAnswered = document.getElementById("ocultarRespondidas").checked;

    return tutorReservations.filter((item) => {
        if (hiddenRespondedRequests.has(item.id)) {
            return false;
        }
        if (hideAnswered && item.estado !== "PENDIENTE") {
            return false;
        }
        const itemDate = new Date(item.fechaHora);
        const sameDate = !fecha || toDateInputValue(itemDate) === fecha;
        const sameHour = !hora || `${String(itemDate.getHours()).padStart(2, "0")}:${String(itemDate.getMinutes()).padStart(2, "0")}` === hora;
        const sameSearch = !search
            || normalizeText(item.materiaNombre).includes(search)
            || normalizeText(item.estudianteNombre).includes(search);
        return sameSearch
            && (!idioma || item.idioma === idioma)
            && (!facultad || item.facultad === facultad)
            && sameDate
            && sameHour;
    }).sort((a, b) => new Date(a.fechaHora) - new Date(b.fechaHora));
}

function renderTutoriasTable() {
    const reservas = getFilteredTutorRequests();
    document.getElementById("tutorSolicitudesCount").textContent = PeerlinkApp.t("results_count", { count: reservas.length });
    const tbody = document.getElementById("reservasTableBody");
    if (!reservas.length) {
        tbody.innerHTML = `<tr><td colspan="7">${PeerlinkApp.t("empty_tutor_requests")}</td></tr>`;
        return;
    }

    tbody.innerHTML = reservas.map((item) => `
        <tr>
            <td>${PeerlinkApp.escapeHtml(item.estudianteNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(item.materiaNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.formatDateTime(item.fechaHora))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(item.facultad))}</td>
            <td><span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(item.estado)}</span></td>
            <td>
                ${item.estado === "PENDIENTE" ? `
                    <div class="table-actions">
                        <button class="btn btn-sm btn-success" data-action="CONFIRMADA" data-id="${item.id}" type="button">${PeerlinkApp.t("action_confirm")}</button>
                        <button class="btn btn-sm btn-outline-danger" data-action="CANCELADA" data-id="${item.id}" type="button">${PeerlinkApp.t("action_cancel")}</button>
                    </div>
                ` : `
                    <div class="table-actions">
                        <button class="btn btn-sm btn-outline-secondary" data-action="PENDIENTE" data-id="${item.id}" type="button">${PeerlinkApp.t("action_undo")}</button>
                        <button class="btn btn-sm btn-outline-dark" data-hide-id="${item.id}" type="button">${PeerlinkApp.t("action_hide")}</button>
                    </div>
                `}
            </td>
        </tr>
    `).join("");

    tbody.querySelectorAll("[data-action]").forEach((button) => {
        button.addEventListener("click", () => updateEstado(button.dataset.id, button.dataset.action));
    });
    tbody.querySelectorAll("[data-hide-id]").forEach((button) => {
        button.addEventListener("click", () => {
            hiddenRespondedRequests.add(Number(button.dataset.hideId));
            renderTutoriasTable();
        });
    });
}

async function updateEstado(id, estado) {
    try {
        const response = await PeerlinkApp.api(`/api/reservas/${id}/estado`, {
            method: "PATCH",
            body: JSON.stringify({ estado })
        });
        hiddenRespondedRequests.delete(Number(id));
        tutorFeedback.success("feedback_tutor_request_updated");
        tutorConsole.print(response);
        await loadTutorias();
    } catch (error) {
        tutorFeedback.error(error);
        tutorConsole.printError(error);
    }
}

function getFilteredCreateMaterias() {
    const search = normalizeText(document.getElementById("crearBusqueda").value);
    const facultad = document.getElementById("crearFacultad").value;

    return materiasCatalog.filter((materia) => {
        const sameSearch = !search || normalizeText(materia.nombre).includes(search);
        return sameSearch && (!facultad || materia.facultad === facultad);
    }).sort((a, b) => a.nombre.localeCompare(b.nombre));
}

function renderCrearMaterias() {
    const container = document.getElementById("crearMateriasContainer");
    const materias = getFilteredCreateMaterias();
    document.getElementById("crearMatchesCount").textContent = PeerlinkApp.t("results_count", { count: materias.length });

    if (!materias.length) {
        container.innerHTML = `<div class="col-12"><div class="alert alert-light border mb-0">${PeerlinkApp.t("empty_filtered_subjects")}</div></div>`;
        selectTutorMateria(null);
        return;
    }

    container.innerHTML = materias.map((materia) => `
        <div class="col-lg-6">
            <article class="card border-0 shadow-sm h-100 offer-card ${selectedTutorMateria && selectedTutorMateria.id === materia.id ? "border border-success" : ""}">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start gap-3 mb-3">
                        <div>
                            <h3 class="h5 mb-1">${PeerlinkApp.escapeHtml(materia.nombre)}</h3>
                            <p class="text-secondary mb-0">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(materia.facultad))}</p>
                        </div>
                        <button class="btn btn-outline-success btn-sm" type="button" data-select-materia="${materia.id}">${PeerlinkApp.t("action_select")}</button>
                    </div>
                    <div class="d-flex flex-wrap gap-2">
                        <span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(materia.idioma))}</span>
                    </div>
                </div>
            </article>
        </div>
    `).join("");

    container.querySelectorAll("[data-select-materia]").forEach((button) => {
        button.addEventListener("click", () => {
            const materia = materias.find((item) => item.id === Number(button.dataset.selectMateria));
            selectTutorMateria(materia || null);
            renderCrearMaterias();
        });
    });
}

function selectTutorMateria(materia) {
    selectedTutorMateria = materia;
    const emptyState = document.getElementById("crearEmptyState");
    const form = document.getElementById("tutorOfertaForm");

    if (!materia) {
        emptyState.classList.remove("hidden");
        form.classList.add("hidden");
        return;
    }

    emptyState.classList.add("hidden");
    form.classList.remove("hidden");
    document.getElementById("tutorMateriaNombre").value = materia.nombre;
    document.getElementById("tutorMateriaIdioma").value = PeerlinkApp.labelForLanguage(materia.idioma);
    document.getElementById("tutorMateriaFacultad").value = PeerlinkApp.labelForFaculty(materia.facultad);
}

async function createTutorOffer(event) {
    event.preventDefault();
    if (!selectedTutorMateria) {
        return;
    }

    try {
        const response = await PeerlinkApp.api("/api/materias/mis-materias", {
            method: "POST",
            body: JSON.stringify({
                materiaId: selectedTutorMateria.id,
                fechaHora: document.getElementById("tutorMateriaFechaHora").value
            })
        });
        tutorFeedback.success("feedback_tutor_offer_saved");
        tutorConsole.print(response);
        setInitialOfferDateTime();
        await loadTutorSources();
        showTutorTab("crear");
    } catch (error) {
        tutorFeedback.error(error);
        tutorConsole.printError(error);
    }
}

function renderTutorOffersTable() {
    const tbody = document.getElementById("tutorOfertasTableBody");
    if (!tutorOffers.length) {
        tbody.innerHTML = `<tr><td colspan="5">${PeerlinkApp.t("empty_available_tutoring")}</td></tr>`;
        return;
    }

    tbody.innerHTML = tutorOffers
        .sort((a, b) => new Date(a.fechaHora) - new Date(b.fechaHora))
        .map((item) => `
            <tr>
                <td>${PeerlinkApp.escapeHtml(item.materiaNombre)}</td>
                <td>${PeerlinkApp.escapeHtml(PeerlinkApp.formatDateTime(item.fechaHora))}</td>
                <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</td>
                <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(item.facultad))}</td>
                <td><button class="btn btn-sm btn-outline-danger" data-remove-materia="${item.materiaId}" type="button">${PeerlinkApp.t("action_remove")}</button></td>
            </tr>
        `).join("");

    tbody.querySelectorAll("[data-remove-materia]").forEach((button) => {
        button.addEventListener("click", () => removeTutorOffer(Number(button.dataset.removeMateria)));
    });
}

async function removeTutorOffer(materiaId) {
    try {
        await PeerlinkApp.api(`/api/materias/mis-materias/${materiaId}`, { method: "DELETE" });
        tutorFeedback.success("feedback_tutor_offer_deleted");
        await loadTutorSources();
    } catch (error) {
        tutorFeedback.error(error);
        tutorConsole.printError(error);
    }
}

function getFilteredTutorScheduleItems() {
    const search = normalizeText(document.getElementById("horarioBusquedaTutor").value);
    const idioma = document.getElementById("materiaTutorIdioma").value;
    const facultad = document.getElementById("materiaTutorFacultad").value;
    const fecha = document.getElementById("materiaTutorFecha").value;
    const hora = document.getElementById("materiaTutorHora").value;

    return tutorReservations.filter((item) => {
        if (item.estado === "CANCELADA") {
            return false;
        }
        const itemDate = new Date(item.fechaHora);
        const sameDate = !fecha || toDateInputValue(itemDate) === fecha;
        const sameHour = !hora || `${String(itemDate.getHours()).padStart(2, "0")}:${String(itemDate.getMinutes()).padStart(2, "0")}` === hora;
        const sameSearch = !search
            || normalizeText(item.materiaNombre).includes(search)
            || normalizeText(item.estudianteNombre).includes(search);
        return sameSearch
            && (!idioma || item.idioma === idioma)
            && (!facultad || item.facultad === facultad)
            && sameDate
            && sameHour;
    }).sort((a, b) => new Date(a.fechaHora) - new Date(b.fechaHora));
}

function renderTutorSchedule() {
    const items = getFilteredTutorScheduleItems();
    document.getElementById("tutorHorarioCount").textContent = PeerlinkApp.t("reservations_count", { count: items.length });
    renderTutorScheduleList(items);
    renderTutorCalendar(items);
}

function renderTutorScheduleList(items) {
    const tbody = document.getElementById("tutorHorarioTableBody");
    if (!items.length) {
        tbody.innerHTML = `<tr><td colspan="6">${PeerlinkApp.t("empty_tutor_requests")}</td></tr>`;
        return;
    }

    tbody.innerHTML = items.map((item) => `
        <tr>
            <td>${PeerlinkApp.escapeHtml(item.materiaNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(item.estudianteNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.formatDateTime(item.fechaHora))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(item.facultad))}</td>
            <td><span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(item.estado)}</span></td>
        </tr>
    `).join("");
}

function setTutorScheduleView(view) {
    tutorScheduleView = view;
    document.getElementById("tutorCalendarPanel").classList.toggle("hidden", view !== "calendar");
    document.getElementById("tutorListPanel").classList.toggle("hidden", view !== "list");
    setToggleButtonState(document.getElementById("tutorCalendarViewBtn"), view === "calendar");
    setToggleButtonState(document.getElementById("tutorListViewBtn"), view === "list");
}

function changeTutorWeek(days) {
    const next = new Date(tutorWeekStart);
    next.setDate(next.getDate() + days);
    tutorWeekStart = startOfWeek(next);
    syncTutorWeekPicker();
    renderTutorCalendar(getFilteredTutorScheduleItems());
}

function syncTutorWeekPicker() {
    document.getElementById("weekPicker").value = toDateInputValue(tutorWeekStart);
}

function renderTutorCalendar(items) {
    const calendar = document.getElementById("weeklyCalendar");
    const weekStart = startOfWeek(tutorWeekStart);
    const weekEnd = new Date(weekStart);
    weekEnd.setDate(weekEnd.getDate() + 7);
    const inWeek = items.filter((item) => {
        const date = new Date(item.fechaHora);
        return date >= weekStart && date < weekEnd;
    });

    document.getElementById("weekRangeLabel").textContent = PeerlinkApp.t("calendar_week_of", {
        date: PeerlinkApp.formatDate(weekStart, { day: "2-digit", month: "long", year: "numeric" })
    });

    if (!inWeek.length) {
        calendar.innerHTML = `<div class="weekly-calendar-empty">${PeerlinkApp.t("empty_week_schedule")}</div>`;
        return;
    }

    const dayStarts = Array.from({ length: 7 }, (_, index) => {
        const date = new Date(weekStart);
        date.setDate(date.getDate() + index);
        return date;
    });
    const hours = buildHourRange(inWeek);
    const cells = [`<div class="weekly-calendar-cell header">${PeerlinkApp.t("calendar_hour")}</div>`];

    dayStarts.forEach((date) => {
        cells.push(`
            <div class="weekly-calendar-cell header">
                <div>${date.toLocaleDateString(PeerlinkApp.getLocale(), { weekday: "short" })}</div>
                <div class="small text-secondary">${PeerlinkApp.formatDate(date, { day: "2-digit", month: "2-digit" })}</div>
            </div>
        `);
    });

    hours.forEach((hour) => {
        cells.push(`<div class="weekly-calendar-cell hour">${String(hour).padStart(2, "0")}:00</div>`);
        dayStarts.forEach((date) => {
            const entries = inWeek.filter((item) => isSameCalendarDay(item.fechaHora, date) && new Date(item.fechaHora).getHours() === hour);
            cells.push(`
                <div class="weekly-calendar-cell">
                    <div class="weekly-slot-list">
                        ${entries.map(renderTutorSlot).join("")}
                    </div>
                </div>
            `);
        });
    });

    calendar.innerHTML = `<div class="weekly-calendar-grid">${cells.join("")}</div>`;
}

function renderTutorSlot(item) {
    return `
        <article class="weekly-slot">
            <div class="weekly-slot-title">${PeerlinkApp.escapeHtml(item.materiaNombre)}</div>
            <div class="weekly-slot-meta">${PeerlinkApp.escapeHtml(item.estudianteNombre)}</div>
            <div class="weekly-slot-meta">${PeerlinkApp.escapeHtml(PeerlinkApp.formatTime(item.fechaHora))}</div>
            <div class="weekly-slot-meta">${PeerlinkApp.escapeHtml(item.estado)}</div>
        </article>
    `;
}

function buildHourRange(items) {
    const hours = items.map((item) => new Date(item.fechaHora).getHours());
    const start = Math.min(6, ...hours);
    const end = Math.max(21, ...hours);
    return Array.from({ length: end - start + 1 }, (_, index) => start + index);
}

function startOfWeek(dateInput) {
    const date = new Date(dateInput);
    const normalized = new Date(date.getFullYear(), date.getMonth(), date.getDate());
    const day = normalized.getDay();
    const diff = day === 0 ? -6 : 1 - day;
    normalized.setDate(normalized.getDate() + diff);
    return normalized;
}

function isSameCalendarDay(value, date) {
    const current = new Date(value);
    return current.getFullYear() === date.getFullYear()
        && current.getMonth() === date.getMonth()
        && current.getDate() === date.getDate();
}

function normalizeText(value) {
    return String(value || "")
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "")
        .trim()
        .toLowerCase();
}

function toDateInputValue(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
}

function toDateTimeLocalValue(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}
