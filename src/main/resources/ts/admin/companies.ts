document.addEventListener('DOMContentLoaded', () => {
    if (typeof bootstrap === 'undefined') {
        console.error('Bootstrap is not loaded.');
        return;
    }

    const deleteButtons = document.querySelectorAll<HTMLButtonElement>('.open-delete-modal');
    const confirmButton = document.getElementById('confirmDeleteButton') as HTMLButtonElement | null;
    const confirmModalElement = document.getElementById('confirmDeleteModal') as HTMLElement | null;

    if (!confirmButton || !confirmModalElement) {
        console.error('Delete modal elements missing.');
        return;
    }

    const confirmModal = new (bootstrap as any).Modal(confirmModalElement);
    let currentForm: HTMLFormElement | null = null;

    deleteButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            const btn = event.currentTarget as HTMLButtonElement;
            currentForm = btn.closest('form') as HTMLFormElement;
            confirmModal.show();
        });
    });

    confirmButton.addEventListener('click', () => {
        if (currentForm) {
            currentForm.submit();
        }
    });
});