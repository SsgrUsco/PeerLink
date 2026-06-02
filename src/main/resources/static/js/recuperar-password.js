const recoveryFeedback = PeerlinkApp.bindFeedback("recoveryFeedback");

PeerlinkApp.applyI18n();
PeerlinkApp.bindLanguageSelect("recoveryLanguageSelect");

const backToLoginLink = document.getElementById("backToLoginLink");
if (backToLoginLink) {
    backToLoginLink.href = PeerlinkApp.withLang("/login.html");
}

const recoveryForm = document.getElementById("recoveryForm");
const recoverySubmitBtn = document.getElementById("recoverySubmitBtn");

recoveryForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    if (!recoveryForm.checkValidity()) {
        recoveryForm.classList.add("was-validated");
        return;
    }

    recoverySubmitBtn.disabled = true;
    try {
        await PeerlinkApp.api("/api/auth/password-recovery/support", {
            method: "POST",
            body: JSON.stringify({
                correo: document.getElementById("recoveryCorreo").value.trim()
            })
        });
        recoveryFeedback.success("feedback_recovery_submitted");
        recoveryForm.reset();
        recoveryForm.classList.remove("was-validated");
    } catch (error) {
        recoveryFeedback.error(error.message || "error_internal");
    } finally {
        recoverySubmitBtn.disabled = false;
    }
});
