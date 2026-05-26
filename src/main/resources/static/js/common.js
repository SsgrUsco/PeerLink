const PeerlinkApp = (() => {
    const translations = {
        es: {
            brand: "PeerLink",
            navbar_logout: "Cerrar sesion",
            navbar_profile: "Perfil",
            navbar_help: "Ayuda",
            navbar_user_fallback: "Usuario",
            navbar_no_role: "SIN ROL",
            login_title: "PeerLink | Login",
            landing_title: "PeerLink | Plataforma de tutorias",
            landing_badge: "Tutoria universitaria organizada",
            landing_heading: "Conecta estudiantes y tutores en un solo flujo claro.",
            landing_desc: "PeerLink centraliza materias, reservas, solicitudes y horarios semanales para que la experiencia academica sea simple, visible y ordenada.",
            landing_primary_cta: "Iniciar sesion",
            landing_secondary_cta: "Crear cuenta",
            landing_panel_cta: "Ir a mi panel",
            landing_section_roles: "Una experiencia para cada rol",
            landing_section_roles_desc: "Cada panel prioriza lo importante segun el trabajo real de estudiantes, tutores y administradores.",
            landing_role_student: "Estudiantes",
            landing_role_student_desc: "Buscan tutorias, filtran coincidencias, solicitan apoyo y consultan su horario en calendario o lista.",
            landing_role_tutor: "Tutores",
            landing_role_tutor_desc: "Publican tutorias, gestionan solicitudes recibidas y mantienen visible su agenda semanal.",
            landing_role_admin: "Administracion",
            landing_role_admin_desc: "Controla materias, asignaciones, usuarios y documentacion tecnica protegida desde un solo lugar.",
            landing_section_features: "Lo esencial, sin desorden",
            landing_feature_filters: "Filtros por texto, idioma, facultad, fecha y hora para llegar rapido a la coincidencia correcta.",
            landing_feature_calendar: "Horarios semanales por horas para ver claramente reservas, tutorias y disponibilidad.",
            landing_feature_i18n: "Interfaz preparada para espanol, ingles y portugues con claves consistentes.",
            landing_footer: "Construido para coordinar tutorias con claridad, trazabilidad y buena experiencia de uso.",
            landing_nav_features: "Beneficios",
            landing_nav_how: "Como funciona",
            landing_nav_testimonials: "Escenarios",
            landing_nav_pricing: "Plan",
            landing_nav_faq: "FAQ",
            landing_cta_primary: "Comenzar gratis",
            landing_cta_secondary: "Saber mas",
            landing_support_text: "Pensado para estudiantes que necesitan claridad inmediata y tutores que quieren organizar mejor su tiempo.",
            landing_support_badge: "Disponible en espanol, ingles y portugues",
            landing_mock_calendar_label: "Calendario",
            landing_mock_calendar: "Consulta tutorias publicadas, solicitudes y reservas desde un calendario semanal por horas.",
            landing_mock_agenda_title: "Tu agenda, bajo control",
            landing_mock_example: "Calculo I - 10:00 AM",
            landing_mock_filters_label: "Filtros",
            landing_mock_filters: "Filtra por idioma, facultad y fecha para llegar mas rapido a la coincidencia correcta.",
            landing_mock_roles_label: "Roles",
            landing_mock_roles: "Paneles separados para estudiante, tutor y administrador con tareas claras en cada rol.",
            landing_mock_security_label: "Seguridad",
            landing_mock_security: "Acceso protegido por rol y control administrativo para mantener orden y trazabilidad.",
            landing_trust_label_1: "Claridad",
            landing_trust_value_1: "Flujo visible",
            landing_trust_text_1: "Cada reserva, solicitud y cambio de estado queda reflejado en el panel correspondiente.",
            landing_trust_label_2: "Coordinacion",
            landing_trust_value_2: "Menos friccion",
            landing_trust_text_2: "Los filtros reducen pasos innecesarios al elegir materia, tutor, idioma y horario.",
            landing_trust_label_3: "Gestion",
            landing_trust_value_3: "Control por rol",
            landing_trust_text_3: "Estudiante, tutor y admin trabajan con vistas distintas pero conectadas entre si.",
            landing_trust_label_4: "Soporte",
            landing_trust_value_4: "i18n manual",
            landing_trust_text_4: "La interfaz mantiene claves tecnicas estables y textos adaptados al idioma actual.",
            landing_features_title: "Beneficios pensados para convertir interes en accion",
            landing_features_desc: "Cada bloque resuelve una objecion frecuente: tiempo, claridad, confianza y seguimiento.",
            landing_feature_card_1_title: "Filtros avanzados",
            landing_feature_card_1_desc: "Encuentra tutorias por buscador, idioma, facultad, fecha y hora sin revisar listas interminables.",
            landing_feature_card_2_title: "Horario semanal claro",
            landing_feature_card_2_desc: "Visualiza reservas y tutorias por hora para reducir choques de agenda y olvidos.",
            landing_feature_card_3_title: "Roles con control real",
            landing_feature_card_3_desc: "Administra usuarios, materias y asignaciones mientras cada rol solo ve lo que necesita.",
            landing_how_title: "Como funciona",
            landing_how_desc: "Reducimos la friccion con un recorrido simple desde el registro hasta la tutoria confirmada.",
            landing_how_step_1_title: "Explora o publica",
            landing_how_step_1_desc: "El estudiante filtra materias o tutorias publicadas y el tutor publica nuevas disponibilidades.",
            landing_how_step_2_title: "Selecciona la mejor coincidencia",
            landing_how_step_2_desc: "Idioma, facultad, tutor y fecha quedan claros antes de confirmar cualquier accion.",
            landing_how_step_3_title: "Sigue todo desde tu horario",
            landing_how_step_3_desc: "Cada reserva o solicitud aparece en calendario y lista para dar seguimiento sin perder contexto.",
            landing_testimonials_title: "Escenarios de uso reales del producto",
            landing_testimonials_desc: "En lugar de promesas vacias, mostramos como encaja PeerLink en situaciones concretas del dia a dia academico.",
            landing_testimonial_1_title: "Para estudiantes que necesitan decidir rapido",
            landing_testimonial_1_quote: "El flujo de solicitar o aceptar tutorias ayuda a comparar opciones y agendar sin perder tiempo entre mensajes dispersos.",
            landing_testimonial_2_title: "Para tutores que quieren ordenar su semana",
            landing_testimonial_2_quote: "El calendario, las solicitudes y la publicacion de tutorias se concentran en un solo panel facil de seguir.",
            landing_testimonial_3_title: "Para administracion que necesita trazabilidad",
            landing_testimonial_3_quote: "Materias, usuarios, asignaciones y acceso tecnico quedan separados para mantener orden y control.",
            landing_pricing_title: "Claridad total desde el inicio",
            landing_pricing_desc: "Aunque la plataforma sea gratuita, mostrarlo de forma explicita reduce dudas y acelera la decision.",
            landing_pricing_badge: "Plan academico",
            landing_pricing_plan: "PeerLink Campus",
            landing_pricing_plan_desc: "Acceso para estudiantes, tutores y administradores dentro del entorno academico.",
            landing_pricing_price: "$0 / Gratis para siempre",
            landing_pricing_points: "Filtros, reservas, calendario semanal, paneles por rol e internacionalizacion incluida.",
            landing_faq_title: "Preguntas frecuentes",
            landing_faq_desc: "Resolvemos objeciones comunes sin saturar la pagina principal.",
            landing_faq_q1: "La plataforma tiene costo?",
            landing_faq_a1: "No. PeerLink se presenta como una solucion academica gratuita para coordinar tutorias dentro del entorno institucional.",
            landing_faq_q2: "Quien puede usar PeerLink?",
            landing_faq_a2: "Estudiantes, tutores y administradores. Cada rol entra a un panel diferente con acciones ajustadas a su flujo de trabajo.",
            landing_faq_q3: "Que pasa si una tutoria cambia o se cancela?",
            landing_faq_a3: "El estado queda reflejado en el flujo de reservas y el horario semanal para que ambas partes tengan visibilidad inmediata.",
            landing_faq_q4: "Puedo filtrar por idioma o facultad?",
            landing_faq_a4: "Si. Los filtros por idioma, facultad, fecha, hora y buscador forman parte del flujo principal para solicitar o aceptar tutorias.",
            landing_final_cta_title: "Convierte la intencion en una tutoria agendada hoy mismo",
            landing_final_cta_desc: "Si llegaste hasta aqui, ya viste como PeerLink ordena la busqueda, la reserva y el seguimiento en un solo flujo.",
            landing_footer_brand: "Una experiencia academica mas clara para estudiantes, tutores y administradores.",
            landing_footer_nav: "Navegacion",
            landing_footer_support: "Soporte y legal",
            landing_footer_contact: "Contacto",
            landing_copyright: "© 2026 PeerLink. Todos los derechos reservados.",
            login_hero_title: "Inicia sesion en el sistema de tutorias",
            login_hero_desc: "Accede a tu panel como administrador, tutor o estudiante.",
            login_heading: "Login",
            login_welcome_title: "Te damos la bienvenida",
            login_welcome_desc: "Ingresa tus credenciales para continuar.",
            register_welcome_title: "Crea tu cuenta",
            register_welcome_desc: "Completa tus datos para empezar a solicitar o publicar tutorias.",
            login_value_note: "Accede a reservas, tutorias, materias y horario semanal desde un mismo entorno.",
            label_email: "Correo",
            label_password: "Contrasena",
            validation_email_required: "Ingresa un correo valido para continuar.",
            validation_password_required: "Ingresa tu contrasena para continuar.",
            validation_name_required: "Ingresa tu nombre completo.",
            validation_role_required: "Selecciona el rol con el que vas a entrar.",
            validation_subject_required: "Ingresa el nombre de la materia.",
            validation_faculty_required: "Selecciona la facultad.",
            aria_peerlink_home: "Ir al inicio de PeerLink",
            aria_landing_nav: "Navegacion principal de PeerLink",
            aria_go_features: "Ir a beneficios",
            aria_go_how: "Ir a como funciona",
            aria_go_testimonials: "Ir a escenarios",
            aria_go_pricing: "Ir a planes",
            aria_go_faq: "Ir a preguntas frecuentes",
            aria_language_selector: "Selector de idioma",
            aria_toggle_password: "Mostrar u ocultar contrasena",
            aria_close_menu: "Cerrar menu",
            aria_open_menu: "Abrir menu",
            aria_admin_nav: "Navegacion administrador",
            aria_student_nav: "Navegacion estudiante",
            aria_tutor_nav: "Navegacion tutor",
            login_remember_me: "Recordar mis datos",
            login_forgot_password: "Olvidaste tu contrasena?",
            action_back_home: "Volver",
            action_login: "Iniciar sesion",
            login_no_account: "No tienes cuenta?",
            login_register_here: "Registrate aqui",
            console_waiting: "Esperando accion...",
            register_title: "PeerLink | Registro",
            register_heading: "Registro",
            register_subtitle: "Crea una cuenta como estudiante o tutor.",
            register_value_note: "Usa un correo que revises con frecuencia para recibir el seguimiento de tus tutorias.",
            label_full_name: "Nombre completo",
            label_role: "Rol",
            action_register: "Registrarse",
            register_has_account: "Ya tienes cuenta?",
            register_back_login: "Volver al login",
            forgot_password_title: "Recuperar acceso",
            forgot_password_desc: "En esta version, la recuperacion se gestiona con soporte o administracion. Dejanos tu correo y te mostraremos los siguientes pasos.",
            forgot_password_note: "Si ya tienes una sesion activa, primero cierra sesion para evitar confusiones con el cambio de cuenta.",
            forgot_password_action: "Solicitar ayuda",
            forgot_password_back: "Volver al login",
            profile_title: "PeerLink | Mi Perfil",
            profile_heading: "Mi perfil",
            profile_desc: "Consulta y actualiza tus datos de cuenta sin salir de la plataforma.",
            profile_info_title: "Datos de la cuenta",
            profile_password_title: "Cambiar contrasena",
            profile_role_hint: "Tu rol se asigna desde la administracion y no puede modificarse desde este panel.",
            profile_save: "Guardar cambios",
            profile_password_save: "Actualizar contrasena",
            profile_current_password: "Contrasena actual",
            profile_new_password: "Nueva contrasena",
            help_title: "PeerLink | Ayuda",
            help_heading: "Centro de ayuda",
            help_desc: "Encuentra respuestas rapidas sobre reservas, tutorias, cuentas y uso general de la plataforma.",
            legal_terms_title: "PeerLink | Terminos",
            legal_privacy_title: "PeerLink | Privacidad",
            legal_terms_heading: "Terminos y condiciones",
            legal_privacy_heading: "Politica de privacidad",
            legal_terms_intro: "PeerLink es una plataforma academica de coordinacion de tutorias. Su uso debe mantenerse dentro de fines educativos, respetando a estudiantes, tutores y administradores.",
            legal_terms_body_1: "Al usar la plataforma aceptas proporcionar informacion veraz, cuidar tu acceso y no utilizar la aplicacion para fines ajenos al acompanamiento academico.",
            legal_terms_body_2: "Las reservas, cambios de estado y publicaciones quedan sujetas a las reglas operativas de la institucion y a la disponibilidad de tutores y materias.",
            legal_privacy_intro: "PeerLink utiliza datos de cuenta, rol, materias y reservas unicamente para gestionar autenticacion, tutorias y paneles de trabajo.",
            legal_privacy_body_1: "La informacion almacenada se limita a los datos necesarios para operar la plataforma y mostrar a cada persona el contenido correspondiente a su rol.",
            legal_privacy_body_2: "Las credenciales se gestionan de forma segura y la aplicacion no debe compartir datos con terceros no autorizados dentro del alcance actual del proyecto.",
            legal_terms_badge: "Acuerdos y reglas",
            legal_terms_section_usage: "Compromiso de uso",
            legal_terms_section_reservations: "Reservas y disponibilidad",
            legal_terms_notice: "Cualquier uso indebido o automatizado de la plataforma puede resultar en la suspension del acceso al panel de reservas.",
            legal_terms_footer: "© 2026 PeerLink. Fomentando el orden y la colaboracion academica.",
            legal_privacy_badge: "Proteccion de datos",
            legal_privacy_section_data: "Uso de la informacion",
            legal_privacy_section_credentials: "Gestion de credenciales",
            legal_privacy_notice: "Esta politica esta disenada para garantizar un entorno academico seguro, confiable y libre de distracciones.",
            legal_privacy_footer: "© 2026 PeerLink. Haciendo la vida universitaria mas segura y conectada.",
            help_badge: "Soporte academico",
            help_unresolved_title: "Aun no resuelves tu problema?",
            help_footer: "© 2026 PeerLink. Haciendo la vida universitaria mas simple.",
            error_403_title: "Acceso denegado",
            error_404_title: "Pagina no encontrada",
            error_500_title: "Ocurrio un error",
            error_page_desc: "Puedes volver al inicio, revisar tu sesion o consultar ayuda.",
            admin_title: "PeerLink | Panel Admin",
            admin_hero_title: "Gestion de Materias",
            admin_hero_desc: "Administra materias, asignaciones de tutores y usuarios desde secciones separadas.",
            admin_tab_subjects: "Materias",
            admin_tab_tutors: "Tutores",
            admin_tab_users: "Usuarios",
            admin_subject_create_tab: "Crear materia",
            admin_subject_search_tab: "Buscar materias",
            admin_subject_form: "Formulario de materia",
            admin_subject_name: "Nombre de la materia",
            admin_save_subject: "Agregar / Guardar",
            admin_cancel_edit: "Cancelar edicion",
            admin_assign_title: "Asignar tutor a materia",
            admin_assign_action: "Asignar",
            admin_assign_desc: "Filtra tutores y materias para crear asignaciones con mas rapidez.",
            admin_subject_table: "Tabla de materias",
            admin_assignments_table: "Asignaciones tutor - materia",
            admin_users_title: "Usuarios",
            admin_footer: "© 2026 PeerLink. Panel de Administracion.",
            admin_save_user: "Agregar usuario",
            admin_open_swagger: "Abrir Swagger",
            admin_swagger_hint: "Documentacion protegida para administradores.",
            admin_search_subjects_title: "Buscar materias",
            admin_search_subjects_desc: "Usa filtros, revisa coincidencias y haz clic en una materia para desplegar su menu de edicion o eliminacion.",
            admin_users_search_placeholder: "Buscar por nombre, correo o rol",
            action_refresh: "Refrescar",
            action_open_swagger: "Abrir Swagger",
            action_apply_filters: "Aplicar filtros",
            action_clear_filters: "Limpiar filtros",
            action_search: "Buscar",
            action_clear: "Limpiar",
            action_select: "Seleccionar",
            action_remove: "Remover",
            action_hide: "Quitar",
            action_undo: "Deshacer",
            action_accept: "Aceptar",
            action_send_request: "Enviar solicitud",
            action_save_tutoring: "Guardar tutoria",
            action_confirm_reservation: "Confirmar reserva",
            action_go_home: "Ir al inicio",
            state_editing: "Editando",
            state_match: "Coincidencia",
            action_prev_week: "Semana anterior",
            action_next_week: "Semana siguiente",
            action_this_week: "Esta semana",
            console_title: "Consola",
            console_technical_title: "Actividad tecnica",
            feedback_loaded: "La informacion se actualizo correctamente.",
            student_title: "PeerLink | Panel Estudiante",
            student_panel_heading: "Panel Estudiante",
            student_panel_desc: "Gestiona tus tutorias en dos flujos: solicitar una nueva o aceptar una tutoria ya publicada por un tutor.",
            student_tab_tutoring: "Tutorias",
            student_tab_schedule: "Mis materias y horario",
            student_subtab_request: "Solicitar",
            student_subtab_accept: "Aceptar",
            student_request_title: "Buscar materias para solicitar",
            student_request_desc: "Filtra por buscador y facultad. Al seleccionar una coincidencia podras elegir tutor, idioma y fecha/hora.",
            student_request_matches: "Coincidencias de materias",
            student_request_selected: "Solicitud seleccionada",
            student_request_empty: "Selecciona una materia para desplegar el formulario de solicitud.",
            student_request_hint: "Primero filtra, luego elige una materia y por ultimo completa tutor, idioma y fecha.",
            student_accept_title: "Aceptar tutorias publicadas",
            student_accept_desc: "Usa el filtro como entrada principal y luego haz clic sobre una coincidencia para reservarla.",
            student_accept_selected: "Tutoria seleccionada",
            student_accept_empty: "Selecciona una coincidencia para desplegar su menu de reserva.",
            student_accept_hint: "Elige una tutoria publicada para revisar los detalles antes de confirmarla.",
            student_schedule_filter_title: "Filtrar mi horario",
            student_schedule_filter_desc: "Busca tus reservas por texto, idioma, facultad y fecha/hora; luego cambia entre calendario y lista.",
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
            tutor_panel_heading: "Panel Tutor",
            tutor_panel_desc: "Administra solicitudes recibidas, publica nuevas tutorias y consulta tu horario semanal en calendario o lista.",
            tutor_tab_requests: "Solicitudes recibidas",
            tutor_tab_create: "Crear tutoria",
            tutor_tab_schedule: "Mis materias y horario",
            tutor_requests_desc_extended: "Filtra por buscador, idioma, facultad y fecha/hora. Tambien puedes ocultar las ya respondidas o deshacer una respuesta.",
            tutor_hide_answered: "Ocultar solicitudes ya respondidas",
            tutor_create_title: "Buscar materia para publicar",
            tutor_create_desc: "Busca una materia creada por admin y selecciona la coincidencia para asignarle la fecha y hora de tu tutoria.",
            tutor_create_hint: "Selecciona una materia, revisa su idioma y facultad, y luego define la fecha y hora de la tutoria.",
            tutor_create_selected: "Tutoria a publicar",
            tutor_create_empty: "Selecciona una coincidencia para desplegar el formulario de publicacion.",
            tutor_published_title: "Tutorias publicadas por mi",
            tutor_published_desc: "Desde aqui puedes revisar o remover las tutorias que ya publicaste.",
            tutor_schedule_filter_title: "Filtrar mi horario",
            tutor_schedule_filter_desc: "Consulta tus tutorias reales por buscador, idioma, facultad y fecha/hora, en calendario semanal o lista.",
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
            label_search: "Buscador",
            label_pick_week: "Semana a visualizar",
            validation_language_required: "Selecciona el idioma de la tutoria.",
            validation_tutor_required: "Selecciona el tutor de la tutoria.",
            validation_datetime_required: "Selecciona la fecha y hora de la tutoria.",
            loading_matches: "Buscando coincidencias...",
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
            action_download_report: "Descargar reporte PDF",
            action_download_tutors_report: "Reporte tutores",
            action_download_users_report: "Reporte usuarios",
            no_actions: "Sin acciones",
            schedule_weekly: "Calendario semanal",
            schedule_list: "Lista simple",
            empty_subjects: "No hay materias registradas.",
            empty_assignments: "No hay relaciones registradas.",
            empty_users: "No hay usuarios registrados.",
            empty_available_tutoring: "No hay tutores disponibles.",
            empty_my_reservations: "No tienes tutorias programadas con esos filtros.",
            empty_tutor_requests: "No tienes tutorias asignadas con esos filtros.",
            empty_week_schedule: "No hay tutorias en la semana seleccionada.",
            empty_filtered_subjects: "No hay materias que coincidan con ese filtro.",
            empty_subject_tutors: "No hay tutores disponibles para esta materia.",
            results_count: "{count} coincidencias",
            reservations_count: "{count} reservas",
            feedback_loaded_student_data: "Se actualizaron las materias, tutores y horarios disponibles.",
            feedback_request_created: "La solicitud fue enviada y ahora aparece en tu horario.",
            feedback_reservation_created: "La tutoria fue reservada correctamente.",
            feedback_schedule_loaded: "Tu horario se actualizo con la informacion mas reciente.",
            feedback_login_error: "No se pudo iniciar sesion con esas credenciales.",
            feedback_register_success: "La cuenta fue creada correctamente. Ya puedes iniciar sesion.",
            feedback_profile_loaded: "Tus datos de perfil fueron cargados.",
            feedback_profile_saved: "Tus datos se guardaron correctamente.",
            feedback_recovery_submitted: "Registramos tu solicitud y el siguiente paso aparece aqui.",
            feedback_loaded_tutor_data: "Se actualizaron solicitudes, materias y horarios del tutor.",
            feedback_tutor_request_updated: "La solicitud se actualizo correctamente.",
            feedback_tutor_offer_saved: "La tutoria fue publicada correctamente.",
            feedback_tutor_offer_deleted: "La tutoria publicada fue removida.",
            feedback_loaded_admin_data: "Se actualizaron materias, asignaciones y usuarios.",
            feedback_subject_saved: "La materia se guardo correctamente.",
            feedback_subject_deleted: "La materia fue eliminada.",
            feedback_assignment_saved: "La asignacion tutor-materia se guardo correctamente.",
            feedback_user_saved: "El usuario fue creado correctamente.",
            feedback_user_deleted: "El usuario fue eliminado correctamente.",
            feedback_report_downloaded: "El reporte PDF se descargo correctamente.",
            placeholder_subject_name: "Nombre, idioma o facultad",
            placeholder_tutor_name: "Nombre o correo",
            placeholder_assignment_search: "Buscar asignaciones",
            placeholder_user_search: "Buscar por nombre, correo o rol",
            placeholder_subject_or_tutor: "Buscar materia o tutor",
            placeholder_subject_or_student: "Buscar estudiante o materia",
            placeholder_subject_only: "Buscar por nombre de materia",
            placeholder_email: "Tu correo institucional o de acceso",
            placeholder_login_email: "correo@ejemplo.com",
            placeholder_password: "Contrasena",
            placeholder_full_name: "Nombre completo",
            status_ready: "ready",
            console_idle: "La actividad reciente aparecera aqui.",
            password_updated: "Contrasena actualizada correctamente.",
            contact_support_or_admin: "Contacta soporte o administracion para continuar.",
            role_all: "Todos",
            select_all_languages: "Todos los idiomas",
            select_all_faculties: "Todas las facultades",
            calendar_hour: "Hora",
            calendar_week_of: "Semana de {date}",
            reservation_with_tutor: "{subject} con {name}",
            reservation_with_student: "{subject} con {name}",
            confirm_delete_subject: "Eliminar materia?",
            confirm_delete_user: "Eliminar usuario {name}?",
            redirecting_to_login: "Redirigiendo a",
            help_contact_line: "Si sigues bloqueada, comparte una captura y el mensaje de error con soporte o con el administrador.",
            help_section_account: "Cuenta y acceso",
            help_section_student: "Estudiantes",
            help_section_tutor: "Tutores",
            help_section_admin: "Administracion",
            help_section_general: "Uso general",
            help_faq_login_q: "No puedo iniciar sesion",
            help_faq_login_a: "Verifica correo, contrasena y que el navegador permita el certificado local si trabajas en entorno de desarrollo.",
            help_faq_student_q: "Como solicito una tutoria",
            help_faq_student_a: "Desde Tutorias puedes usar Solicitar para proponer fecha y tutor, o Aceptar para reservar una tutoria ya publicada.",
            help_faq_tutor_q: "Como publico una tutoria",
            help_faq_tutor_a: "Desde Crear tutoria filtra la materia, selecciona la coincidencia y asigna fecha y hora.",
            help_faq_admin_q: "Que puedo hacer como admin",
            help_faq_admin_a: "Puedes crear materias, asignar tutores, administrar usuarios y abrir Swagger con permisos protegidos.",
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
            navbar_profile: "Profile",
            navbar_help: "Help",
            navbar_user_fallback: "User",
            navbar_no_role: "NO ROLE",
            login_title: "PeerLink | Login",
            landing_title: "PeerLink | Tutoring platform",
            landing_badge: "Organized university tutoring",
            landing_heading: "Connect students and tutors in one clear flow.",
            landing_desc: "PeerLink centralizes subjects, reservations, requests, and weekly schedules so the academic experience feels simple, visible, and organized.",
            landing_primary_cta: "Sign in",
            landing_secondary_cta: "Create account",
            landing_panel_cta: "Go to my panel",
            landing_section_roles: "An experience for each role",
            landing_section_roles_desc: "Each dashboard prioritizes what matters most for students, tutors, and administrators.",
            landing_role_student: "Students",
            landing_role_student_desc: "They search tutoring sessions, filter matches, request support, and review their schedule in calendar or list view.",
            landing_role_tutor: "Tutors",
            landing_role_tutor_desc: "They publish tutoring sessions, manage incoming requests, and keep their weekly agenda visible.",
            landing_role_admin: "Administration",
            landing_role_admin_desc: "It controls subjects, assignments, users, and protected technical documentation from one place.",
            landing_section_features: "The essentials, without clutter",
            landing_feature_filters: "Filters by text, language, faculty, date, and time to reach the right match quickly.",
            landing_feature_calendar: "Weekly hourly schedules to clearly see reservations, tutoring sessions, and availability.",
            landing_feature_i18n: "Interface prepared for Spanish, English, and Portuguese with consistent keys.",
            landing_footer: "Built to coordinate tutoring with clarity, traceability, and a strong user experience.",
            landing_nav_features: "Benefits",
            landing_nav_how: "How it works",
            landing_nav_testimonials: "Scenarios",
            landing_nav_pricing: "Plan",
            landing_nav_faq: "FAQ",
            landing_cta_primary: "Start free",
            landing_cta_secondary: "Learn more",
            landing_support_text: "Designed for students who need immediate clarity and tutors who want a better organized week.",
            landing_support_badge: "Available in Spanish, English, and Portuguese",
            landing_mock_calendar_label: "Calendar",
            landing_mock_calendar: "Review published tutoring sessions, requests, and reservations from a weekly hourly calendar.",
            landing_mock_agenda_title: "Your schedule, under control",
            landing_mock_example: "Calculus I - 10:00 AM",
            landing_mock_filters_label: "Filters",
            landing_mock_filters: "Filter by language, faculty, and date to reach the right match faster.",
            landing_mock_roles_label: "Roles",
            landing_mock_roles: "Separate dashboards for student, tutor, and admin with clear responsibilities in each role.",
            landing_mock_security_label: "Security",
            landing_mock_security: "Role-protected access and administrative control keep the experience organized and traceable.",
            landing_trust_label_1: "Clarity",
            landing_trust_value_1: "Visible flow",
            landing_trust_text_1: "Every reservation, request, and status change appears in the panel where it belongs.",
            landing_trust_label_2: "Coordination",
            landing_trust_value_2: "Less friction",
            landing_trust_text_2: "Filters reduce unnecessary steps when choosing subject, tutor, language, and schedule.",
            landing_trust_label_3: "Management",
            landing_trust_value_3: "Role control",
            landing_trust_text_3: "Students, tutors, and admins work from different but connected views.",
            landing_trust_label_4: "Support",
            landing_trust_value_4: "Manual i18n",
            landing_trust_text_4: "The interface keeps stable technical keys while adapting visible labels to the active language.",
            landing_features_title: "Benefits designed to turn interest into action",
            landing_features_desc: "Each block answers a common objection: time, clarity, trust, and follow-up.",
            landing_feature_card_1_title: "Advanced filters",
            landing_feature_card_1_desc: "Find tutoring sessions by search, language, faculty, date, and time without scanning endless lists.",
            landing_feature_card_2_title: "Clear weekly schedule",
            landing_feature_card_2_desc: "See reservations and tutoring sessions by hour to reduce schedule conflicts and missed sessions.",
            landing_feature_card_3_title: "Real role control",
            landing_feature_card_3_desc: "Manage users, subjects, and assignments while each role sees only what it needs.",
            landing_how_title: "How it works",
            landing_how_desc: "We reduce friction with a simple path from registration to a confirmed tutoring session.",
            landing_how_step_1_title: "Explore or publish",
            landing_how_step_1_desc: "Students filter subjects or published tutoring sessions while tutors publish new availability.",
            landing_how_step_2_title: "Choose the best match",
            landing_how_step_2_desc: "Language, faculty, tutor, and date stay clear before any action is confirmed.",
            landing_how_step_3_title: "Track everything from your schedule",
            landing_how_step_3_desc: "Each reservation or request appears in calendar and list view so follow-up stays simple.",
            landing_testimonials_title: "Real product use scenarios",
            landing_testimonials_desc: "Instead of empty promises, we show how PeerLink fits concrete academic situations.",
            landing_testimonial_1_title: "For students who need to decide quickly",
            landing_testimonial_1_quote: "The request or accept flow helps compare options and schedule support without juggling scattered messages.",
            landing_testimonial_2_title: "For tutors who want an organized week",
            landing_testimonial_2_quote: "Calendar, requests, and tutoring publication stay together in one dashboard that is easy to follow.",
            landing_testimonial_3_title: "For administration that needs traceability",
            landing_testimonial_3_quote: "Subjects, users, assignments, and technical access stay separate enough to preserve order and control.",
            landing_pricing_title: "Full clarity from the start",
            landing_pricing_desc: "Even when the platform is free, stating it clearly reduces doubt and speeds up the decision.",
            landing_pricing_badge: "Academic plan",
            landing_pricing_plan: "PeerLink Campus",
            landing_pricing_plan_desc: "Access for students, tutors, and administrators inside the academic environment.",
            landing_pricing_price: "$0 / Free forever",
            landing_pricing_points: "Filters, reservations, weekly calendar, role-based dashboards, and built-in internationalization.",
            landing_faq_title: "Frequently asked questions",
            landing_faq_desc: "We answer common objections without overcrowding the page.",
            landing_faq_q1: "Does the platform have a cost?",
            landing_faq_a1: "No. PeerLink is presented as a free academic solution to coordinate tutoring inside the institutional environment.",
            landing_faq_q2: "Who can use PeerLink?",
            landing_faq_a2: "Students, tutors, and administrators. Each role enters a different dashboard with actions tailored to its workflow.",
            landing_faq_q3: "What happens if a tutoring session changes or is canceled?",
            landing_faq_a3: "The status is reflected in reservations and the weekly schedule so both sides keep immediate visibility.",
            landing_faq_q4: "Can I filter by language or faculty?",
            landing_faq_a4: "Yes. Language, faculty, date, time, and search filters are part of the main flow for requesting or accepting tutoring sessions.",
            landing_final_cta_title: "Turn interest into a scheduled tutoring session today",
            landing_final_cta_desc: "If you reached this point, you already saw how PeerLink organizes search, reservation, and follow-up in one flow.",
            landing_footer_brand: "A clearer academic experience for students, tutors, and administrators.",
            landing_footer_nav: "Navigation",
            landing_footer_support: "Support and legal",
            landing_footer_contact: "Contact",
            landing_copyright: "© 2026 PeerLink. All rights reserved.",
            login_hero_title: "Sign in to the tutoring system",
            login_hero_desc: "Access your dashboard as administrator, tutor, or student.",
            login_heading: "Login",
            login_welcome_title: "Welcome back",
            login_welcome_desc: "Enter your credentials to continue.",
            register_welcome_title: "Create your account",
            register_welcome_desc: "Complete your details to start requesting or publishing tutoring sessions.",
            login_value_note: "Access reservations, tutoring, subjects, and your weekly schedule from one place.",
            label_email: "Email",
            label_password: "Password",
            validation_email_required: "Enter a valid email to continue.",
            validation_password_required: "Enter your password to continue.",
            validation_name_required: "Enter your full name.",
            validation_role_required: "Select the role you will use to enter.",
            validation_subject_required: "Enter the subject name.",
            validation_faculty_required: "Select the faculty.",
            aria_peerlink_home: "Go to PeerLink home",
            aria_landing_nav: "PeerLink main navigation",
            aria_go_features: "Go to benefits",
            aria_go_how: "Go to how it works",
            aria_go_testimonials: "Go to scenarios",
            aria_go_pricing: "Go to plans",
            aria_go_faq: "Go to frequently asked questions",
            aria_language_selector: "Language selector",
            aria_toggle_password: "Show or hide password",
            aria_close_menu: "Close menu",
            aria_open_menu: "Open menu",
            aria_admin_nav: "Administrator navigation",
            aria_student_nav: "Student navigation",
            aria_tutor_nav: "Tutor navigation",
            login_remember_me: "Remember my details",
            login_forgot_password: "Forgot your password?",
            action_back_home: "Back",
            action_login: "Sign in",
            login_no_account: "Don't have an account?",
            login_register_here: "Register here",
            console_waiting: "Waiting for action...",
            register_title: "PeerLink | Register",
            register_heading: "Register",
            register_subtitle: "Create an account as a student or tutor.",
            register_value_note: "Use an email you check often so tutoring follow-up stays easy to manage.",
            label_full_name: "Full name",
            label_role: "Role",
            action_register: "Register",
            register_has_account: "Already have an account?",
            register_back_login: "Back to login",
            forgot_password_title: "Recover access",
            forgot_password_desc: "In this version, recovery is handled through support or administration. Leave your email and we will show you the next steps.",
            forgot_password_note: "If you already have an active session, sign out first to avoid confusion while switching accounts.",
            forgot_password_action: "Request help",
            forgot_password_back: "Back to login",
            profile_title: "PeerLink | My Profile",
            profile_heading: "My profile",
            profile_desc: "Review and update your account details without leaving the platform.",
            profile_info_title: "Account details",
            profile_password_title: "Change password",
            profile_role_hint: "Your role is assigned by administration and cannot be changed from this panel.",
            profile_save: "Save changes",
            profile_password_save: "Update password",
            profile_current_password: "Current password",
            profile_new_password: "New password",
            help_title: "PeerLink | Help",
            help_heading: "Help center",
            help_desc: "Find quick answers about reservations, tutoring sessions, accounts, and general platform usage.",
            legal_terms_title: "PeerLink | Terms",
            legal_privacy_title: "PeerLink | Privacy",
            legal_terms_heading: "Terms and conditions",
            legal_privacy_heading: "Privacy policy",
            legal_terms_intro: "PeerLink is an academic tutoring coordination platform. Its use should remain within educational purposes and respect students, tutors, and administrators.",
            legal_terms_body_1: "By using the platform, you agree to provide truthful information, protect your access, and avoid using the application for purposes unrelated to academic support.",
            legal_terms_body_2: "Reservations, status changes, and published sessions remain subject to institutional operating rules and the availability of tutors and subjects.",
            legal_privacy_intro: "PeerLink uses account, role, subject, and reservation data only to manage authentication, tutoring, and work dashboards.",
            legal_privacy_body_1: "Stored information is limited to the data needed to operate the platform and show each person the content that matches their role.",
            legal_privacy_body_2: "Credentials are managed securely, and within the current project scope the application must not share data with unauthorized third parties.",
            legal_terms_badge: "Agreements and rules",
            legal_terms_section_usage: "Acceptable use commitment",
            legal_terms_section_reservations: "Reservations and availability",
            legal_terms_notice: "Any improper or automated use of the platform may result in suspension of access to the reservations dashboard.",
            legal_terms_footer: "© 2026 PeerLink. Promoting academic order and collaboration.",
            legal_privacy_badge: "Data protection",
            legal_privacy_section_data: "Information use",
            legal_privacy_section_credentials: "Credential management",
            legal_privacy_notice: "This policy is designed to support a safe, reliable academic environment free of distractions.",
            legal_privacy_footer: "© 2026 PeerLink. Making university life safer and more connected.",
            help_badge: "Academic support",
            help_unresolved_title: "Still need help?",
            help_footer: "© 2026 PeerLink. Making university life simpler.",
            error_403_title: "Access denied",
            error_404_title: "Page not found",
            error_500_title: "Something went wrong",
            error_page_desc: "You can go back home, review your session, or open help.",
            admin_title: "PeerLink | Admin Panel",
            admin_hero_title: "Subject Management",
            admin_hero_desc: "Manage subjects, tutor assignments, and users from separate sections.",
            admin_tab_subjects: "Subjects",
            admin_tab_tutors: "Tutors",
            admin_tab_users: "Users",
            admin_subject_create_tab: "Create subject",
            admin_subject_search_tab: "Search subjects",
            admin_subject_form: "Subject form",
            admin_subject_name: "Subject name",
            admin_save_subject: "Add / Save",
            admin_cancel_edit: "Cancel edit",
            admin_assign_title: "Assign tutor to subject",
            admin_assign_action: "Assign",
            admin_assign_desc: "Filter tutors and subjects to create assignments faster.",
            admin_subject_table: "Subjects table",
            admin_assignments_table: "Tutor - subject assignments",
            admin_users_title: "Users",
            admin_footer: "© 2026 PeerLink. Administration panel.",
            admin_save_user: "Add user",
            admin_open_swagger: "Open Swagger",
            admin_swagger_hint: "Protected documentation for administrators.",
            admin_search_subjects_title: "Search subjects",
            admin_search_subjects_desc: "Use filters, review matches, and click a subject to open its edit or delete menu.",
            admin_users_search_placeholder: "Search by name, email, or role",
            action_refresh: "Refresh",
            action_open_swagger: "Open Swagger",
            action_apply_filters: "Apply filters",
            action_clear_filters: "Clear filters",
            action_search: "Search",
            action_clear: "Clear",
            action_select: "Select",
            action_remove: "Remove",
            action_hide: "Hide",
            action_undo: "Undo",
            action_accept: "Accept",
            action_send_request: "Send request",
            action_save_tutoring: "Save tutoring",
            action_confirm_reservation: "Confirm reservation",
            action_go_home: "Go home",
            state_editing: "Editing",
            state_match: "Match",
            action_prev_week: "Previous week",
            action_next_week: "Next week",
            action_this_week: "This week",
            console_title: "Console",
            console_technical_title: "Technical activity",
            feedback_loaded: "Information was refreshed successfully.",
            student_title: "PeerLink | Student Panel",
            student_panel_heading: "Student Panel",
            student_panel_desc: "Manage your tutoring in two flows: request a new one or accept a tutoring session already published by a tutor.",
            student_tab_tutoring: "Tutoring",
            student_tab_schedule: "My subjects and schedule",
            student_subtab_request: "Request",
            student_subtab_accept: "Accept",
            student_request_title: "Search subjects to request",
            student_request_desc: "Filter by search box and faculty. After selecting a match you can choose tutor, language, and date/time.",
            student_request_matches: "Subject matches",
            student_request_selected: "Selected request",
            student_request_empty: "Select a subject to open the request form.",
            student_request_hint: "First filter, then choose a subject, and finally complete tutor, language, and date.",
            student_accept_title: "Accept published tutoring",
            student_accept_desc: "Use the filter as the main entry point and then click a match to reserve it.",
            student_accept_selected: "Selected tutoring",
            student_accept_empty: "Select a match to open its reservation menu.",
            student_accept_hint: "Choose a published tutoring session to review its details before confirming.",
            student_schedule_filter_title: "Filter my schedule",
            student_schedule_filter_desc: "Search your reservations by text, language, faculty, and date/time; then switch between calendar and list.",
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
            tutor_panel_heading: "Tutor Panel",
            tutor_panel_desc: "Manage incoming requests, publish new tutoring sessions, and review your weekly schedule in calendar or list view.",
            tutor_tab_requests: "Received requests",
            tutor_tab_create: "Create tutoring",
            tutor_tab_schedule: "My subjects and schedule",
            tutor_requests_desc_extended: "Filter by search box, language, faculty, and date/time. You can also hide answered requests or undo a response.",
            tutor_hide_answered: "Hide answered requests",
            tutor_create_title: "Search subject to publish",
            tutor_create_desc: "Search a subject created by the admin and select the match to assign the date and time of your tutoring session.",
            tutor_create_hint: "Select a subject, review its language and faculty, and then define the tutoring date and time.",
            tutor_create_selected: "Tutoring to publish",
            tutor_create_empty: "Select a match to open the publishing form.",
            tutor_published_title: "Tutoring sessions published by me",
            tutor_published_desc: "From here you can review or remove the tutoring sessions you have already published.",
            tutor_schedule_filter_title: "Filter my schedule",
            tutor_schedule_filter_desc: "Review your real tutoring sessions by search text, language, faculty, and date/time in weekly calendar or list view.",
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
            label_search: "Search",
            label_pick_week: "Week to view",
            validation_language_required: "Select the tutoring language.",
            validation_tutor_required: "Select the tutoring tutor.",
            validation_datetime_required: "Select the tutoring date and time.",
            loading_matches: "Searching matches...",
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
            action_download_report: "Download PDF report",
            action_download_tutors_report: "Tutors report",
            action_download_users_report: "Users report",
            no_actions: "No actions",
            schedule_weekly: "Weekly calendar",
            schedule_list: "Simple list",
            empty_subjects: "There are no registered subjects.",
            empty_assignments: "There are no registered relationships.",
            empty_users: "There are no registered users.",
            empty_available_tutoring: "There are no available tutors.",
            empty_my_reservations: "You have no tutoring sessions for these filters.",
            empty_tutor_requests: "You have no tutoring sessions for these filters.",
            empty_week_schedule: "There are no tutoring sessions in the selected week.",
            empty_filtered_subjects: "There are no subjects matching this filter.",
            empty_subject_tutors: "There are no tutors available for this subject.",
            results_count: "{count} matches",
            reservations_count: "{count} reservations",
            feedback_loaded_student_data: "Subjects, tutors, and available schedules were refreshed.",
            feedback_request_created: "Your request was sent and now appears in your schedule.",
            feedback_reservation_created: "The tutoring session was reserved successfully.",
            feedback_schedule_loaded: "Your schedule was refreshed with the latest information.",
            feedback_login_error: "Could not sign in with those credentials.",
            feedback_register_success: "The account was created successfully. You can now sign in.",
            feedback_profile_loaded: "Your profile details were loaded.",
            feedback_profile_saved: "Your profile details were saved successfully.",
            feedback_recovery_submitted: "We recorded your request and the next step appears here.",
            feedback_loaded_tutor_data: "Tutor requests, subjects, and schedules were refreshed.",
            feedback_tutor_request_updated: "The request was updated successfully.",
            feedback_tutor_offer_saved: "The tutoring session was published successfully.",
            feedback_tutor_offer_deleted: "The published tutoring session was removed.",
            feedback_loaded_admin_data: "Subjects, assignments, and users were refreshed.",
            feedback_subject_saved: "The subject was saved successfully.",
            feedback_subject_deleted: "The subject was deleted.",
            feedback_assignment_saved: "The tutor-subject assignment was saved successfully.",
            feedback_user_saved: "The user was created successfully.",
            feedback_user_deleted: "The user was deleted successfully.",
            feedback_report_downloaded: "The PDF report was downloaded successfully.",
            placeholder_subject_name: "Name, language, or faculty",
            placeholder_tutor_name: "Name or email",
            placeholder_assignment_search: "Search assignments",
            placeholder_user_search: "Search by name, email, or role",
            placeholder_subject_or_tutor: "Search subject or tutor",
            placeholder_subject_or_student: "Search student or subject",
            placeholder_subject_only: "Search by subject name",
            placeholder_email: "Your institutional or access email",
            placeholder_login_email: "email@example.com",
            placeholder_password: "Password",
            placeholder_full_name: "Full name",
            status_ready: "ready",
            console_idle: "Recent activity will appear here.",
            password_updated: "Password updated successfully.",
            contact_support_or_admin: "Contact support or administration to continue.",
            role_all: "All",
            select_all_languages: "All languages",
            select_all_faculties: "All faculties",
            calendar_hour: "Hour",
            calendar_week_of: "Week of {date}",
            reservation_with_tutor: "{subject} with {name}",
            reservation_with_student: "{subject} with {name}",
            confirm_delete_subject: "Delete subject?",
            confirm_delete_user: "Delete user {name}?",
            redirecting_to_login: "Redirecting to",
            help_contact_line: "If you are still blocked, share a screenshot and the error message with support or the administrator.",
            help_section_account: "Account and access",
            help_section_student: "Students",
            help_section_tutor: "Tutors",
            help_section_admin: "Administration",
            help_section_general: "General use",
            help_faq_login_q: "I cannot sign in",
            help_faq_login_a: "Check your email, password, and that your browser accepts the local certificate if you are working in a development environment.",
            help_faq_student_q: "How do I request tutoring",
            help_faq_student_a: "From Tutoring you can use Request to propose date and tutor, or Accept to reserve a tutoring session already published.",
            help_faq_tutor_q: "How do I publish tutoring",
            help_faq_tutor_a: "From Create tutoring, filter the subject, select the match, and assign date and time.",
            help_faq_admin_q: "What can I do as admin",
            help_faq_admin_a: "You can create subjects, assign tutors, manage users, and open Swagger with protected permissions.",
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
            navbar_profile: "Perfil",
            navbar_help: "Ajuda",
            navbar_user_fallback: "Usuario",
            navbar_no_role: "SEM PAPEL",
            login_title: "PeerLink | Login",
            landing_title: "PeerLink | Plataforma de tutorias",
            landing_badge: "Tutoria universitaria organizada",
            landing_heading: "Conecte estudantes e tutores em um fluxo claro.",
            landing_desc: "PeerLink centraliza materias, reservas, solicitacoes e horarios semanais para que a experiencia academica seja simples, visivel e organizada.",
            landing_primary_cta: "Entrar",
            landing_secondary_cta: "Criar conta",
            landing_panel_cta: "Ir ao meu painel",
            landing_section_roles: "Uma experiencia para cada papel",
            landing_section_roles_desc: "Cada painel prioriza o que importa para estudantes, tutores e administradores.",
            landing_role_student: "Estudantes",
            landing_role_student_desc: "Buscam tutorias, filtram coincidencias, solicitam apoio e consultam seu horario em calendario ou lista.",
            landing_role_tutor: "Tutores",
            landing_role_tutor_desc: "Publicam tutorias, gerenciam solicitacoes recebidas e mantem sua agenda semanal visivel.",
            landing_role_admin: "Administracao",
            landing_role_admin_desc: "Controla materias, atribuicoes, usuarios e documentacao tecnica protegida em um so lugar.",
            landing_section_features: "O essencial, sem desordem",
            landing_feature_filters: "Filtros por texto, idioma, faculdade, data e hora para chegar rapidamente a coincidencia correta.",
            landing_feature_calendar: "Horarios semanais por hora para ver claramente reservas, tutorias e disponibilidade.",
            landing_feature_i18n: "Interface preparada para espanhol, ingles e portugues com chaves consistentes.",
            landing_footer: "Construido para coordenar tutorias com clareza, rastreabilidade e boa experiencia de uso.",
            landing_nav_features: "Beneficios",
            landing_nav_how: "Como funciona",
            landing_nav_testimonials: "Cenarios",
            landing_nav_pricing: "Plano",
            landing_nav_faq: "FAQ",
            landing_cta_primary: "Comecar gratis",
            landing_cta_secondary: "Saber mais",
            landing_support_text: "Pensado para estudantes que precisam de clareza imediata e tutores que querem organizar melhor sua semana.",
            landing_support_badge: "Disponivel em espanhol, ingles e portugues",
            landing_mock_calendar_label: "Calendario",
            landing_mock_calendar: "Consulte tutorias publicadas, solicitacoes e reservas em um calendario semanal por hora.",
            landing_mock_agenda_title: "Sua agenda sob controle",
            landing_mock_example: "Calculo I - 10:00",
            landing_mock_filters_label: "Filtros",
            landing_mock_filters: "Filtre por idioma, faculdade e data para chegar mais rapido a coincidencia correta.",
            landing_mock_roles_label: "Papeis",
            landing_mock_roles: "Paineis separados para estudante, tutor e admin com responsabilidades claras em cada papel.",
            landing_mock_security_label: "Seguranca",
            landing_mock_security: "Acesso protegido por papel e controle administrativo mantem a experiencia organizada e rastreavel.",
            landing_trust_label_1: "Clareza",
            landing_trust_value_1: "Fluxo visivel",
            landing_trust_text_1: "Cada reserva, solicitacao e mudanca de status aparece no painel correspondente.",
            landing_trust_label_2: "Coordenacao",
            landing_trust_value_2: "Menos friccao",
            landing_trust_text_2: "Os filtros reduzem passos desnecessarios ao escolher materia, tutor, idioma e horario.",
            landing_trust_label_3: "Gestao",
            landing_trust_value_3: "Controle por papel",
            landing_trust_text_3: "Estudantes, tutores e admins trabalham em vistas diferentes, mas conectadas.",
            landing_trust_label_4: "Suporte",
            landing_trust_value_4: "i18n manual",
            landing_trust_text_4: "A interface mantem chaves tecnicas estaveis enquanto adapta os textos visiveis ao idioma atual.",
            landing_features_title: "Beneficios pensados para transformar interesse em acao",
            landing_features_desc: "Cada bloco responde a uma objecao comum: tempo, clareza, confianca e acompanhamento.",
            landing_feature_card_1_title: "Filtros avancados",
            landing_feature_card_1_desc: "Encontre tutorias por buscador, idioma, faculdade, data e hora sem revisar listas interminaveis.",
            landing_feature_card_2_title: "Horario semanal claro",
            landing_feature_card_2_desc: "Veja reservas e tutorias por hora para reduzir conflitos de agenda e esquecimentos.",
            landing_feature_card_3_title: "Controle real por papel",
            landing_feature_card_3_desc: "Gerencie usuarios, materias e atribuicoes enquanto cada papel ve apenas o necessario.",
            landing_how_title: "Como funciona",
            landing_how_desc: "Reduzimos a friccao com um caminho simples do registro ate a tutoria confirmada.",
            landing_how_step_1_title: "Explore ou publique",
            landing_how_step_1_desc: "Estudantes filtram materias ou tutorias publicadas enquanto tutores publicam novas disponibilidades.",
            landing_how_step_2_title: "Escolha a melhor coincidencia",
            landing_how_step_2_desc: "Idioma, faculdade, tutor e data ficam claros antes de confirmar qualquer acao.",
            landing_how_step_3_title: "Acompanhe tudo pelo horario",
            landing_how_step_3_desc: "Cada reserva ou solicitacao aparece em calendario e lista para facilitar o acompanhamento.",
            landing_testimonials_title: "Cenarios reais de uso do produto",
            landing_testimonials_desc: "Em vez de promessas vazias, mostramos como o PeerLink se encaixa em situacoes academicas concretas.",
            landing_testimonial_1_title: "Para estudantes que precisam decidir rapido",
            landing_testimonial_1_quote: "O fluxo de solicitar ou aceitar tutorias ajuda a comparar opcoes e agendar apoio sem mensagens dispersas.",
            landing_testimonial_2_title: "Para tutores que querem uma semana organizada",
            landing_testimonial_2_quote: "Calendario, solicitacoes e publicacao de tutorias ficam reunidos em um unico painel facil de acompanhar.",
            landing_testimonial_3_title: "Para administracao que precisa de rastreabilidade",
            landing_testimonial_3_quote: "Materias, usuarios, atribuicoes e acesso tecnico ficam separados para preservar ordem e controle.",
            landing_pricing_title: "Clareza total desde o inicio",
            landing_pricing_desc: "Mesmo quando a plataforma e gratuita, dizer isso com clareza reduz duvidas e acelera a decisao.",
            landing_pricing_badge: "Plano academico",
            landing_pricing_plan: "PeerLink Campus",
            landing_pricing_plan_desc: "Acesso para estudantes, tutores e administradores dentro do ambiente academico.",
            landing_pricing_price: "$0 / Gratis para sempre",
            landing_pricing_points: "Filtros, reservas, calendario semanal, paineis por papel e internacionalizacao integrada.",
            landing_faq_title: "Perguntas frequentes",
            landing_faq_desc: "Respondemos objecoes comuns sem sobrecarregar a pagina.",
            landing_faq_q1: "A plataforma tem custo?",
            landing_faq_a1: "Nao. O PeerLink se apresenta como uma solucao academica gratuita para coordenar tutorias no ambiente institucional.",
            landing_faq_q2: "Quem pode usar o PeerLink?",
            landing_faq_a2: "Estudantes, tutores e administradores. Cada papel entra em um painel diferente com acoes ajustadas ao seu fluxo.",
            landing_faq_q3: "O que acontece se uma tutoria mudar ou for cancelada?",
            landing_faq_a3: "O status aparece no fluxo de reservas e no horario semanal para que ambos os lados mantenham visibilidade imediata.",
            landing_faq_q4: "Posso filtrar por idioma ou faculdade?",
            landing_faq_a4: "Sim. Filtros por idioma, faculdade, data, hora e buscador fazem parte do fluxo principal para solicitar ou aceitar tutorias.",
            landing_final_cta_title: "Transforme interesse em uma tutoria agendada hoje",
            landing_final_cta_desc: "Se voce chegou ate aqui, ja viu como o PeerLink organiza busca, reserva e acompanhamento em um unico fluxo.",
            landing_footer_brand: "Uma experiencia academica mais clara para estudantes, tutores e administradores.",
            landing_footer_nav: "Navegacao",
            landing_footer_support: "Suporte e legal",
            landing_footer_contact: "Contato",
            landing_copyright: "© 2026 PeerLink. Todos os direitos reservados.",
            login_hero_title: "Entre no sistema de tutorias",
            login_hero_desc: "Acesse seu painel como administrador, tutor ou estudante.",
            login_heading: "Login",
            login_welcome_title: "Boas-vindas",
            login_welcome_desc: "Digite suas credenciais para continuar.",
            register_welcome_title: "Crie sua conta",
            register_welcome_desc: "Preencha seus dados para comecar a solicitar ou publicar tutorias.",
            login_value_note: "Acesse reservas, tutorias, materias e seu horario semanal em um unico lugar.",
            label_email: "E-mail",
            label_password: "Senha",
            validation_email_required: "Digite um e-mail valido para continuar.",
            validation_password_required: "Digite sua senha para continuar.",
            validation_name_required: "Digite seu nome completo.",
            validation_role_required: "Selecione o papel com o qual voce vai entrar.",
            validation_subject_required: "Digite o nome da materia.",
            validation_faculty_required: "Selecione a faculdade.",
            aria_peerlink_home: "Ir para o inicio do PeerLink",
            aria_landing_nav: "Navegacao principal do PeerLink",
            aria_go_features: "Ir para beneficios",
            aria_go_how: "Ir para como funciona",
            aria_go_testimonials: "Ir para cenarios",
            aria_go_pricing: "Ir para planos",
            aria_go_faq: "Ir para perguntas frequentes",
            aria_language_selector: "Seletor de idioma",
            aria_toggle_password: "Mostrar ou ocultar senha",
            aria_close_menu: "Fechar menu",
            aria_open_menu: "Abrir menu",
            aria_admin_nav: "Navegacao do administrador",
            aria_student_nav: "Navegacao do estudante",
            aria_tutor_nav: "Navegacao do tutor",
            login_remember_me: "Lembrar meus dados",
            login_forgot_password: "Esqueceu sua senha?",
            action_back_home: "Voltar",
            action_login: "Entrar",
            login_no_account: "Nao tem conta?",
            login_register_here: "Cadastre-se aqui",
            console_waiting: "Aguardando acao...",
            register_title: "PeerLink | Registro",
            register_heading: "Registro",
            register_subtitle: "Crie uma conta como estudante ou tutor.",
            register_value_note: "Use um e-mail que voce consulte com frequencia para acompanhar suas tutorias com facilidade.",
            label_full_name: "Nome completo",
            label_role: "Papel",
            action_register: "Registrar",
            register_has_account: "Ja tem conta?",
            register_back_login: "Voltar ao login",
            forgot_password_title: "Recuperar acesso",
            forgot_password_desc: "Nesta versao, a recuperacao e feita por suporte ou administracao. Deixe seu e-mail e mostraremos os proximos passos.",
            forgot_password_note: "Se voce ja estiver com uma sessao ativa, encerre-a antes para evitar confusoes ao trocar de conta.",
            forgot_password_action: "Solicitar ajuda",
            forgot_password_back: "Voltar ao login",
            profile_title: "PeerLink | Meu Perfil",
            profile_heading: "Meu perfil",
            profile_desc: "Consulte e atualize os dados da sua conta sem sair da plataforma.",
            profile_info_title: "Dados da conta",
            profile_password_title: "Alterar senha",
            profile_role_hint: "Seu papel e definido pela administracao e nao pode ser alterado neste painel.",
            profile_save: "Salvar alteracoes",
            profile_password_save: "Atualizar senha",
            profile_current_password: "Senha atual",
            profile_new_password: "Nova senha",
            help_title: "PeerLink | Ajuda",
            help_heading: "Central de ajuda",
            help_desc: "Encontre respostas rapidas sobre reservas, tutorias, contas e uso geral da plataforma.",
            legal_terms_title: "PeerLink | Termos",
            legal_privacy_title: "PeerLink | Privacidade",
            legal_terms_heading: "Termos e condicoes",
            legal_privacy_heading: "Politica de privacidade",
            legal_terms_intro: "PeerLink e uma plataforma academica de coordenacao de tutorias. Seu uso deve permanecer em fins educacionais e respeitar estudantes, tutores e administradores.",
            legal_terms_body_1: "Ao usar a plataforma, voce concorda em fornecer informacoes verdadeiras, proteger seu acesso e nao usar a aplicacao para finalidades alheias ao acompanhamento academico.",
            legal_terms_body_2: "Reservas, mudancas de status e tutorias publicadas continuam sujeitas as regras operacionais da instituicao e a disponibilidade de tutores e materias.",
            legal_privacy_intro: "PeerLink utiliza dados de conta, papel, materias e reservas apenas para gerir autenticacao, tutorias e paineis de trabalho.",
            legal_privacy_body_1: "As informacoes armazenadas se limitam ao necessario para operar a plataforma e mostrar a cada pessoa o conteudo correspondente ao seu papel.",
            legal_privacy_body_2: "As credenciais sao tratadas com seguranca e a aplicacao nao deve compartilhar dados com terceiros nao autorizados dentro do escopo atual do projeto.",
            legal_terms_badge: "Acordos e regras",
            legal_terms_section_usage: "Compromisso de uso",
            legal_terms_section_reservations: "Reservas e disponibilidade",
            legal_terms_notice: "Qualquer uso indevido ou automatizado da plataforma pode resultar na suspensao do acesso ao painel de reservas.",
            legal_terms_footer: "© 2026 PeerLink. Promovendo ordem e colaboracao academica.",
            legal_privacy_badge: "Protecao de dados",
            legal_privacy_section_data: "Uso da informacao",
            legal_privacy_section_credentials: "Gestao de credenciais",
            legal_privacy_notice: "Esta politica foi projetada para apoiar um ambiente academico seguro, confiavel e livre de distracoes.",
            legal_privacy_footer: "© 2026 PeerLink. Tornando a vida universitaria mais segura e conectada.",
            help_badge: "Suporte academico",
            help_unresolved_title: "Ainda precisa de ajuda?",
            help_footer: "© 2026 PeerLink. Tornando a vida universitaria mais simples.",
            error_403_title: "Acesso negado",
            error_404_title: "Pagina nao encontrada",
            error_500_title: "Ocorreu um erro",
            error_page_desc: "Voce pode voltar ao inicio, revisar sua sessao ou abrir a ajuda.",
            admin_title: "PeerLink | Painel Admin",
            admin_hero_title: "Gestao de Materias",
            admin_hero_desc: "Gerencie materias, atribuicoes de tutores e usuarios em secoes separadas.",
            admin_tab_subjects: "Materias",
            admin_tab_tutors: "Tutores",
            admin_tab_users: "Usuarios",
            admin_subject_create_tab: "Criar materia",
            admin_subject_search_tab: "Buscar materias",
            admin_subject_form: "Formulario da materia",
            admin_subject_name: "Nome da materia",
            admin_save_subject: "Adicionar / Salvar",
            admin_cancel_edit: "Cancelar edicao",
            admin_assign_title: "Atribuir tutor a materia",
            admin_assign_action: "Atribuir",
            admin_assign_desc: "Filtre tutores e materias para criar atribuicoes com mais rapidez.",
            admin_subject_table: "Tabela de materias",
            admin_assignments_table: "Atribuicoes tutor - materia",
            admin_users_title: "Usuarios",
            admin_footer: "© 2026 PeerLink. Painel de administracao.",
            admin_save_user: "Adicionar usuario",
            admin_open_swagger: "Abrir Swagger",
            admin_swagger_hint: "Documentacao protegida para administradores.",
            admin_search_subjects_title: "Buscar materias",
            admin_search_subjects_desc: "Use filtros, revise coincidencias e clique em uma materia para abrir seu menu de edicao ou exclusao.",
            admin_users_search_placeholder: "Buscar por nome, e-mail ou papel",
            action_refresh: "Atualizar",
            action_open_swagger: "Abrir Swagger",
            action_apply_filters: "Aplicar filtros",
            action_clear_filters: "Limpar filtros",
            action_search: "Buscar",
            action_clear: "Limpar",
            action_select: "Selecionar",
            action_remove: "Remover",
            action_hide: "Ocultar",
            action_undo: "Desfazer",
            action_accept: "Aceitar",
            action_send_request: "Enviar solicitacao",
            action_save_tutoring: "Salvar tutoria",
            action_confirm_reservation: "Confirmar reserva",
            action_go_home: "Ir ao inicio",
            state_editing: "Editando",
            state_match: "Coincidencia",
            action_prev_week: "Semana anterior",
            action_next_week: "Proxima semana",
            action_this_week: "Esta semana",
            console_title: "Console",
            console_technical_title: "Atividade tecnica",
            feedback_loaded: "As informacoes foram atualizadas com sucesso.",
            student_title: "PeerLink | Painel Estudante",
            student_panel_heading: "Painel Estudante",
            student_panel_desc: "Gerencie suas tutorias em dois fluxos: solicitar uma nova ou aceitar uma tutoria ja publicada por um tutor.",
            student_tab_tutoring: "Tutorias",
            student_tab_schedule: "Minhas materias e horario",
            student_subtab_request: "Solicitar",
            student_subtab_accept: "Aceitar",
            student_request_title: "Buscar materias para solicitar",
            student_request_desc: "Filtre por buscador e faculdade. Ao selecionar uma coincidencia voce podera escolher tutor, idioma e data/hora.",
            student_request_matches: "Coincidencias de materias",
            student_request_selected: "Solicitacao selecionada",
            student_request_empty: "Selecione uma materia para abrir o formulario de solicitacao.",
            student_request_hint: "Primeiro filtre, depois escolha uma materia e por fim complete tutor, idioma e data.",
            student_accept_title: "Aceitar tutorias publicadas",
            student_accept_desc: "Use o filtro como ponto de partida e depois clique em uma coincidencia para reservá-la.",
            student_accept_selected: "Tutoria selecionada",
            student_accept_empty: "Selecione uma coincidencia para abrir seu menu de reserva.",
            student_accept_hint: "Escolha uma tutoria publicada para revisar os detalhes antes de confirmar.",
            student_schedule_filter_title: "Filtrar meu horario",
            student_schedule_filter_desc: "Busque suas reservas por texto, idioma, faculdade e data/hora; depois alterne entre calendario e lista.",
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
            tutor_panel_heading: "Painel Tutor",
            tutor_panel_desc: "Gerencie solicitacoes recebidas, publique novas tutorias e consulte seu horario semanal em calendario ou lista.",
            tutor_tab_requests: "Solicitacoes recebidas",
            tutor_tab_create: "Criar tutoria",
            tutor_tab_schedule: "Minhas materias e horario",
            tutor_requests_desc_extended: "Filtre por buscador, idioma, faculdade e data/hora. Voce tambem pode ocultar as ja respondidas ou desfazer uma resposta.",
            tutor_hide_answered: "Ocultar solicitacoes ja respondidas",
            tutor_create_title: "Buscar materia para publicar",
            tutor_create_desc: "Busque uma materia criada pelo admin e selecione a coincidencia para definir a data e hora da sua tutoria.",
            tutor_create_hint: "Selecione uma materia, revise idioma e faculdade, e depois defina a data e a hora da tutoria.",
            tutor_create_selected: "Tutoria para publicar",
            tutor_create_empty: "Selecione uma coincidencia para abrir o formulario de publicacao.",
            tutor_published_title: "Tutorias publicadas por mim",
            tutor_published_desc: "Daqui voce pode revisar ou remover as tutorias que ja publicou.",
            tutor_schedule_filter_title: "Filtrar meu horario",
            tutor_schedule_filter_desc: "Consulte suas tutorias reais por texto, idioma, faculdade e data/hora, em calendario semanal ou lista.",
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
            label_search: "Buscador",
            label_pick_week: "Semana para visualizar",
            validation_language_required: "Selecione o idioma da tutoria.",
            validation_tutor_required: "Selecione o tutor da tutoria.",
            validation_datetime_required: "Selecione a data e hora da tutoria.",
            loading_matches: "Buscando coincidencias...",
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
            action_download_report: "Baixar relatorio PDF",
            action_download_tutors_report: "Relatorio de tutores",
            action_download_users_report: "Relatorio de usuarios",
            no_actions: "Sem acoes",
            schedule_weekly: "Calendario semanal",
            schedule_list: "Lista simples",
            empty_subjects: "Nao ha materias cadastradas.",
            empty_assignments: "Nao ha relacoes cadastradas.",
            empty_users: "Nao ha usuarios cadastrados.",
            empty_available_tutoring: "Nao ha tutores disponiveis.",
            empty_my_reservations: "Voce nao tem tutorias para esses filtros.",
            empty_tutor_requests: "Voce nao tem tutorias para esses filtros.",
            empty_week_schedule: "Nao ha tutorias na semana selecionada.",
            empty_filtered_subjects: "Nao ha materias que coincidam com esse filtro.",
            empty_subject_tutors: "Nao ha tutores disponiveis para esta materia.",
            results_count: "{count} coincidencias",
            reservations_count: "{count} reservas",
            feedback_loaded_student_data: "Materias, tutores e horarios disponiveis foram atualizados.",
            feedback_request_created: "A solicitacao foi enviada e agora aparece no seu horario.",
            feedback_reservation_created: "A tutoria foi reservada com sucesso.",
            feedback_schedule_loaded: "Seu horario foi atualizado com as informacoes mais recentes.",
            feedback_login_error: "Nao foi possivel entrar com essas credenciais.",
            feedback_register_success: "A conta foi criada com sucesso. Agora voce ja pode entrar.",
            feedback_profile_loaded: "Os dados do seu perfil foram carregados.",
            feedback_profile_saved: "Os dados do seu perfil foram salvos com sucesso.",
            feedback_recovery_submitted: "Registramos sua solicitacao e o proximo passo aparece aqui.",
            feedback_loaded_tutor_data: "Solicitacoes, materias e horarios do tutor foram atualizados.",
            feedback_tutor_request_updated: "A solicitacao foi atualizada com sucesso.",
            feedback_tutor_offer_saved: "A tutoria foi publicada com sucesso.",
            feedback_tutor_offer_deleted: "A tutoria publicada foi removida.",
            feedback_loaded_admin_data: "Materias, atribuicoes e usuarios foram atualizados.",
            feedback_subject_saved: "A materia foi salva com sucesso.",
            feedback_subject_deleted: "A materia foi excluida.",
            feedback_assignment_saved: "A atribuicao tutor-materia foi salva com sucesso.",
            feedback_user_saved: "O usuario foi criado com sucesso.",
            feedback_user_deleted: "O usuario foi removido.",
            feedback_report_downloaded: "O relatorio PDF foi baixado com sucesso.",
            placeholder_subject_name: "Nome, idioma ou faculdade",
            placeholder_tutor_name: "Nome ou e-mail",
            placeholder_assignment_search: "Buscar atribuicoes",
            placeholder_user_search: "Buscar por nome, e-mail ou papel",
            placeholder_subject_or_tutor: "Buscar materia ou tutor",
            placeholder_subject_or_student: "Buscar estudante ou materia",
            placeholder_subject_only: "Buscar por nome da materia",
            placeholder_email: "Seu e-mail institucional ou de acesso",
            placeholder_login_email: "email@exemplo.com",
            placeholder_password: "Senha",
            placeholder_full_name: "Nome completo",
            status_ready: "ready",
            console_idle: "A atividade recente aparecera aqui.",
            password_updated: "Senha atualizada com sucesso.",
            contact_support_or_admin: "Entre em contato com o suporte ou a administracao para continuar.",
            role_all: "Todos",
            select_all_languages: "Todos os idiomas",
            select_all_faculties: "Todas as faculdades",
            calendar_hour: "Hora",
            calendar_week_of: "Semana de {date}",
            reservation_with_tutor: "{subject} com {name}",
            reservation_with_student: "{subject} com {name}",
            confirm_delete_subject: "Excluir materia?",
            confirm_delete_user: "Excluir usuario {name}?",
            redirecting_to_login: "Redirecionando para",
            help_contact_line: "Se ainda estiver bloqueada, compartilhe uma captura e a mensagem de erro com o suporte ou com o administrador.",
            help_section_account: "Conta e acesso",
            help_section_student: "Estudantes",
            help_section_tutor: "Tutores",
            help_section_admin: "Administracao",
            help_section_general: "Uso geral",
            help_faq_login_q: "Nao consigo entrar",
            help_faq_login_a: "Verifique e-mail, senha e se o navegador aceita o certificado local quando estiver em ambiente de desenvolvimento.",
            help_faq_student_q: "Como solicito uma tutoria",
            help_faq_student_a: "Em Tutorias voce pode usar Solicitar para propor data e tutor, ou Aceitar para reservar uma tutoria ja publicada.",
            help_faq_tutor_q: "Como publico uma tutoria",
            help_faq_tutor_a: "Em Criar tutoria, filtre a materia, selecione a coincidencia e defina data e hora.",
            help_faq_admin_q: "O que posso fazer como admin",
            help_faq_admin_a: "Voce pode criar materias, atribuir tutores, administrar usuarios e abrir o Swagger com permissoes protegidas.",
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

    function languageOptionsMarkup(asButtons = false) {
        if (asButtons) {
            return ["es", "en", "pt"].map((lang) => `
                <button class="peerlink-language-option" type="button" role="menuitem" data-lang="${lang}">${lang.toUpperCase()}</button>
            `).join("");
        }

        return `
            <option value="es">ES</option>
            <option value="en">EN</option>
            <option value="pt">PT</option>
        `;
    }

    function languageIconMarkup() {
        return `
            <svg class="peerlink-language-icon" aria-hidden="true" viewBox="0 0 16 16" focusable="false">
                <path d="M4.545 6.714 4.11 8H3l1.862-5h1.284L8 8H6.833l-.435-1.286H4.545Zm1.634-.736L5.5 3.956h-.049l-.679 2.022h1.407Z"/>
                <path d="M0 2a2 2 0 0 1 2-2h7a2 2 0 0 1 2 2v3h3a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2v-3H2a2 2 0 0 1-2-2V2Zm2-1a1 1 0 0 0-1 1v7a1 1 0 0 0 1 1h7a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H2Zm7.138 9.995c.193.301.402.583.63.846-.748.575-1.673 1.001-2.768 1.292.178.217.451.635.555.867 1.125-.359 2.08-.844 2.886-1.494.777.665 1.739 1.165 2.93 1.472.133-.254.414-.673.629-.89-1.125-.253-2.057-.694-2.82-1.284.681-.747 1.222-1.651 1.621-2.757H14V8h-3v1.047h.765a6.477 6.477 0 0 1-1.257 2.002 6.11 6.11 0 0 1-.872-1.415H8.5c.18.47.392.921.638 1.361Z"/>
            </svg>
        `;
    }

    function renderLanguageSelect(id = "languageSelect", extraClasses = "") {
        return `
            <div id="${id}" class="peerlink-language-select ${extraClasses}" data-language-dropdown>
                <button class="peerlink-language-toggle" type="button" aria-label="${t("aria_language_selector")}" aria-haspopup="menu" aria-expanded="false">
                    ${languageIconMarkup()}
                    <span data-language-current>${getLang().toUpperCase()}</span>
                </button>
                <div class="peerlink-language-menu" role="menu">
                    ${languageOptionsMarkup(true)}
                </div>
            </div>
        `;
    }

    function bindLanguageSelect(target) {
        const node = typeof target === "string" ? document.getElementById(target) : target;
        if (!node || node.dataset.boundLanguageSelect) {
            return;
        }

        if (node.tagName === "SELECT") {
            node.value = getLang();
            node.dataset.boundLanguageSelect = "true";
            node.addEventListener("change", () => setLang(node.value));
            return;
        }

        if (!node.querySelector(".peerlink-language-toggle")
                || !node.querySelector("[data-language-current]")
                || !node.querySelectorAll("[data-lang]").length) {
            node.innerHTML = `
                <button class="peerlink-language-toggle" type="button" aria-label="${t("aria_language_selector")}" aria-haspopup="menu" aria-expanded="false">
                    ${languageIconMarkup()}
                    <span data-language-current>${getLang().toUpperCase()}</span>
                </button>
                <div class="peerlink-language-menu" role="menu">
                    ${languageOptionsMarkup(true)}
                </div>
            `;
        }

        const toggle = node.querySelector(".peerlink-language-toggle");
        const current = node.querySelector("[data-language-current]");
        const options = node.querySelectorAll("[data-lang]");
        if (!toggle || !current || !options.length) {
            return;
        }

        current.textContent = getLang().toUpperCase();
        node.dataset.boundLanguageSelect = "true";
        options.forEach((option) => {
            const active = option.dataset.lang === getLang();
            option.classList.toggle("active", active);
            option.setAttribute("aria-current", active ? "true" : "false");
            option.addEventListener("click", () => {
                node.classList.remove("show");
                toggle.setAttribute("aria-expanded", "false");
                setLang(option.dataset.lang);
            });
        });

        toggle.addEventListener("click", (event) => {
            event.stopPropagation();
            const isOpen = node.classList.toggle("show");
            toggle.setAttribute("aria-expanded", String(isOpen));
        });

        document.addEventListener("click", () => {
            node.classList.remove("show");
            toggle.setAttribute("aria-expanded", "false");
        });
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

    function updateStoredProfile(data = {}) {
        if (data.correo) {
            localStorage.setItem(storageKeys.email, data.correo);
        }
        if (data.nombreCompleto) {
            localStorage.setItem(storageKeys.name, data.nombreCompleto);
        }
    }

    function clearAuth() {
        localStorage.removeItem(storageKeys.token);
        localStorage.removeItem(storageKeys.role);
        localStorage.removeItem(storageKeys.email);
        localStorage.removeItem(storageKeys.name);
        document.cookie = "AUTH_TOKEN=; path=/; Max-Age=0; Secure; SameSite=Lax";
    }

    function isLogoutTrigger(target) {
        return Boolean(target?.closest?.("[data-dashboard-logout], #dashboardLogoutBtn, #dashboardLogoutDirectBtn, #logoutBtn"));
    }

    function logoutToLogin() {
        clearAuth();
        window.location.href = withLang("/login.html");
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
            <nav class="navbar navbar-expand-lg border-bottom border-light-subtle nav-blur sticky-top" aria-label="PeerLink application navigation">
                <div class="container-xxl">
                    <a class="navbar-brand" href="${withLang(panelForRole(auth.role || "ADMIN"))}" aria-label="${t("brand")}">
                        <img src="/img/peerlink.svg" alt="${t("brand")}" class="brand-logo brand-logo-sm" width="200" height="50" fetchpriority="high">
                    </a>
                    <div class="d-flex align-items-center gap-3 flex-wrap">
                        <div class="d-flex align-items-center gap-2 flex-wrap">
                            <a class="btn btn-outline-success btn-sm" href="${withLang("/perfil.html")}">${t("navbar_profile")}</a>
                            <a class="btn btn-outline-secondary btn-sm" href="${withLang("/ayuda.html")}">${t("navbar_help")}</a>
                        </div>
                        ${renderLanguageSelect("navbarLanguageSelect")}
                        <span class="badge rounded-pill text-bg-light border">${escapeHtml(auth.name || auth.email || t("navbar_user_fallback"))} · ${escapeHtml(auth.role || t("navbar_no_role"))}</span>
                        <button id="logoutBtn" class="btn btn-outline-dark btn-sm" type="button">${t("navbar_logout")}</button>
                    </div>
                </div>
            </nav>
        `;

        container.querySelectorAll(".peerlink-language-select").forEach(bindLanguageSelect);

        const logoutBtn = document.getElementById("logoutBtn");
        if (logoutBtn) {
            logoutBtn.dataset.dashboardLogout = "true";
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
        root.querySelectorAll("[data-i18n-aria-label]").forEach((node) => {
            node.setAttribute("aria-label", t(node.dataset.i18nAriaLabel));
        });
        root.querySelectorAll("[data-i18n-title-attr]").forEach((node) => {
            node.setAttribute("title", t(node.dataset.i18nTitleAttr));
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

    async function downloadFile(path, filename, options = {}, requiresAuth = true) {
        const auth = getAuth();
        const headers = {
            ...(options.headers || {})
        };

        if (requiresAuth && auth.token) {
            headers.Authorization = `Bearer ${auth.token}`;
        }

        const response = await fetch(withLang(path), {
            ...options,
            headers
        });

        if (!response.ok) {
            if (response.status === 401) {
                clearAuth();
            }
            const raw = await response.text();
            throw raw ? safeJsonParse(raw) : { status: response.status, message: response.statusText };
        }

        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        link.remove();
        URL.revokeObjectURL(url);
    }

    function bindConsole(elementId) {
        const node = document.getElementById(elementId);
        if (node && !node.dataset.boundConsole) {
            node.textContent = JSON.stringify({
                status: t("status_ready"),
                message: t("console_idle")
            }, null, 2);
            node.dataset.boundConsole = "true";
        }

        function normalizeConsoleData(data) {
            if (!data || typeof data !== "object" || Array.isArray(data)) {
                return data;
            }

            const normalized = { ...data };

            if (normalized.status === "ready") {
                normalized.status = t("status_ready");
            }
            if (typeof normalized.message === "string") {
                normalized.message = t(normalized.message);
            }
            if (typeof normalized.nextStep === "string") {
                normalized.nextStep = t(normalized.nextStep);
            }

            return normalized;
        }

        return {
            print(data) {
                const node = document.getElementById(elementId);
                if (node) {
                    node.textContent = JSON.stringify(normalizeConsoleData(data), null, 2);
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

    function bindFeedback(elementId) {
        function render(kind, messageKeyOrText, fallbackText = "") {
            const node = document.getElementById(elementId);
            if (!node) {
                return;
            }

            const dict = translations[getLang()] || translations.es;
            const message = dict[messageKeyOrText]
                ? t(messageKeyOrText)
                : (messageKeyOrText || fallbackText);

            node.className = `alert alert-${kind} border shadow-sm mb-4`;
            node.textContent = message;
            node.classList.remove("hidden");
        }

        return {
            info(messageKeyOrText, fallbackText = "") {
                render("light", messageKeyOrText, fallbackText);
            },
            success(messageKeyOrText, fallbackText = "") {
                render("success", messageKeyOrText, fallbackText);
            },
            error(error) {
                const detail = error?.message || error?.error || error?.details?.exception || "Error";
                render("danger", detail, detail);
            },
            clear() {
                const node = document.getElementById(elementId);
                if (!node) {
                    return;
                }
                node.textContent = "";
                node.className = "hidden";
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

    function closeDashboardDropdowns(except = null) {
        document.querySelectorAll(".dropdown-menu.show").forEach((menu) => {
            if (menu === except) {
                return;
            }
            menu.classList.remove("show");
            menu.closest(".dropdown")?.querySelector("[data-bs-toggle='dropdown']")?.setAttribute("aria-expanded", "false");
        });
    }

    function openDashboardOffcanvas(selector) {
        const panel = document.querySelector(selector);
        if (!panel) {
            return;
        }
        panel.classList.add("show");
        panel.style.visibility = "visible";
        document.body.classList.add("overflow-hidden");

        if (!document.querySelector(".dashboard-offcanvas-backdrop")) {
            const backdrop = document.createElement("button");
            backdrop.type = "button";
            backdrop.className = "offcanvas-backdrop fade show dashboard-offcanvas-backdrop";
            backdrop.setAttribute("aria-label", "Cerrar menu");
            document.body.appendChild(backdrop);
        }
    }

    function closeDashboardOffcanvas(target = null) {
        const panel = target?.closest?.(".offcanvas.show") || document.querySelector(".offcanvas.show");
        if (panel) {
            panel.classList.remove("show");
            panel.style.visibility = "hidden";
        }
        document.querySelector(".dashboard-offcanvas-backdrop")?.remove();
        document.body.classList.remove("overflow-hidden");
    }

    document.addEventListener("click", (event) => {
        const dropdownToggle = event.target.closest("[data-bs-toggle='dropdown']");
        if (dropdownToggle) {
            event.preventDefault();
            const menu = dropdownToggle.closest(".dropdown")?.querySelector(".dropdown-menu");
            if (menu) {
                const willOpen = !menu.classList.contains("show");
                closeDashboardDropdowns(menu);
                menu.classList.toggle("show", willOpen);
                dropdownToggle.setAttribute("aria-expanded", String(willOpen));
            }
            return;
        }

        const offcanvasToggle = event.target.closest("[data-bs-toggle='offcanvas']");
        if (offcanvasToggle) {
            event.preventDefault();
            openDashboardOffcanvas(offcanvasToggle.dataset.bsTarget);
            return;
        }

        if (event.target.closest("[data-bs-dismiss='offcanvas']") || event.target.classList.contains("dashboard-offcanvas-backdrop")) {
            event.preventDefault();
            closeDashboardOffcanvas(event.target);
            return;
        }

        if (!isLogoutTrigger(event.target)) {
            closeDashboardDropdowns();
            return;
        }

        event.preventDefault();
        logoutToLogin();
    });

    document.addEventListener("DOMContentLoaded", () => {
        document.querySelectorAll(".peerlink-language-select").forEach(bindLanguageSelect);
    });

    return {
        api,
        applyI18n,
        bindConsole,
        downloadFile,
        bindFeedback,
        bindLanguageSelect,
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
        renderLanguageSelect,
        renderNavbar,
        saveAuth,
        updateStoredProfile,
        setLang,
        t,
        withLang
    };
})();
