const studentAuth = PeerlinkApp.ensureRole("ESTUDIANTE");
const studentConsole = PeerlinkApp.bindConsole("consoleOutput");
const studentHours = Array.from({ length: 16 }, (_, index) => index + 6);

let tutorMateriaRelations = [];
let studentReservations = [];
let selectedWeekStart = startOfWeek(new Date());

if (studentAuth) {
    PeerlinkApp.renderNavbar("navbarContainer");
    PeerlinkApp.applyI18n();
    bootStudent();
}

async function bootStudent() {
    fillStaticSelects();
    setInitialDateTime();

    document.getElementById("reservaForm").addEventListener("submit", createReserva);
    document.getElementById("filtrosForm").addEventListener("submit", handleFiltersSubmit);
    document.getElementById("clearFiltersBtn").addEventListener("click", clearFilters);
    document.getElementById("refreshTutorMateriasBtn").addEventListener("click", loadTutorMaterias);
    document.getElementById("refreshReservasBtn").addEventListener("click", loadMisReservas);
    document.getElementById("reservaMateriaId").addEventListener("change", fillTutorOptions);
    document.getElementById("reservaTutorId").addEventListener("change", syncSelectedOfferPreview);
    document.getElementById("prevWeekBtn").addEventListener("click", () => changeWeek(-7));
    document.getElementById("nextWeekBtn").addEventListener("click", () => changeWeek(7));
    document.getElementById("thisWeekBtn").addEventListener("click", () => {
        selectedWeekStart = startOfWeek(new Date());
        syncWeekPicker();
        renderWeeklyCalendar(studentReservations);
    });
    document.getElementById("weekPicker").addEventListener("change", (event) => {
        selectedWeekStart = startOfWeek(event.target.value ? new Date(`${event.target.value}T00:00:00`) : new Date());
        syncWeekPicker();
        renderWeeklyCalendar(studentReservations);
    });

    syncWeekPicker();
    await Promise.all([loadTutorMaterias(), loadMisReservas()]);
}

function fillStaticSelects() {
    fillLanguageSelect("filtroIdioma", true);
    fillFacultySelect("filtroFacultad", true);
}

function setInitialDateTime() {
    const input = document.getElementById("reservaFechaHora");
    const minDate = new Date();
    minDate.setMinutes(minDate.getMinutes() + 5);
    const minValue = toDateTimeLocalValue(minDate);
    input.min = minValue;
    input.value = minValue;
}

function fillLanguageSelect(elementId, includeAllOption) {
    const select = document.getElementById(elementId);
    const options = [];

    if (includeAllOption) {
        options.push(`<option value="">${PeerlinkApp.escapeHtml(PeerlinkApp.t("select_all_languages"))}</option>`);
    }

    PeerlinkApp.getTutoringLanguages().forEach((code) => {
        options.push(`<option value="${code}">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(code))}</option>`);
    });

    select.innerHTML = options.join("");
}

function fillFacultySelect(elementId, includeAllOption) {
    const select = document.getElementById(elementId);
    const options = [];

    if (includeAllOption) {
        options.push(`<option value="">${PeerlinkApp.escapeHtml(PeerlinkApp.t("select_all_faculties"))}</option>`);
    }

    PeerlinkApp.getFaculties().forEach((code) => {
        options.push(`<option value="${code}">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(code))}</option>`);
    });

    select.innerHTML = options.join("");
}

async function loadTutorMaterias() {
    try {
        tutorMateriaRelations = await PeerlinkApp.api("/api/materias/tutores-materias");
        fillMateriaOptions();
        fillTutorOptions();
        renderTutorMateriaTable();
        syncSelectedOfferPreview();
    } catch (error) {
        studentConsole.printError(error);
    }
}

function renderTutorMateriaTable() {
    const tbody = document.getElementById("tutorMateriasTableBody");
    const filteredRelations = getFilteredTutorMateriaRelations();
    if (!filteredRelations.length) {
        tbody.innerHTML = `<tr><td colspan="5">${PeerlinkApp.t("empty_available_tutoring")}</td></tr>`;
        return;
    }

    tbody.innerHTML = filteredRelations.map((item) => `
        <tr>
            <td>${PeerlinkApp.escapeHtml(item.materiaNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(item.tutorNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(item.tutorCorreo)}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(item.facultad))}</td>
        </tr>
    `).join("");
}

function fillMateriaOptions() {
    const select = document.getElementById("reservaMateriaId");
    const unique = [...new Map(getFilteredTutorMateriaRelations().map((item) => [item.materiaId, item])).values()];
    select.innerHTML = unique.map((item) => `
        <option value="${item.materiaId}">${PeerlinkApp.escapeHtml(item.materiaNombre)}</option>
    `).join("");
}

function fillTutorOptions() {
    const materiaId = Number(document.getElementById("reservaMateriaId").value);
    const tutorSelect = document.getElementById("reservaTutorId");
    const filtered = getFilteredTutorMateriaRelations().filter((item) => item.materiaId === materiaId);
    tutorSelect.innerHTML = filtered.map((item) => `
        <option value="${item.tutorId}">${PeerlinkApp.escapeHtml(item.tutorNombre)}</option>
    `).join("");
    syncSelectedOfferPreview();
}

async function createReserva(event) {
    event.preventDefault();

    const payload = {
        tutorId: Number(document.getElementById("reservaTutorId").value),
        materiaId: Number(document.getElementById("reservaMateriaId").value),
        fechaHora: document.getElementById("reservaFechaHora").value
    };

    try {
        const response = await PeerlinkApp.api("/api/reservas", {
            method: "POST",
            body: JSON.stringify(payload)
        });
        studentConsole.print(response);
        setInitialDateTime();
        await loadMisReservas();
    } catch (error) {
        studentConsole.printError(error);
    }
}

function handleFiltersSubmit(event) {
    event.preventDefault();
    fillMateriaOptions();
    fillTutorOptions();
    renderTutorMateriaTable();
    loadMisReservas();
}

function clearFilters() {
    document.getElementById("filtrosForm").reset();
    fillMateriaOptions();
    fillTutorOptions();
    renderTutorMateriaTable();
    loadMisReservas();
}

function getFilteredTutorMateriaRelations() {
    const idioma = document.getElementById("filtroIdioma").value;
    const facultad = document.getElementById("filtroFacultad").value;

    return tutorMateriaRelations.filter((item) => (!idioma || item.idioma === idioma) && (!facultad || item.facultad === facultad));
}

function syncSelectedOfferPreview() {
    const materiaId = Number(document.getElementById("reservaMateriaId").value);
    const tutorId = Number(document.getElementById("reservaTutorId").value);
    const selected = tutorMateriaRelations.find((item) => item.materiaId === materiaId && item.tutorId === tutorId);

    document.getElementById("reservaIdiomaPreview").value = selected
        ? PeerlinkApp.labelForLanguage(selected.idioma)
        : "";
    document.getElementById("reservaFacultadPreview").value = selected
        ? PeerlinkApp.labelForFaculty(selected.facultad)
        : "";
}

async function loadMisReservas() {
    try {
        const reservas = await PeerlinkApp.api(`/api/reservas/mis-reservas${buildReservationFilterQuery()}`);
        studentReservations = applyClientSideHourFilter(reservas);
        renderReservasTable(studentReservations);
        renderWeeklyCalendar(studentReservations);
    } catch (error) {
        studentConsole.printError(error);
    }
}

function buildReservationFilterQuery() {
    const params = new URLSearchParams();
    const idioma = document.getElementById("filtroIdioma").value;
    const facultad = document.getElementById("filtroFacultad").value;
    const fecha = document.getElementById("filtroFecha").value;
    const hora = document.getElementById("filtroHora").value;

    if (idioma) {
        params.set("idioma", idioma);
    }
    if (facultad) {
        params.set("facultad", facultad);
    }

    if (fecha && hora) {
        const start = new Date(`${fecha}T${hora}:00`);
        const end = new Date(start);
        end.setHours(end.getHours() + 1);
        params.set("desde", toApiDateTime(start));
        params.set("hasta", toApiDateTime(end));
    } else if (fecha) {
        const start = new Date(`${fecha}T00:00:00`);
        const end = new Date(start);
        end.setDate(end.getDate() + 1);
        params.set("desde", toApiDateTime(start));
        params.set("hasta", toApiDateTime(end));
    }

    const query = params.toString();
    return query ? `?${query}` : "";
}

function applyClientSideHourFilter(reservas) {
    const hourValue = document.getElementById("filtroHora").value;
    if (!hourValue) {
        return reservas;
    }

    return reservas.filter((item) => {
        const date = new Date(item.fechaHora);
        const localHour = `${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
        return localHour.startsWith(hourValue.slice(0, 2));
    });
}

function renderReservasTable(reservas) {
    const tbody = document.getElementById("reservasTableBody");
    if (!reservas.length) {
        tbody.innerHTML = `<tr><td colspan="6">${PeerlinkApp.t("empty_my_reservations")}</td></tr>`;
        return;
    }

    tbody.innerHTML = reservas.map((item) => `
        <tr>
            <td>${PeerlinkApp.escapeHtml(item.materiaNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(item.tutorNombre)}</td>
            <td>${PeerlinkApp.formatDateTime(item.fechaHora)}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(item.facultad))}</td>
            <td><span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(item.estado)}</span></td>
        </tr>
    `).join("");
}

function changeWeek(days) {
    const next = new Date(selectedWeekStart);
    next.setDate(next.getDate() + days);
    selectedWeekStart = startOfWeek(next);
    syncWeekPicker();
    renderWeeklyCalendar(studentReservations);
}

function syncWeekPicker() {
    document.getElementById("weekPicker").value = toDateInputValue(selectedWeekStart);
}

function renderWeeklyCalendar(reservas) {
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

    const cells = [`<div class="weekly-calendar-cell header">${PeerlinkApp.t("calendar_hour")}</div>`];
    dayStarts.forEach((date) => {
        cells.push(`
            <div class="weekly-calendar-cell header">
                <div>${date.toLocaleDateString(PeerlinkApp.getLocale(), { weekday: "short" })}</div>
                <div class="small text-secondary">${PeerlinkApp.formatDate(date, { day: "2-digit", month: "2-digit" })}</div>
            </div>
        `);
    });

    studentHours.forEach((hour) => {
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
            <div class="weekly-slot-title">${PeerlinkApp.escapeHtml(PeerlinkApp.t("reservation_with_tutor", {
                subject: item.materiaNombre,
                name: item.tutorNombre
            }))}</div>
            <div class="weekly-slot-meta">${PeerlinkApp.escapeHtml(PeerlinkApp.formatTime(item.fechaHora))}</div>
            <div class="weekly-slot-meta">${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</div>
            <div class="weekly-slot-meta">${PeerlinkApp.escapeHtml(item.estado)}</div>
        </article>
    `;
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

function toApiDateTime(date) {
    return `${toDateTimeLocalValue(date)}:00`;
}
