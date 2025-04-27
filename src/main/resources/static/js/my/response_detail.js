"use strict";
function updateResponseMessageSubmitState() {
    const responseMessageField = document.getElementById('responseMessage');
    const submitButton = document.querySelector('button[type="submit"]');
    if (!responseMessageField || !submitButton)
        return;
    const message = responseMessageField.value.trim();
    submitButton.disabled = message.length < 5;
}
document.addEventListener('DOMContentLoaded', () => {
    const responseMessageField = document.getElementById('responseMessage');
    const submitButton = document.querySelector('button[type="submit"]');
    if (submitButton) {
        submitButton.disabled = true; // Initial disabled
    }
    responseMessageField === null || responseMessageField === void 0 ? void 0 : responseMessageField.addEventListener('input', updateResponseMessageSubmitState);
});
