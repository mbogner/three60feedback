document.addEventListener('DOMContentLoaded', () => {
    showMessageToast();
});

function showMessageToast(): void {
    const toastElement = document.getElementById('messageToast');
    const toastBody = document.getElementById('messageToastBody');

    const message = (document.querySelector('[data-message]') as HTMLElement | null)?.dataset.message ?? '';

    if (toastElement && toastBody && message) {
        toastBody.textContent = message;
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
    }
}