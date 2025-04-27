"use strict";
document.addEventListener('DOMContentLoaded', () => {
    if (typeof bootstrap === 'undefined') {
        console.error('Bootstrap is not loaded.');
        return;
    }
    const openModalButton = document.getElementById('openSyncModal');
    const confirmButton = document.getElementById('confirmSyncButton');
    const syncForm = document.getElementById('syncForm');
    const confirmModalElement = document.getElementById('confirmSyncModal');
    const cancelButton = confirmModalElement === null || confirmModalElement === void 0 ? void 0 : confirmModalElement.querySelector('button.btn-secondary');
    if (!openModalButton || !confirmButton || !syncForm || !confirmModalElement || !cancelButton) {
        console.error('One or more required elements not found.');
        return;
    }
    const confirmModal = new bootstrap.Modal(confirmModalElement);
    openModalButton.addEventListener('click', () => {
        confirmModal.show();
    });
    confirmButton.addEventListener('click', () => {
        syncForm.submit();
    });
    // Accessibility fix: move focus BEFORE hiding, when Cancel is clicked
    cancelButton.addEventListener('click', () => {
        openModalButton === null || openModalButton === void 0 ? void 0 : openModalButton.focus();
    });
});
