export function setupDeleteModal(buttonSelector = '.open-delete-modal', modalId = 'confirmDeleteModal', confirmButtonId = 'confirmDeleteButton', modalNameId = 'modalName') {
    const deleteButtons = document.querySelectorAll(buttonSelector);
    const confirmModalElement = document.getElementById(modalId);
    const confirmButton = document.getElementById(confirmButtonId);
    const cancelButton = confirmModalElement === null || confirmModalElement === void 0 ? void 0 : confirmModalElement.querySelector('button.btn-secondary');
    const modalName = document.getElementById(modalNameId);
    if (!confirmModalElement || !confirmButton || !modalName || !cancelButton) {
        console.error('Modal elements missing.');
        return;
    }
    const confirmModal = new bootstrap.Modal(confirmModalElement);
    let currentForm = null;
    let lastClickedButton = null;
    deleteButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            const btn = event.currentTarget;
            currentForm = btn.closest('form');
            lastClickedButton = btn;
            const email = btn.getAttribute('data-name');
            modalName.textContent = email || 'Unknown';
            confirmModal.show();
        });
    });
    confirmButton.addEventListener('click', () => {
        if (currentForm) {
            currentForm.submit();
        }
    });
    cancelButton.addEventListener('click', () => {
        lastClickedButton === null || lastClickedButton === void 0 ? void 0 : lastClickedButton.focus();
    });
}
