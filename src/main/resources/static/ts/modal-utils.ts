declare const bootstrap: any;

export function setupDeleteModal(
    buttonSelector: string = '.open-delete-modal',
    modalId: string = 'confirmDeleteModal',
    confirmButtonId: string = 'confirmDeleteButton',
    modalNameId: string = 'modalName'
) {
    const deleteButtons = document.querySelectorAll<HTMLButtonElement>(buttonSelector);
    const confirmModalElement = document.getElementById(modalId) as HTMLElement | null;
    const confirmButton = document.getElementById(confirmButtonId) as HTMLButtonElement | null;
    const cancelButton = confirmModalElement?.querySelector('button.btn-secondary') as HTMLButtonElement | null;
    const modalName = document.getElementById(modalNameId) as HTMLElement | null;

    if (!confirmModalElement || !confirmButton || !modalName || !cancelButton) {
        console.error('Modal elements missing.');
        return;
    }

    const confirmModal = new (bootstrap as any).Modal(confirmModalElement);
    let currentForm: HTMLFormElement | null = null;
    let lastClickedButton: HTMLButtonElement | null = null;

    deleteButtons.forEach(button => {
        button.addEventListener('click', (event) => {
            const btn = event.currentTarget as HTMLButtonElement;
            currentForm = btn.closest('form') as HTMLFormElement;
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
        lastClickedButton?.focus();
    });
}