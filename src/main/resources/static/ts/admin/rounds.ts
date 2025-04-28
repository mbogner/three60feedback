import {setupDeleteModal} from '../modal-utils.js';

document.addEventListener('DOMContentLoaded', () => {
    setupDeleteModal();
    setupRoundSearch();
});

function setupRoundSearch(): void {
    const searchInput = document.getElementById('roundSearch') as HTMLInputElement | null;
    const clearButton = document.getElementById('clearRoundSearch') as HTMLButtonElement | null;
    const rows = document.querySelectorAll<HTMLTableRowElement>('tbody tr');

    if (!searchInput || !clearButton) {
        console.warn('Round search input or clear button not found.');
        return;
    }

    const filterRounds = (): void => {
        const query = searchInput.value.toLowerCase();
        rows.forEach(row => {
            const receiverDiv = row.querySelector('.fw-semibold');
            const receiverText = receiverDiv ? receiverDiv.textContent?.toLowerCase() ?? '' : '';
            row.style.display = receiverText.includes(query) ? '' : 'none';
        });
    };

    searchInput.addEventListener('input', filterRounds);

    clearButton.addEventListener('click', () => {
        searchInput.value = '';
        filterRounds();
        searchInput.focus();
    });
}
