"use strict";
function updateFeedbackFormSubmitState() {
    var _a, _b;
    const positive = ((_a = document.getElementById('positive')) === null || _a === void 0 ? void 0 : _a.value.trim()) || '';
    const negative = ((_b = document.getElementById('negative')) === null || _b === void 0 ? void 0 : _b.value.trim()) || '';
    const submitButton = document.querySelector('button[type="submit"]');
    if (submitButton) {
        submitButton.disabled = !(positive && negative);
    }
}
document.addEventListener('DOMContentLoaded', () => {
    const positiveField = document.getElementById('positive');
    const negativeField = document.getElementById('negative');
    const submitButton = document.querySelector('button[type="submit"]');
    if (submitButton) {
        submitButton.disabled = true;
    }
    positiveField === null || positiveField === void 0 ? void 0 : positiveField.addEventListener('input', updateFeedbackFormSubmitState);
    negativeField === null || negativeField === void 0 ? void 0 : negativeField.addEventListener('input', updateFeedbackFormSubmitState);
});
