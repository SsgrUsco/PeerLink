const studentAuth = PeerlinkApp.ensureRole("ESTUDIANTE");
const studentConsole = PeerlinkApp.bindConsole("consoleOutput");
const studentFeedback = PeerlinkApp.bindFeedback("studentFeedback");

let materiasCatalog = [];
let tutorAssignments = [];
let availableOffers = [];
let studentReservations = [];
let selectedOffer = null;
let selectedSolicitudGroupKey = null;
let selectedWeekStart = startOfWeek(new Date());
let studentScheduleView = "calendar";

if (studentAuth) {
    PeerlinkApp.renderNavbar("navbarContainer");
    PeerlinkApp.applyI18n();
    bootStudent();
}

async function bootStudent() {
    fillFacultySelect("solicitarFacultad", true);
    fillLanguageSelect("aceptarIdioma", true);
    fillFacultySelect("aceptarFacultad", true);
    fillLanguageSelect("horarioIdioma", true);
    fillFacultySelect("horarioFacultad", true);

    document.getElementById("studentTutoriasTabBtn").addEventListener("click", () => showStudentTab("tutorias"));
    document.getElementById("studentHorarioTabBtn").addEventListener("click", () => showStudentTab("horario"));
    document.getElementById("studentSolicitarTabBtn").addEventListener("click", () => showStudentTutoriasSubtab("solicitar"));
    document.getElementById("studentAceptarTabBtn").addEventListener("click", () => showStudentTutoriasSubtab("aceptar"));

    document.getElementById("solicitarBusquedaForm").addEventListener("submit", (event) => {
        event.preventDefault();
        renderSolicitarMatches();
    });
    document.getElementById("clearSolicitarBtn").addEventListener("click", () => {
        document.getElementById("solicitarBusquedaForm").reset();
        renderSolicitarMatches();
    });
    document.getElementById("refreshSolicitarBtn").addEventListener("click", loadStudentSources);
    document.getElementById("solicitudIdioma").addEventListener("change", syncSolicitudTutorOptions);
    document.getElementById("solicitudReservaForm").addEventListener("submit", createDirectReservation);

    document.getElementById("aceptarBusquedaForm").addEventListener("submit", (event) => {
        event.preventDefault();
        renderAvailableOffers();
    });
    document.getElementById("clearAceptarBtn").addEventListener("click", () => {
        document.getElementById("aceptarBusquedaForm").reset();
        renderAvailableOffers();
    });
    document.getElementById("refreshTutorMateriasBtn").addEventListener("click", loadStudentSources);
    document.getElementById("confirmReservaBtn").addEventListener("click", createReservationFromSelectedOffer);

    document.getElementById("horarioReservasForm").addEventListener("submit", (event) => {
        event.preventDefault();
        renderStudentSchedule();
    });
    document.getElementById("clearHorarioReservasBtn").addEventListener("click", () => {
        document.getElementById("horarioReservasForm").reset();
        renderStudentSchedule();
    });
    document.getElementById("refreshReservasBtn").addEventListener("click", loadMyReservations);

    document.getElementById("studentCalendarViewBtn").addEventListener("click", () => setStudentScheduleView("calendar"));
    document.getElementById("studentListViewBtn").addEventListener("click", () => setStudentScheduleView("list"));
    document.getElementById("prevWeekBtn").addEventListener("click", () => changeWeek(-7));
    document.getElementById("nextWeekBtn").addEventListener("click", () => changeWeek(7));
    document.getElementById("thisWeekBtn").addEventListener("click", () => {
        selectedWeekStart = startOfWeek(new Date());
        syncWeekPicker();
        renderStudentCalendar(getFilteredStudentReservations());
    });
    document.getElementById("weekPicker").addEventListener("change", (event) => {
        selectedWeekStart = startOfWeek(event.target.value ? new Date(`${event.target.value}T00:00:00`) : new Date());
        syncWeekPicker();
        renderStudentCalendar(getFilteredStudentReservations());
    });

    setInitialSolicitudDateTime();
    syncWeekPicker();
    showStudentTab("tutorias");
    showStudentTutoriasSubtab("solicitar");
    await Promise.all([loadStudentSources(), loadMyReservations()]);
}

function showStudentTab(tab) {
    const tutoriasTab = document.getElementById("studentTutoriasTab");
    const horarioTab = document.getElementById("studentHorarioTab");
    const tutoriasBtn = document.getElementById("studentTutoriasTabBtn");
    const horarioBtn = document.getElementById("studentHorarioTabBtn");

    tutoriasTab.classList.toggle("hidden", tab !== "tutorias");
    horarioTab.classList.toggle("hidden", tab !== "horario");
    tutoriasBtn.className = tab === "tutorias" ? "btn btn-success" : "btn btn-outline-success";
    horarioBtn.className = tab === "horario" ? "btn btn-success" : "btn btn-outline-success";
}

function showStudentTutoriasSubtab(tab) {
    const solicitarTab = document.getElementById("studentSolicitarTab");
    const aceptarTab = document.getElementById("studentAceptarTab");
    const solicitarBtn = document.getElementById("studentSolicitarTabBtn");
    const aceptarBtn = document.getElementById("studentAceptarTabBtn");

    solicitarTab.classList.toggle("hidden", tab !== "solicitar");
    aceptarTab.classList.toggle("hidden", tab !== "aceptar");
    solicitarBtn.className = tab === "solicitar" ? "btn btn-success" : "btn btn-outline-success";
    aceptarBtn.className = tab === "aceptar" ? "btn btn-success" : "btn btn-outline-success";
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

function setInitialSolicitudDateTime() {
    const input = document.getElementById("solicitudFechaHora");
    const initialDate = new Date();
    initialDate.setDate(initialDate.getDate() + 1);
    initialDate.setHours(8, 0, 0, 0);
    input.min = toDateTimeLocalValue(new Date());
    input.value = toDateTimeLocalValue(initialDate);
}

async function loadStudentSources() {
    try {
        const [materias, asignaciones] = await Promise.all([
            PeerlinkApp.api("/api/materias"),
            PeerlinkApp.api("/api/materias/tutores-materias")
        ]);
        materiasCatalog = materias;
        tutorAssignments = asignaciones;
        availableOffers = tutorAssignments.filter((item) => item.fechaHora);
        renderSolicitarMatches();
        renderAvailableOffers();
        studentFeedback.info("feedback_loaded_student_data");
    } catch (error) {
        studentFeedback.error(error);
        studentConsole.printError(error);
    }
}

function renderSolicitarMatches() {
    const container = document.getElementById("solicitarMateriasContainer");
    const groups = getFilteredSolicitudGroups();
    document.getElementById("solicitarResultsCount").textContent = PeerlinkApp.t("results_count", { count: groups.length });

    if (!groups.length) {
        container.innerHTML = `<div class="col-12"><div class="alert alert-light border mb-0">${PeerlinkApp.t("empty_filtered_subjects")}</div></div>`;
        selectSolicitudGroup(null);
        return;
    }

    container.innerHTML = groups.map((group) => `
        <div class="col-lg-6">
            <article class="card border-0 shadow-sm h-100 offer-card ${selectedSolicitudGroupKey === group.key ? "border border-success" : ""}">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start gap-3 mb-3">
                        <div>
                            <h3 class="h5 mb-1">${PeerlinkApp.escapeHtml(group.nombre)}</h3>
                            <p class="text-secondary mb-0">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(group.facultad))}</p>
                        </div>
                        <button class="btn btn-outline-success btn-sm" type="button" data-select-solicitud="${PeerlinkApp.escapeHtml(group.key)}">${PeerlinkApp.t("action_select")}</button>
                    </div>
                    <div class="d-flex flex-wrap gap-2">
                        <span class="badge text-bg-light border">${group.variants.length} idioma(s)</span>
                        <span class="badge text-bg-light border">${group.tutores} tutor(es)</span>
                    </div>
                </div>
            </article>
        </div>
    `).join("");

    container.querySelectorAll("[data-select-solicitud]").forEach((button) => {
        button.addEventListener("click", () => {
            selectSolicitudGroup(button.dataset.selectSolicitud);
            renderSolicitarMatches();
        });
    });
}

function getFilteredSolicitudGroups() {
    const search = normalizeText(document.getElementById("solicitarBusqueda").value);
    const facultad = document.getElementById("solicitarFacultad").value;
    const grouped = new Map();

    materiasCatalog.forEach((materia) => {
        const sameFaculty = !facultad || materia.facultad === facultad;
        const sameSearch = !search || normalizeText(materia.nombre).includes(search);
        if (!sameFaculty || !sameSearch) {
            return;
        }

        const key = `${materia.nombre}::${materia.facultad}`;
        if (!grouped.has(key)) {
            grouped.set(key, {
                key,
                nombre: materia.nombre,
                facultad: materia.facultad,
                variants: [],
                tutores: 0
            });
        }
        grouped.get(key).variants.push(materia);
    });

    grouped.forEach((group) => {
        const materiaIds = new Set(group.variants.map((item) => item.id));
        const tutorIds = new Set(
            tutorAssignments
                .filter((item) => materiaIds.has(item.materiaId))
                .map((item) => item.tutorId)
        );
        group.tutores = tutorIds.size;
    });

    return Array.from(grouped.values()).sort((a, b) => a.nombre.localeCompare(b.nombre));
}

function selectSolicitudGroup(groupKey) {
    selectedSolicitudGroupKey = groupKey;
    const emptyState = document.getElementById("solicitudEmptyState");
    const form = document.getElementById("solicitudReservaForm");

    if (!groupKey) {
        emptyState.classList.remove("hidden");
        form.classList.add("hidden");
        return;
    }

    const group = getFilteredSolicitudGroups().find((item) => item.key === groupKey)
        || buildAllSolicitudGroups().find((item) => item.key === groupKey);
    if (!group) {
        emptyState.classList.remove("hidden");
        form.classList.add("hidden");
        return;
    }

    emptyState.classList.add("hidden");
    form.classList.remove("hidden");
    document.getElementById("solicitudMateriaNombre").value = group.nombre;
    document.getElementById("solicitudMateriaFacultad").value = PeerlinkApp.labelForFaculty(group.facultad);

    const idiomaSelect = document.getElementById("solicitudIdioma");
    idiomaSelect.innerHTML = group.variants.map((item) => `
        <option value="${item.id}">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</option>
    `).join("");
    syncSolicitudTutorOptions();
}

function buildAllSolicitudGroups() {
    const previousSearch = document.getElementById("solicitarBusqueda").value;
    const previousFaculty = document.getElementById("solicitarFacultad").value;
    document.getElementById("solicitarBusqueda").value = "";
    document.getElementById("solicitarFacultad").value = "";
    const groups = getFilteredSolicitudGroups();
    document.getElementById("solicitarBusqueda").value = previousSearch;
    document.getElementById("solicitarFacultad").value = previousFaculty;
    return groups;
}

function syncSolicitudTutorOptions() {
    const materiaId = Number(document.getElementById("solicitudIdioma").value);
    const tutorSelect = document.getElementById("solicitudTutor");
    const tutors = tutorAssignments
        .filter((item) => item.materiaId === materiaId)
        .reduce((accumulator, item) => {
            if (!accumulator.some((current) => current.tutorId === item.tutorId)) {
                accumulator.push(item);
            }
            return accumulator;
        }, []);

    if (!tutors.length) {
        tutorSelect.innerHTML = `<option value="">${PeerlinkApp.t("empty_subject_tutors")}</option>`;
        return;
    }

    tutorSelect.innerHTML = tutors.map((item) => `
        <option value="${item.tutorId}">${PeerlinkApp.escapeHtml(item.tutorNombre)}</option>
    `).join("");
}

async function createDirectReservation(event) {
    event.preventDefault();

    const materiaId = Number(document.getElementById("solicitudIdioma").value);
    const tutorId = Number(document.getElementById("solicitudTutor").value);
    const fechaHora = document.getElementById("solicitudFechaHora").value;
    if (!materiaId || !tutorId || !fechaHora) {
        return;
    }

    try {
        const response = await PeerlinkApp.api("/api/reservas", {
            method: "POST",
            body: JSON.stringify({
                tutorId,
                materiaId,
                fechaHora
            })
        });
        studentFeedback.success("feedback_request_created");
        studentConsole.print(response);
        await loadMyReservations();
        showStudentTab("horario");
    } catch (error) {
        studentFeedback.error(error);
        studentConsole.printError(error);
    }
}

function renderAvailableOffers() {
    const container = document.getElementById("availableOffersContainer");
    const filtered = getFilteredOffers();
    document.getElementById("aceptarResultsCount").textContent = PeerlinkApp.t("results_count", { count: filtered.length });

    if (!filtered.length) {
        container.innerHTML = `<div class="col-12"><div class="alert alert-light border mb-0">${PeerlinkApp.t("empty_available_tutoring")}</div></div>`;
        selectOffer(null);
        return;
    }

    container.innerHTML = filtered.map((offer) => `
        <div class="col-lg-6">
            <article class="card border-0 shadow-sm h-100 offer-card ${selectedOffer && selectedOffer.tutorId === offer.tutorId && selectedOffer.materiaId === offer.materiaId ? "border border-success" : ""}">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start gap-3 mb-3">
                        <div>
                            <h3 class="h5 mb-1">${PeerlinkApp.escapeHtml(offer.materiaNombre)}</h3>
                            <p class="text-secondary mb-0">${PeerlinkApp.escapeHtml(offer.tutorNombre)}</p>
                        </div>
                        <button class="btn btn-outline-success btn-sm" type="button" data-select-offer="${offer.tutorId}-${offer.materiaId}">${PeerlinkApp.t("action_accept")}</button>
                    </div>
                    <div class="small text-secondary">${PeerlinkApp.escapeHtml(offer.tutorCorreo || "")}</div>
                    <div class="mt-3 d-flex flex-wrap gap-2">
                        <span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(offer.idioma))}</span>
                        <span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(offer.facultad))}</span>
                        <span class="badge text-bg-success-subtle border border-success-subtle">${PeerlinkApp.escapeHtml(PeerlinkApp.formatDateTime(offer.fechaHora))}</span>
                    </div>
                </div>
            </article>
        </div>
    `).join("");

    container.querySelectorAll("[data-select-offer]").forEach((button) => {
        button.addEventListener("click", () => {
            const [tutorId, materiaId] = button.dataset.selectOffer.split("-").map(Number);
            const offer = filtered.find((item) => item.tutorId === tutorId && item.materiaId === materiaId);
            selectOffer(offer || null);
            renderAvailableOffers();
        });
    });
}

function getFilteredOffers() {
    const search = normalizeText(document.getElementById("aceptarBusqueda").value);
    const idioma = document.getElementById("aceptarIdioma").value;
    const facultad = document.getElementById("aceptarFacultad").value;
    const fecha = document.getElementById("aceptarFecha").value;
    const hora = document.getElementById("aceptarHora").value;

    return availableOffers.filter((offer) => {
        const offerDate = new Date(offer.fechaHora);
        const sameDate = !fecha || toDateInputValue(offerDate) === fecha;
        const sameHour = !hora || `${String(offerDate.getHours()).padStart(2, "0")}:${String(offerDate.getMinutes()).padStart(2, "0")}` === hora;
        const sameSearch = !search
            || normalizeText(offer.materiaNombre).includes(search)
            || normalizeText(offer.tutorNombre).includes(search);
        return sameSearch
            && (!idioma || offer.idioma === idioma)
            && (!facultad || offer.facultad === facultad)
            && sameDate
            && sameHour;
    }).sort((a, b) => new Date(a.fechaHora) - new Date(b.fechaHora));
}

function selectOffer(offer) {
    selectedOffer = offer;
    const empty = document.getElementById("selectedOfferEmpty");
    const panel = document.getElementById("selectedOfferPanel");

    if (!offer) {
        empty.classList.remove("hidden");
        panel.classList.add("hidden");
        return;
    }

    empty.classList.add("hidden");
    panel.classList.remove("hidden");
    document.getElementById("selectedOfferMateria").value = offer.materiaNombre;
    document.getElementById("selectedOfferTutor").value = offer.tutorNombre;
    document.getElementById("selectedOfferIdioma").value = PeerlinkApp.labelForLanguage(offer.idioma);
    document.getElementById("selectedOfferFacultad").value = PeerlinkApp.labelForFaculty(offer.facultad);
    document.getElementById("selectedOfferFechaHora").value = PeerlinkApp.formatDateTime(offer.fechaHora);
}

async function createReservationFromSelectedOffer() {
    if (!selectedOffer) {
        return;
    }

    try {
        const response = await PeerlinkApp.api("/api/reservas", {
            method: "POST",
            body: JSON.stringify({
                tutorId: selectedOffer.tutorId,
                materiaId: selectedOffer.materiaId
            })
        });
        studentFeedback.success("feedback_reservation_created");
        studentConsole.print(response);
        await loadMyReservations();
        showStudentTab("horario");
    } catch (error) {
        studentFeedback.error(error);
        studentConsole.printError(error);
    }
}

async function loadMyReservations() {
    try {
        studentReservations = await PeerlinkApp.api("/api/reservas/mis-reservas");
        renderStudentSchedule();
        studentFeedback.info("feedback_schedule_loaded");
    } catch (error) {
        studentFeedback.error(error);
        studentConsole.printError(error);
    }
}

function renderStudentSchedule() {
    const filtered = getFilteredStudentReservations();
    document.getElementById("reservasResultsCount").textContent = PeerlinkApp.t("reservations_count", { count: filtered.length });
    renderReservationsList(filtered);
    renderStudentCalendar(filtered);
}

function getFilteredStudentReservations() {
    const search = normalizeText(document.getElementById("horarioBusqueda").value);
    const idioma = document.getElementById("horarioIdioma").value;
    const facultad = document.getElementById("horarioFacultad").value;
    const fecha = document.getElementById("horarioFecha").value;
    const hora = document.getElementById("horarioHora").value;

    return studentReservations.filter((item) => {
        const itemDate = new Date(item.fechaHora);
        const sameDate = !fecha || toDateInputValue(itemDate) === fecha;
        const sameHour = !hora || `${String(itemDate.getHours()).padStart(2, "0")}:${String(itemDate.getMinutes()).padStart(2, "0")}` === hora;
        const sameSearch = !search
            || normalizeText(item.materiaNombre).includes(search)
            || normalizeText(item.tutorNombre).includes(search);
        return sameSearch
            && (!idioma || item.idioma === idioma)
            && (!facultad || item.facultad === facultad)
            && sameDate
            && sameHour;
    }).sort((a, b) => new Date(a.fechaHora) - new Date(b.fechaHora));
}

function renderReservationsList(reservas) {
    const tbody = document.getElementById("reservasTableBody");
    if (!reservas.length) {
        tbody.innerHTML = `<tr><td colspan="6">${PeerlinkApp.t("empty_my_reservations")}</td></tr>`;
        return;
    }

    tbody.innerHTML = reservas.map((item) => `
        <tr>
            <td>${PeerlinkApp.escapeHtml(item.materiaNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(item.tutorNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.formatDateTime(item.fechaHora))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(item.facultad))}</td>
            <td><span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(item.estado)}</span></td>
        </tr>
    `).join("");
}

function setStudentScheduleView(view) {
    studentScheduleView = view;
    document.getElementById("studentCalendarPanel").classList.toggle("hidden", view !== "calendar");
    document.getElementById("studentListPanel").classList.toggle("hidden", view !== "list");
    document.getElementById("studentCalendarViewBtn").className = view === "calendar" ? "btn btn-success" : "btn btn-outline-success";
    document.getElementById("studentListViewBtn").className = view === "list" ? "btn btn-success" : "btn btn-outline-success";
}

function changeWeek(days) {
    const next = new Date(selectedWeekStart);
    next.setDate(next.getDate() + days);
    selectedWeekStart = startOfWeek(next);
    syncWeekPicker();
    renderStudentCalendar(getFilteredStudentReservations());
}

function syncWeekPicker() {
    document.getElementById("weekPicker").value = toDateInputValue(selectedWeekStart);
}

function renderStudentCalendar(reservas) {
    const calendar = document.getElementById("weeklyCalendar");
    const weekStart = startOfWeek(selectedWeekStart);
    const weekEnd = new Date(weekStart);
    weekEnd.setDate(weekEnd.getDate() + 7);

    const inWeek = reservas.filter((item) => {
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
                        ${entries.map(renderStudentSlot).join("")}
                    </div>
                </div>
            `);
        });
    });

    calendar.innerHTML = `<div class="weekly-calendar-grid">${cells.join("")}</div>`;
}

function renderStudentSlot(item) {
    return `
        <article class="weekly-slot">
            <div class="weekly-slot-title">${PeerlinkApp.escapeHtml(item.materiaNombre)}</div>
            <div class="weekly-slot-meta">${PeerlinkApp.escapeHtml(item.tutorNombre)}</div>
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
