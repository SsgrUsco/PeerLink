const recoveryFeedback = PeerlinkApp.bindFeedback("recoveryFeedback");

PeerlinkApp.applyI18n();
PeerlinkApp.bindLanguageSelect("recoveryLanguageSelect");

const backToLoginLink = document.getElementById("backToLoginLink");
if (backToLoginLink) {
    backToLoginLink.href = PeerlinkApp.withLang("/login.html");
}

document.getElementById("recoveryForm").addEventListener("submit", (event) => {
    event.preventDefault();
    recoveryFeedback.success("feedback_recovery_submitted");
});
