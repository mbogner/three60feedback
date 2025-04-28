"use strict";
document.addEventListener('DOMContentLoaded', () => {
    showMessageToast();
});
function showMessageToast() {
    var _a, _b;
    const toastElement = document.getElementById('messageToast');
    const toastBody = document.getElementById('messageToastBody');
    const message = (_b = (_a = document.querySelector('[data-message]')) === null || _a === void 0 ? void 0 : _a.dataset.message) !== null && _b !== void 0 ? _b : '';
    if (toastElement && toastBody && message) {
        toastBody.textContent = message;
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
    }
}
