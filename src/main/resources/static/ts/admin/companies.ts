document.addEventListener('DOMContentLoaded', () => {
    if (typeof bootstrap === 'undefined') {
        console.error('Bootstrap is not loaded.');
        return;
    }

    const deleteButtons = document.querySelectorAll<HTMLButtonElement>('.open-delete-modal');
    const confirmButton = document.getElementById('confirmDeleteButton') as HTMLButtonElement | null;
    const confirmModalElement = document.getElementById('confirmDeleteModal') as HTMLElement | null;
    const cancelButton = confirmModalElement?.querySelector('button.btn-secondary') as HTMLButtonElement | null;

    if (!confirmButton || !confirmModalElement || !cancelButton) {
        console.error('Delete modal elements missing.');
        return;
    }

    const confirmModal = new (bootstrap as any).Modal(confirmModalElement);
    let currentForm: HTMLFormElement | null = null;
    let lastClickedButton: HTMLButtonElement | null = null; // To remember which delete button opened the modal

    deleteButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            const btn = event.currentTarget as HTMLButtonElement;
            currentForm = btn.closest('form') as HTMLFormElement;
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
        lastClickedButton?.focus();
    });
});