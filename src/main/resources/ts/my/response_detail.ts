function updateResponseMessageSubmitState(): void {
    const responseMessageField = document.getElementById('responseMessage') as HTMLTextAreaElement | null;
    const submitButton = document.querySelector<HTMLButtonElement>('button[type="submit"]');

    if (!responseMessageField || !submitButton) return;

    const message = responseMessageField.value.trim();
    submitButton.disabled = message.length < 5;
}

document.addEventListener('DOMContentLoaded', () => {
    const responseMessageField = document.getElementById('responseMessage') as HTMLTextAreaElement | null;
    const submitButton = document.querySelector<HTMLButtonElement>('button[type="submit"]');

    if (submitButton) {
        submitButton.disabled = true; // Initial disabled
    }

    responseMessageField?.addEventListener('input', updateResponseMessageSubmitState);
});