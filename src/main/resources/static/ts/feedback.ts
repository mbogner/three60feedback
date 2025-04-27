function updateFeedbackFormSubmitState(): void {
    const positive = (document.getElementById('positive') as HTMLTextAreaElement | null)?.value.trim() || '';
    const negative = (document.getElementById('negative') as HTMLTextAreaElement | null)?.value.trim() || '';
    const submitButton = document.querySelector<HTMLButtonElement>('button[type="submit"]');

    if (submitButton) {
        submitButton.disabled = !(positive && negative);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const positiveField = document.getElementById('positive') as HTMLTextAreaElement | null;
    const negativeField = document.getElementById('negative') as HTMLTextAreaElement | null;
    const submitButton = document.querySelector<HTMLButtonElement>('button[type="submit"]');

    if (submitButton) {
        submitButton.disabled = true;
    }

    positiveField?.addEventListener('input', updateFeedbackFormSubmitState);
    negativeField?.addEventListener('input', updateFeedbackFormSubmitState);
});