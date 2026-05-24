const PeerlinkApp = (() => {
    const translations = {
        es: {
            brand: "PeerLink",
            navbar_logout: "Cerrar sesion",
            navbar_user_fallback: "Usuario",
            navbar_no_role: "SIN ROL",
            login_title: "PeerLink | Login",
            login_hero_title: "Inicia sesion en el sistema de tutorias",
            login_hero_desc: "Accede a tu panel como administrador, tutor o estudiante.",
            login_heading: "Login",
            label_email: "Correo",
            label_password: "Contrasena",
            action_login: "Iniciar sesion",
            login_no_account: "No tienes cuenta?",
            login_register_here: "Registrate aqui",
            console_waiting: "Esperando accion...",
            register_title: "PeerLink | Registro",
            register_heading: "Registro",
            register_subtitle: "Crea una cuenta como estudiante o tutor.",
            label_full_name: "Nombre completo",
            label_role: "Rol",
            action_register: "Registrarse",
            register_has_account: "Ya tienes cuenta?",
            register_back_login: "Volver al login",
            admin_title: "PeerLink | Panel Admin",
            admin_hero_title: "Gestion de Materias",
            admin_hero_desc: "Crea, edita, elimina materias y asigna tutores a cada una.",
            admin_subject_form: "Formulario de materia",
            admin_subject_name: "Nombre de la materia",
            admin_save_subject: "Agregar / Guardar",
            admin_cancel_edit: "Cancelar edicion",
            admin_assign_title: "Asignar tutor a materia",
            admin_assign_action: "Asignar",
            admin_subject_table: "Tabla de materias",
            admin_assignments_table: "Asignaciones tutor - materia",
            admin_users_title: "Usuarios",
            admin_save_user: "Agregar usuario",
            admin_open_swagger: "Abrir Swagger",
            admin_swagger_hint: "Documentacion protegida para administradores.",
            action_refresh: "Refrescar",
            action_open_swagger: "Abrir Swagger",
            action_apply_filters: "Aplicar filtros",
            action_clear_filters: "Limpiar filtros",
            action_prev_week: "Semana anterior",
            action_next_week: "Semana siguiente",
            action_this_week: "Esta semana",
            console_title: "Consola",
            student_title: "PeerLink | Panel Estudiante",
            student_hero_title: "Mis Reservas y Solicitudes",
            student_hero_desc: "Selecciona materia, tutor, idioma y facultad para programar tu tutoria.",
            student_new_reservation: "Nueva reserva",
            student_available_tutoring: "Tutores disponibles por materia",
            student_filters_title: "Filtros de tutorias",
            student_filters_desc: "Filtra tus tutorias por idioma, facultad y fecha/hora.",
            student_my_reservations: "Mis reservas",
            student_schedule_title: "Horario semanal",
            student_schedule_desc: "Vista semanal por horas de tus tutorias programadas.",
            tutor_title: "PeerLink | Panel Tutor",
            tutor_hero_title: "Gestion de Tutorias Solicitadas",
            tutor_hero_desc: "Confirma o cancela las solicitudes pendientes dirigidas a ti.",
            tutor_requests_title: "Solicitudes recibidas",
            tutor_filters_title: "Filtrar tutorias",
            tutor_filters_desc: "Encuentra rapido tus tutorias por idioma, facultad y fecha/hora.",
            tutor_schedule_title: "Mi horario semanal",
            tutor_schedule_desc: "Calendario semanal con tus tutorias asignadas.",
            label_subject: "Materia",
            label_tutor_available: "Tutor disponible",
            label_datetime: "Fecha y hora",
            label_language: "Idioma",
            label_faculty: "Facultad",
            label_date: "Fecha",
            label_time: "Hora",
            label_pick_week: "Semana a visualizar",
            action_request_tutoring: "Solicitar tutoria",
            table_id: "ID",
            table_name: "Nombre",
            table_actions: "Acciones",
            table_tutor: "Tutor",
            table_email: "Correo",
            table_subject: "Materia",
            table_student: "Estudiante",
            table_datetime: "Fecha y hora",
            table_status: "Estado",
            table_language: "Idioma",
            table_faculty: "Facultad",
            table_week_schedule: "Horario semanal",
            action_edit: "Editar",
            action_delete: "Eliminar",
            action_confirm: "Confirmar",
            action_cancel: "Cancelar",
            no_actions: "Sin acciones",
            empty_subjects: "No hay materias registradas.",
            empty_assignments: "No hay relaciones registradas.",
            empty_users: "No hay usuarios registrados.",
            empty_available_tutoring: "No hay tutores disponibles.",
            empty_my_reservations: "No tienes tutorias programadas con esos filtros.",
            empty_tutor_requests: "No tienes tutorias asignadas con esos filtros.",
            empty_week_schedule: "No hay tutorias en la semana seleccionada.",
            select_all_languages: "Todos los idiomas",
            select_all_faculties: "Todas las facultades",
            calendar_hour: "Hora",
            calendar_week_of: "Semana de {date}",
            reservation_with_tutor: "{subject} con {name}",
            reservation_with_student: "{subject} con {name}",
            confirm_delete_subject: "Eliminar materia?",
            confirm_delete_user: "Eliminar usuario {name}?",
            console_network_error: "No se pudo conectar con el backend.",
            console_network_hint: "Si usas Firefox con HTTPS local, confirma la excepcion del certificado para este sitio.",
            language_es: "Espanol",
            language_en: "Ingles",
            language_pt: "Portugues",
            faculty_CIENCIAS_EXACTAS_Y_NATURALES: "Facultad de Ciencias Exactas y Naturales",
            faculty_CIENCIAS_JURIDICAS_Y_POLITICAS: "Facultad de Ciencias Juridicas y Politicas",
            faculty_CIENCIAS_SOCIALES_Y_HUMANAS: "Facultad de Ciencias Sociales y Humanas",
            faculty_ECONOMIA_Y_ADMINISTRACION: "Facultad de Economia y Administracion",
            faculty_EDUCACION: "Facultad de Educacion",
            faculty_INGENIERIA: "Facultad de Ingenieria",
            faculty_SALUD: "Facultad de Salud"
        },
        en: {
            brand: "PeerLink",
            navbar_logout: "Log out",
            navbar_user_fallback: "User",
            navbar_no_role: "NO ROLE",
            login_title: "PeerLink | Login",
            login_hero_title: "Sign in to the tutoring system",
            login_hero_desc: "Access your dashboard as administrator, tutor, or student.",
            login_heading: "Login",
            label_email: "Email",
            label_password: "Password",
            action_login: "Sign in",
            login_no_account: "Don't have an account?",
            login_register_here: "Register here",
            console_waiting: "Waiting for action...",
            register_title: "PeerLink | Register",
            register_heading: "Register",
            register_subtitle: "Create an account as a student or tutor.",
            label_full_name: "Full name",
            label_role: "Role",
            action_register: "Register",
            register_has_account: "Already have an account?",
            register_back_login: "Back to login",
            admin_title: "PeerLink | Admin Panel",
            admin_hero_title: "Subject Management",
            admin_hero_desc: "Create, edit, delete subjects, and assign tutors to each one.",
            admin_subject_form: "Subject form",
            admin_subject_name: "Subject name",
            admin_save_subject: "Add / Save",
            admin_cancel_edit: "Cancel edit",
            admin_assign_title: "Assign tutor to subject",
            admin_assign_action: "Assign",
            admin_subject_table: "Subjects table",
            admin_assignments_table: "Tutor - subject assignments",
            admin_users_title: "Users",
            admin_save_user: "Add user",
            admin_open_swagger: "Open Swagger",
            admin_swagger_hint: "Protected documentation for administrators.",
            action_refresh: "Refresh",
            action_open_swagger: "Open Swagger",
            action_apply_filters: "Apply filters",
            action_clear_filters: "Clear filters",
            action_prev_week: "Previous week",
            action_next_week: "Next week",
            action_this_week: "This week",
            console_title: "Console",
            student_title: "PeerLink | Student Panel",
            student_hero_title: "My Reservations and Requests",
            student_hero_desc: "Select subject, tutor, language, and faculty to schedule your tutoring session.",
            student_new_reservation: "New reservation",
            student_available_tutoring: "Available tutors by subject",
            student_filters_title: "Tutoring filters",
            student_filters_desc: "Filter your tutoring sessions by language, faculty, and date/time.",
            student_my_reservations: "My reservations",
            student_schedule_title: "Weekly schedule",
            student_schedule_desc: "Weekly hourly view of your scheduled tutoring sessions.",
            tutor_title: "PeerLink | Tutor Panel",
            tutor_hero_title: "Requested Tutoring Management",
            tutor_hero_desc: "Confirm or cancel pending requests assigned to you.",
            tutor_requests_title: "Received requests",
            tutor_filters_title: "Filter tutoring sessions",
            tutor_filters_desc: "Quickly find your tutoring sessions by language, faculty, and date/time.",
            tutor_schedule_title: "My weekly schedule",
            tutor_schedule_desc: "Weekly calendar with your assigned tutoring sessions.",
            label_subject: "Subject",
            label_tutor_available: "Available tutor",
            label_datetime: "Date and time",
            label_language: "Language",
            label_faculty: "Faculty",
            label_date: "Date",
            label_time: "Time",
            label_pick_week: "Week to view",
            action_request_tutoring: "Request tutoring",
            table_id: "ID",
            table_name: "Name",
            table_actions: "Actions",
            table_tutor: "Tutor",
            table_email: "Email",
            table_subject: "Subject",
            table_student: "Student",
            table_datetime: "Date and time",
            table_status: "Status",
            table_language: "Language",
            table_faculty: "Faculty",
            table_week_schedule: "Weekly schedule",
            action_edit: "Edit",
            action_delete: "Delete",
            action_confirm: "Confirm",
            action_cancel: "Cancel",
            no_actions: "No actions",
            empty_subjects: "There are no registered subjects.",
            empty_assignments: "There are no registered relationships.",
            empty_users: "There are no registered users.",
            empty_available_tutoring: "There are no available tutors.",
            empty_my_reservations: "You have no tutoring sessions for these filters.",
            empty_tutor_requests: "You have no tutoring sessions for these filters.",
            empty_week_schedule: "There are no tutoring sessions in the selected week.",
            select_all_languages: "All languages",
            select_all_faculties: "All faculties",
            calendar_hour: "Hour",
            calendar_week_of: "Week of {date}",
            reservation_with_tutor: "{subject} with {name}",
            reservation_with_student: "{subject} with {name}",
            confirm_delete_subject: "Delete subject?",
            confirm_delete_user: "Delete user {name}?",
            console_network_error: "Could not connect to the backend.",
            console_network_hint: "If you are using Firefox with local HTTPS, make sure the certificate exception was saved for this site.",
            language_es: "Spanish",
            language_en: "English",
            language_pt: "Portuguese",
            faculty_CIENCIAS_EXACTAS_Y_NATURALES: "Faculty of Exact and Natural Sciences",
            faculty_CIENCIAS_JURIDICAS_Y_POLITICAS: "Faculty of Legal and Political Sciences",
            faculty_CIENCIAS_SOCIALES_Y_HUMANAS: "Faculty of Social and Human Sciences",
            faculty_ECONOMIA_Y_ADMINISTRACION: "Faculty of Economics and Administration",
            faculty_EDUCACION: "Faculty of Education",
            faculty_INGENIERIA: "Faculty of Engineering",
            faculty_SALUD: "Faculty of Health"
        },
        pt: {
            brand: "PeerLink",
            navbar_logout: "Encerrar sessao",
            navbar_user_fallback: "Usuario",
            navbar_no_role: "SEM PAPEL",
            login_title: "PeerLink | Login",
            login_hero_title: "Entre no sistema de tutorias",
            login_hero_desc: "Acesse seu painel como administrador, tutor ou estudante.",
            login_heading: "Login",
            label_email: "E-mail",
            label_password: "Senha",
            action_login: "Entrar",
            login_no_account: "Nao tem conta?",
            login_register_here: "Cadastre-se aqui",
            console_waiting: "Aguardando acao...",
            register_title: "PeerLink | Registro",
            register_heading: "Registro",
            register_subtitle: "Crie uma conta como estudante ou tutor.",
            label_full_name: "Nome completo",
            label_role: "Papel",
            action_register: "Registrar",
            register_has_account: "Ja tem conta?",
            register_back_login: "Voltar ao login",
            admin_title: "PeerLink | Painel Admin",
            admin_hero_title: "Gestao de Materias",
            admin_hero_desc: "Crie, edite, exclua materias e atribua tutores a cada uma.",
            admin_subject_form: "Formulario da materia",
            admin_subject_name: "Nome da materia",
            admin_save_subject: "Adicionar / Salvar",
            admin_cancel_edit: "Cancelar edicao",
            admin_assign_title: "Atribuir tutor a materia",
            admin_assign_action: "Atribuir",
            admin_subject_table: "Tabela de materias",
            admin_assignments_table: "Atribuicoes tutor - materia",
            admin_users_title: "Usuarios",
            admin_save_user: "Adicionar usuario",
            admin_open_swagger: "Abrir Swagger",
            admin_swagger_hint: "Documentacao protegida para administradores.",
            action_refresh: "Atualizar",
            action_open_swagger: "Abrir Swagger",
            action_apply_filters: "Aplicar filtros",
            action_clear_filters: "Limpar filtros",
            action_prev_week: "Semana anterior",
            action_next_week: "Proxima semana",
            action_this_week: "Esta semana",
            console_title: "Console",
            student_title: "PeerLink | Painel Estudante",
            student_hero_title: "Minhas Reservas e Solicitacoes",
            student_hero_desc: "Selecione materia, tutor, idioma e faculdade para agendar sua tutoria.",
            student_new_reservation: "Nova reserva",
            student_available_tutoring: "Tutores disponiveis por materia",
            student_filters_title: "Filtros de tutorias",
            student_filters_desc: "Filtre suas tutorias por idioma, faculdade e data/hora.",
            student_my_reservations: "Minhas reservas",
            student_schedule_title: "Horario semanal",
            student_schedule_desc: "Visao semanal por horas das suas tutorias agendadas.",
            tutor_title: "PeerLink | Painel Tutor",
            tutor_hero_title: "Gestao de Tutorias Solicitadas",
            tutor_hero_desc: "Confirme ou cancele as solicitacoes pendentes atribuidas a voce.",
            tutor_requests_title: "Solicitacoes recebidas",
            tutor_filters_title: "Filtrar tutorias",
            tutor_filters_desc: "Encontre rapidamente suas tutorias por idioma, faculdade e data/hora.",
            tutor_schedule_title: "Meu horario semanal",
            tutor_schedule_desc: "Calendario semanal com suas tutorias atribuidas.",
            label_subject: "Materia",
            label_tutor_available: "Tutor disponivel",
            label_datetime: "Data e hora",
            label_language: "Idioma",
            label_faculty: "Faculdade",
            label_date: "Data",
            label_time: "Hora",
            label_pick_week: "Semana para visualizar",
            action_request_tutoring: "Solicitar tutoria",
            table_id: "ID",
            table_name: "Nome",
            table_actions: "Acoes",
            table_tutor: "Tutor",
            table_email: "E-mail",
            table_subject: "Materia",
            table_student: "Estudante",
            table_datetime: "Data e hora",
            table_status: "Estado",
            table_language: "Idioma",
            table_faculty: "Faculdade",
            table_week_schedule: "Horario semanal",
            action_edit: "Editar",
            action_delete: "Excluir",
            action_confirm: "Confirmar",
            action_cancel: "Cancelar",
            no_actions: "Sem acoes",
            empty_subjects: "Nao ha materias cadastradas.",
            empty_assignments: "Nao ha relacoes cadastradas.",
            empty_users: "Nao ha usuarios cadastrados.",
            empty_available_tutoring: "Nao ha tutores disponiveis.",
            empty_my_reservations: "Voce nao tem tutorias para esses filtros.",
            empty_tutor_requests: "Voce nao tem tutorias para esses filtros.",
            empty_week_schedule: "Nao ha tutorias na semana selecionada.",
            select_all_languages: "Todos os idiomas",
            select_all_faculties: "Todas as faculdades",
            calendar_hour: "Hora",
            calendar_week_of: "Semana de {date}",
            reservation_with_tutor: "{subject} com {name}",
            reservation_with_student: "{subject} com {name}",
            confirm_delete_subject: "Excluir materia?",
            confirm_delete_user: "Excluir usuario {name}?",
            console_network_error: "Nao foi possivel conectar ao backend.",
            console_network_hint: "Se voce usa Firefox com HTTPS local, confirme a excecao do certificado para este site.",
            language_es: "Espanhol",
            language_en: "Ingles",
            language_pt: "Portugues",
            faculty_CIENCIAS_EXACTAS_Y_NATURALES: "Faculdade de Ciencias Exatas e Naturais",
            faculty_CIENCIAS_JURIDICAS_Y_POLITICAS: "Faculdade de Ciencias Juridicas e Politicas",
            faculty_CIENCIAS_SOCIALES_Y_HUMANAS: "Faculdade de Ciencias Sociais e Humanas",
            faculty_ECONOMIA_Y_ADMINISTRACION: "Faculdade de Economia e Administracao",
            faculty_EDUCACION: "Faculdade de Educacao",
            faculty_INGENIERIA: "Faculdade de Engenharia",
            faculty_SALUD: "Faculdade de Saude"
        }
    };

    const storageKeys = {
        token: "peerlink.token",
        role: "peerlink.rol",
        email: "peerlink.correo",
        name: "peerlink.nombreCompleto",
        lang: "peerlink.lang"
    };

    const tutoringLanguages = ["es", "en", "pt"];
    const faculties = [
        "CIENCIAS_EXACTAS_Y_NATURALES",
        "CIENCIAS_JURIDICAS_Y_POLITICAS",
        "CIENCIAS_SOCIALES_Y_HUMANAS",
        "ECONOMIA_Y_ADMINISTRACION",
        "EDUCACION",
        "INGENIERIA",
        "SALUD"
    ];

    function getLang() {
        return new URLSearchParams(window.location.search).get("lang")
            || localStorage.getItem(storageKeys.lang)
            || "es";
    }

    function getLocale() {
        return getLang() === "pt" ? "pt-BR" : getLang() === "en" ? "en-US" : "es-CO";
    }

    function t(key, params = {}) {
        const dict = translations[getLang()] || translations.es;
        const base = dict[key] || translations.es[key] || key;
        return Object.entries(params).reduce(
            (text, [param, value]) => text.replaceAll(`{${param}}`, value),
            base
        );
    }

    function setLang(lang) {
        localStorage.setItem(storageKeys.lang, lang);
        const url = new URL(window.location.href);
        url.searchParams.set("lang", lang);
        window.location.href = url.toString();
    }

    function getAuth() {
        return {
            token: localStorage.getItem(storageKeys.token) || "",
            role: localStorage.getItem(storageKeys.role) || "",
            email: localStorage.getItem(storageKeys.email) || "",
            name: localStorage.getItem(storageKeys.name) || ""
        };
    }

    function saveAuth(payload) {
        localStorage.setItem(storageKeys.token, payload.token);
        localStorage.setItem(storageKeys.role, payload.rol);
        localStorage.setItem(storageKeys.email, payload.correo);
        localStorage.setItem(storageKeys.name, payload.nombreCompleto);
        document.cookie = `AUTH_TOKEN=${payload.token}; path=/; Secure; SameSite=Lax`;
    }

    function clearAuth() {
        localStorage.removeItem(storageKeys.token);
        localStorage.removeItem(storageKeys.role);
        localStorage.removeItem(storageKeys.email);
        localStorage.removeItem(storageKeys.name);
        document.cookie = "AUTH_TOKEN=; path=/; Max-Age=0; Secure; SameSite=Lax";
    }

    function panelForRole(role) {
        switch (role) {
            case "ADMIN":
                return "/panel-admin.html";
            case "ESTUDIANTE":
                return "/panel-estudiante.html";
            case "TUTOR":
                return "/panel-tutor.html";
            default:
                return "/login.html";
        }
    }

    function redirectToPanel(role) {
        window.location.href = withLang(panelForRole(role));
    }

    function withLang(path) {
        const url = new URL(path, window.location.origin);
        url.searchParams.set("lang", getLang());
        return url.pathname + url.search;
    }

    function ensureRole(role) {
        const auth = getAuth();
        if (!auth.token) {
            window.location.href = withLang("/login.html");
            return null;
        }
        if (role && auth.role !== role) {
            redirectToPanel(auth.role);
            return null;
        }
        return auth;
    }

    function renderNavbar(containerId) {
        const auth = getAuth();
        const container = document.getElementById(containerId);
        if (!container) {
            return;
        }

        container.innerHTML = `
            <nav class="navbar navbar-expand-lg border-bottom border-light-subtle bg-white bg-opacity-75 backdrop-blur sticky-top">
                <div class="container-xxl">
                    <a class="navbar-brand fw-bold text-success-emphasis" href="${withLang(panelForRole(auth.role || "ADMIN"))}">${t("brand")}</a>
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <div class="btn-group btn-group-sm" role="group" aria-label="Language selector">
                            <button class="btn btn-outline-success lang-btn" data-lang="es" type="button">ES</button>
                            <button class="btn btn-outline-success lang-btn" data-lang="en" type="button">EN</button>
                            <button class="btn btn-outline-success lang-btn" data-lang="pt" type="button">PT</button>
                        </div>
                        <span class="badge rounded-pill text-bg-light border">${escapeHtml(auth.name || auth.email || t("navbar_user_fallback"))} · ${escapeHtml(auth.role || t("navbar_no_role"))}</span>
                        <button id="logoutBtn" class="btn btn-outline-dark btn-sm" type="button">${t("navbar_logout")}</button>
                    </div>
                </div>
            </nav>
        `;

        container.querySelectorAll(".lang-btn").forEach((button) => {
            button.classList.toggle("active", button.dataset.lang === getLang());
            button.addEventListener("click", () => setLang(button.dataset.lang));
        });

        const logoutBtn = document.getElementById("logoutBtn");
        if (logoutBtn) {
            logoutBtn.addEventListener("click", () => {
                clearAuth();
                window.location.href = withLang("/login.html");
            });
        }
    }

    function applyI18n(root = document) {
        document.documentElement.lang = getLang();
        const titleKey = document.body?.dataset?.i18nTitle;
        if (titleKey) {
            document.title = t(titleKey);
        }
        root.querySelectorAll("[data-i18n]").forEach((node) => {
            node.textContent = t(node.dataset.i18n);
        });
        root.querySelectorAll("[data-i18n-html]").forEach((node) => {
            node.innerHTML = t(node.dataset.i18nHtml);
        });
        root.querySelectorAll("[data-i18n-placeholder]").forEach((node) => {
            node.placeholder = t(node.dataset.i18nPlaceholder);
        });
    }

    async function api(path, options = {}, requiresAuth = true) {
        const auth = getAuth();
        const headers = {
            "Content-Type": "application/json",
            ...(options.headers || {})
        };

        if (requiresAuth && auth.token) {
            headers.Authorization = `Bearer ${auth.token}`;
        }

        const response = await fetch(withLang(path), {
            ...options,
            headers
        });

        const raw = await response.text();
        const data = raw ? safeJsonParse(raw) : null;

        if (!response.ok) {
            if (response.status === 401) {
                clearAuth();
            }
            throw data || { status: response.status, message: response.statusText };
        }

        return data;
    }

    function bindConsole(elementId) {
        return {
            print(data) {
                const node = document.getElementById(elementId);
                if (node) {
                    node.textContent = JSON.stringify(data, null, 2);
                }
            },
            printError(error) {
                const node = document.getElementById(elementId);
                if (node) {
                    node.textContent = JSON.stringify({ error: true, ...error }, null, 2);
                }
            }
        };
    }

    function safeJsonParse(value) {
        try {
            return JSON.parse(value);
        } catch {
            return { raw: value };
        }
    }

    function formatDateTime(value) {
        return new Date(value).toLocaleString(getLocale());
    }

    function formatDate(value, options = {}) {
        return new Date(value).toLocaleDateString(getLocale(), options);
    }

    function formatTime(value) {
        return new Date(value).toLocaleTimeString(getLocale(), {
            hour: "2-digit",
            minute: "2-digit"
        });
    }

    function labelForLanguage(code) {
        return t(`language_${code}`);
    }

    function labelForFaculty(code) {
        return t(`faculty_${code}`);
    }

    function getTutoringLanguages() {
        return [...tutoringLanguages];
    }

    function getFaculties() {
        return [...faculties];
    }

    function escapeHtml(value) {
        return String(value ?? "")
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll("\"", "&quot;")
            .replaceAll("'", "&#39;");
    }

    return {
        api,
        applyI18n,
        bindConsole,
        clearAuth,
        ensureRole,
        escapeHtml,
        formatDate,
        formatDateTime,
        formatTime,
        getAuth,
        getFaculties,
        getLang,
        getLocale,
        getTutoringLanguages,
        labelForFaculty,
        labelForLanguage,
        panelForRole,
        redirectToPanel,
        renderNavbar,
        saveAuth,
        setLang,
        t,
        withLang
    };
})();
