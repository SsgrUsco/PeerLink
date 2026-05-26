const storageKeys = {
    token: "peerlink.token",
    role: "peerlink.rol",
    email: "peerlink.correo",
    name: "peerlink.nombreCompleto",
    lang: "peerlink.lang"
};

const i18n = {
    es: {
        login_title: "PeerLink | Login",
        action_back_home: "Volver",
        label_email: "Correo",
        label_password: "Contrasena",
        label_language: "Idioma",
        login_welcome_title: "Te damos la bienvenida",
        login_welcome_desc: "Ingresa tus credenciales para continuar.",
        validation_email_required: "Ingresa un correo valido para continuar.",
        validation_password_required: "Ingresa tu contrasena para continuar.",
        aria_language_selector: "Selector de idioma",
        aria_peerlink_home: "Ir al inicio de PeerLink",
        aria_toggle_password: "Mostrar u ocultar contrasena",
        placeholder_login_email: "correo@ejemplo.com",
        placeholder_password: "Contrasena",
        login_remember_me: "Recordar mis datos",
        login_forgot_password: "Olvidaste tu contrasena?",
        action_login: "Iniciar sesion",
        login_no_account: "No tienes una cuenta?",
        login_register_here: "Registrate aqui",
        feedback_login_error: "No fue posible iniciar sesion. Revisa tus credenciales."
    },
    en: {
        login_title: "PeerLink | Login",
        action_back_home: "Back",
        label_email: "Email",
        label_password: "Password",
        label_language: "Language",
        login_welcome_title: "Welcome back",
        login_welcome_desc: "Enter your credentials to continue.",
        validation_email_required: "Enter a valid email to continue.",
        validation_password_required: "Enter your password to continue.",
        aria_language_selector: "Language selector",
        aria_peerlink_home: "Go to PeerLink home",
        aria_toggle_password: "Show or hide password",
        placeholder_login_email: "email@example.com",
        placeholder_password: "Password",
        login_remember_me: "Remember me",
        login_forgot_password: "Forgot your password?",
        action_login: "Sign in",
        login_no_account: "Do not have an account?",
        login_register_here: "Register here",
        feedback_login_error: "Unable to sign in. Check your credentials."
    },
    pt: {
        login_title: "PeerLink | Login",
        action_back_home: "Voltar",
        label_email: "E-mail",
        label_password: "Senha",
        label_language: "Idioma",
        login_welcome_title: "Boas-vindas",
        login_welcome_desc: "Digite suas credenciais para continuar.",
        validation_email_required: "Digite um e-mail valido para continuar.",
        validation_password_required: "Digite sua senha para continuar.",
        aria_language_selector: "Seletor de idioma",
        aria_peerlink_home: "Ir para o inicio do PeerLink",
        aria_toggle_password: "Mostrar ou ocultar senha",
        placeholder_login_email: "email@exemplo.com",
        placeholder_password: "Senha",
        login_remember_me: "Lembrar meus dados",
        login_forgot_password: "Esqueceu sua senha?",
        action_login: "Entrar",
        login_no_account: "Nao tem uma conta?",
        login_register_here: "Cadastre-se aqui",
        feedback_login_error: "Nao foi possivel entrar. Verifique suas credenciais."
    }
};

function getLang() {
    return new URLSearchParams(window.location.search).get("lang")
        || localStorage.getItem(storageKeys.lang)
        || "es";
}

function t(key) {
    return i18n[getLang()]?.[key] || i18n.es[key] || key;
}

function withLang(path) {
    const url = new URL(path, window.location.origin);
    url.searchParams.set("lang", getLang());
    return url.pathname + url.search;
}

function panelForRole(role) {
    if (role === "ADMIN") return "/panel-admin.html";
    if (role === "TUTOR") return "/panel-tutor.html";
    if (role === "ESTUDIANTE") return "/panel-estudiante.html";
    return "/login.html";
}

function applyI18n() {
    document.documentElement.lang = getLang();
    document.title = t(document.body.dataset.i18nTitle || "login_title");
    document.querySelectorAll("[data-i18n]").forEach((node) => {
        node.textContent = t(node.dataset.i18n);
    });
    document.querySelectorAll("[data-i18n-placeholder]").forEach((node) => {
        node.placeholder = t(node.dataset.i18nPlaceholder);
    });
    document.querySelectorAll("[data-i18n-aria-label]").forEach((node) => {
        node.setAttribute("aria-label", t(node.dataset.i18nAriaLabel));
    });
}

function bindLanguageSelect(id) {
    const node = document.getElementById(id);
    const toggle = node?.querySelector(".peerlink-language-toggle");
    const current = node?.querySelector("[data-language-current]");
    const options = node?.querySelectorAll("[data-lang]");
    if (!node || !toggle || !current || !options?.length) return;

    current.textContent = getLang().toUpperCase();
    options.forEach((option) => {
        option.classList.toggle("active", option.dataset.lang === getLang());
        option.addEventListener("click", () => {
            localStorage.setItem(storageKeys.lang, option.dataset.lang);
            const url = new URL(window.location.href);
            url.searchParams.set("lang", option.dataset.lang);
            window.location.href = url.toString();
        });
    });

    toggle.addEventListener("click", (event) => {
        event.stopPropagation();
        node.classList.toggle("show");
        toggle.setAttribute("aria-expanded", String(node.classList.contains("show")));
    });

    document.addEventListener("click", () => {
        node.classList.remove("show");
        toggle.setAttribute("aria-expanded", "false");
    });
}

function saveAuth(payload) {
    localStorage.setItem(storageKeys.token, payload.token);
    localStorage.setItem(storageKeys.role, payload.rol);
    localStorage.setItem(storageKeys.email, payload.correo);
    localStorage.setItem(storageKeys.name, payload.nombreCompleto);
    document.cookie = `AUTH_TOKEN=${payload.token}; path=/; Secure; SameSite=Lax`;
}

function getAuth() {
    return {
        token: localStorage.getItem(storageKeys.token) || "",
        role: localStorage.getItem(storageKeys.role) || ""
    };
}

async function api(path, options = {}) {
    const response = await fetch(withLang(path), {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        }
    });
    const raw = await response.text();
    const data = raw ? JSON.parse(raw) : null;
    if (!response.ok) {
        throw data || { message: response.statusText };
    }
    return data;
}

applyI18n();
bindLanguageSelect("loginLanguageSelect");

const loginForm = document.getElementById("loginForm");
const loginAlert = document.getElementById("loginAlert");
const togglePasswordBtn = document.getElementById("togglePasswordBtn");
const togglePasswordIcon = document.getElementById("togglePasswordIcon");
const passwordInput = document.getElementById("password");
const submitButton = document.getElementById("loginSubmitBtn");
const submitSpinner = document.getElementById("loginSubmitSpinner");

document.getElementById("goRegisterLink").href = withLang("/registro.html");
document.getElementById("goRecoveryLink").href = withLang("/recuperar-password.html");

const auth = getAuth();
if (auth.token && auth.role) {
    window.location.href = withLang(panelForRole(auth.role));
}

togglePasswordBtn.addEventListener("click", () => {
    const reveal = passwordInput.type === "password";
    passwordInput.type = reveal ? "text" : "password";
    togglePasswordIcon.className = reveal ? "bi bi-eye-slash" : "bi bi-eye";
});

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    hideAlert();
    if (!loginForm.checkValidity()) {
        loginForm.classList.add("was-validated");
        return;
    }

    setLoading(true);
    try {
        const response = await api("/api/auth/login", {
            method: "POST",
            body: JSON.stringify({
                correo: document.getElementById("correo").value.trim(),
                password: passwordInput.value
            })
        });
        saveAuth(response);
        window.location.href = withLang(panelForRole(response.rol));
    } catch (error) {
        showAlert(error?.message || t("feedback_login_error"));
    } finally {
        setLoading(false);
    }
});

function setLoading(isLoading) {
    submitButton.disabled = isLoading;
    submitSpinner.classList.toggle("d-none", !isLoading);
}

function showAlert(message) {
    loginAlert.textContent = message;
    loginAlert.classList.remove("d-none");
}

function hideAlert() {
    loginAlert.textContent = "";
    loginAlert.classList.add("d-none");
}
