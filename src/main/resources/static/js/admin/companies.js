"use strict";
document.addEventListener('DOMContentLoaded', () => {
    if (typeof bootstrap === 'undefined') {
        console.error('Bootstrap is not loaded.');
        return;
    }
    const deleteButtons = document.querySelectorAll('.open-delete-modal');
    const confirmButton = document.getElementById('confirmDeleteButton');
    const confirmModalElement = document.getElementById('confirmDeleteModal');
    const cancelButton = confirmModalElement === null || confirmModalElement === void 0 ? void 0 : confirmModalElement.querySelector('button.btn-secondary');
    if (!confirmButton || !confirmModalElement || !cancelButton) {
        console.error('Delete modal elements missing.');
        return;
    }
    const confirmModal = new bootstrap.Modal(confirmModalElement);
    let currentForm = null;
    let lastClickedButton = null; // To remember which delete button opened the modal
    deleteButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            const btn = event.currentTarget;
            currentForm = btn.closest('form');
            lastClickedButton = btn; // Save which button opened the modal
            confirmModal.show();
        });
    });
    confirmButton.addEventListener('click', () => {
        if (currentForm) {
            currentForm.submit();
        }
    });
    // Accessibility fix: move focus back BEFORE modal closes if Cancel is clicked
    cancelButton.addEventListener('click', () => {
        lastClickedButton === null || lastClickedButton === void 0 ? void 0 : lastClickedButton.focus();
    });
});
