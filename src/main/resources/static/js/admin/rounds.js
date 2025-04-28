import { setupDeleteModal } from '../modal-utils.js';
document.addEventListener('DOMContentLoaded', () => {
    setupDeleteModal();
    setupRoundSearch();
});
function setupRoundSearch() {
    const searchInput = document.getElementById('roundSearch');
    const clearButton = document.getElementById('clearRoundSearch');
    const rows = document.querySelectorAll('tbody tr');
    if (!searchInput || !clearButton) {
        console.warn('Round search input or clear button not found.');
        return;
    }
    const filterRounds = () => {
        const query = searchInput.value.toLowerCase();
        rows.forEach(row => {
            var _a, _b;
            const receiverDiv = row.querySelector('.fw-semibold');
            const receiverText = receiverDiv ? (_b = (_a = receiverDiv.textContent) === null || _a === void 0 ? void 0 : _a.toLowerCase()) !== null && _b !== void 0 ? _b : '' : '';
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
