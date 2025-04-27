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
    if (!openModalButton || !confirmButton || !syncForm || !confirmModalElement) {
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
});
