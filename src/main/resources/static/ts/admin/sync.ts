document.addEventListener('DOMContentLoaded', () => {
    if (typeof bootstrap === 'undefined') {
        console.error('Bootstrap is not loaded.');
        return;
    }

    const openModalButton = document.getElementById('openSyncModal') as HTMLButtonElement | null;
    const confirmButton = document.getElementById('confirmSyncButton') as HTMLButtonElement | null;
    const syncForm = document.getElementById('syncForm') as HTMLFormElement | null;
    const confirmModalElement = document.getElementById('confirmSyncModal') as HTMLElement | null;
    const cancelButton = confirmModalElement?.querySelector('button.btn-secondary') as HTMLButtonElement | null;

    if (!openModalButton || !confirmButton || !syncForm || !confirmModalElement || !cancelButton) {
        console.error('One or more required elements not found.');
        return;
    }

    const confirmModal = new (bootstrap as any).Modal(confirmModalElement);

    openModalButton.addEventListener('click', () => {
        confirmModal.show();
    });

    confirmButton.addEventListener('click', () => {
        syncForm.submit();
    });

    // Accessibility fix: move focus BEFORE hiding, when Cancel is clicked
    cancelButton.addEventListener('click', () => {
        openModalButton?.focus();
    });
});