const tutorAuth = PeerlinkApp.ensureRole("TUTOR");
const tutorConsole = PeerlinkApp.bindConsole("consoleOutput");
const tutorHours = Array.from({ length: 16 }, (_, index) => index + 6);

let tutorReservations = [];
let tutorWeekStart = startOfWeek(new Date());

if (tutorAuth) {
    PeerlinkApp.renderNavbar("navbarContainer");
    PeerlinkApp.applyI18n();
    bootTutor();
}

function bootTutor() {
    fillLanguageSelect("filtroIdioma", true);
    fillFacultySelect("filtroFacultad", true);

    document.getElementById("filtrosForm").addEventListener("submit", handleTutorFiltersSubmit);
    document.getElementById("clearFiltersBtn").addEventListener("click", clearTutorFilters);
    document.getElementById("refreshReservasBtn").addEventListener("click", loadTutorias);
    document.getElementById("prevWeekBtn").addEventListener("click", () => changeTutorWeek(-7));
    document.getElementById("nextWeekBtn").addEventListener("click", () => changeTutorWeek(7));
    document.getElementById("thisWeekBtn").addEventListener("click", () => {
        tutorWeekStart = startOfWeek(new Date());
        syncTutorWeekPicker();
        renderTutorCalendar(tutorReservations);
    });
    document.getElementById("weekPicker").addEventListener("change", (event) => {
        tutorWeekStart = startOfWeek(event.target.value ? new Date(`${event.target.value}T00:00:00`) : new Date());
        syncTutorWeekPicker();
        renderTutorCalendar(tutorReservations);
    });

    syncTutorWeekPicker();
    loadTutorias();
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

function handleTutorFiltersSubmit(event) {
    event.preventDefault();
    loadTutorias();
}

function clearTutorFilters() {
    document.getElementById("filtrosForm").reset();
    loadTutorias();
}

async function loadTutorias() {
    try {
        const reservas = await PeerlinkApp.api(`/api/reservas/mis-tutorias${buildTutorFilterQuery()}`);
        tutorReservations = applyTutorHourFilter(reservas);
        renderTutoriasTable(tutorReservations);
        renderTutorCalendar(tutorReservations);
    } catch (error) {
        tutorConsole.printError(error);
    }
}

function buildTutorFilterQuery() {
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

function applyTutorHourFilter(reservas) {
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

function renderTutoriasTable(reservas) {
    const tbody = document.getElementById("reservasTableBody");
    if (!reservas.length) {
        tbody.innerHTML = `<tr><td colspan="7">${PeerlinkApp.t("empty_tutor_requests")}</td></tr>`;
        return;
    }

    tbody.innerHTML = reservas.map((item) => `
        <tr>
            <td>${PeerlinkApp.escapeHtml(item.estudianteNombre)}</td>
            <td>${PeerlinkApp.escapeHtml(item.materiaNombre)}</td>
            <td>${PeerlinkApp.formatDateTime(item.fechaHora)}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForLanguage(item.idioma))}</td>
            <td>${PeerlinkApp.escapeHtml(PeerlinkApp.labelForFaculty(item.facultad))}</td>
            <td><span class="badge text-bg-light border">${PeerlinkApp.escapeHtml(item.estado)}</span></td>
            <td>
                ${item.estado === "PENDIENTE" ? `
                    <div class="table-actions">
                        <button class="btn btn-sm btn-success" data-action="CONFIRMADA" data-id="${item.id}" type="button">${PeerlinkApp.t("action_confirm")}</button>
                        <button class="btn btn-sm btn-outline-danger" data-action="CANCELADA" data-id="${item.id}" type="button">${PeerlinkApp.t("action_cancel")}</button>
                    </div>
                ` : `<span class="text-secondary">${PeerlinkApp.t("no_actions")}</span>`}
            </td>
        </tr>
    `).join("");

    tbody.querySelectorAll("[data-action]").forEach((button) => {
        button.addEventListener("click", () => updateEstado(button.dataset.id, button.dataset.action));
    });
}

async function updateEstado(id, estado) {
    try {
        const response = await PeerlinkApp.api(`/api/reservas/${id}/estado`, {
            method: "PATCH",
            body: JSON.stringify({ estado })
        });
        tutorConsole.print(response);
        await loadTutorias();
    } catch (error) {
        tutorConsole.printError(error);
    }
}

function changeTutorWeek(days) {
    const next = new Date(tutorWeekStart);
    next.setDate(next.getDate() + days);
    tutorWeekStart = startOfWeek(next);
    syncTutorWeekPicker();
    renderTutorCalendar(tutorReservations);
}

function syncTutorWeekPicker() {
    document.getElementById("weekPicker").value = toDateInputValue(tutorWeekStart);
}

function renderTutorCalendar(reservas) {
    const calendar = document.getElementById("weeklyCalendar");
    const weekStart = startOfWeek(tutorWeekStart);
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

    tutorHours.forEach((hour) => {
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
            <div class="weekly-slot-title">${PeerlinkApp.escapeHtml(PeerlinkApp.t("reservation_with_student", {
                subject: item.materiaNombre,
                name: item.estudianteNombre
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
