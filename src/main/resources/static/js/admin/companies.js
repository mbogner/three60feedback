"use strict";
document.addEventListener('DOMContentLoaded', () => {
    if (typeof bootstrap === 'undefined') {
        console.error('Bootstrap is not loaded.');
        return;
    }
    const deleteButtons = document.querySelectorAll('.open-delete-modal');
    const confirmButton = document.getElementById('confirmDeleteButton');
    const confirmModalElement = document.getElementById('confirmDeleteModal');
    if (!confirmButton || !confirmModalElement) {
        console.error('Delete modal elements missing.');
        return;
    }
    const confirmModal = new bootstrap.Modal(confirmModalElement);
    let currentForm = null;
    deleteButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            const btn = event.currentTarget;
            currentForm = btn.closest('form');
            confirmModal.show();
        });
    });
    confirmButton.addEventListener('click', () => {
        if (currentForm) {
            currentForm.submit();
        }
    });
});
