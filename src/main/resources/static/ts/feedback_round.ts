function updateGiverSelectionState(): void {
    const checkedCount = document.querySelectorAll<HTMLInputElement>('input[type="checkbox"][name="invites"]:checked').length;
    const submitButton = document.querySelector<HTMLButtonElement>('button[type="submit"]');
    if (submitButton) {
        submitButton.disabled = checkedCount < 5;
    }
}

function toggleAllGiverCheckboxes(select: boolean): void {
    const checkboxes = document.querySelectorAll<HTMLInputElement>('input[type="checkbox"][name="invites"]');
    checkboxes.forEach(checkbox => {
        checkbox.checked = select;
    });
    updateGiverSelectionState();
}

// Make it available for onclick="..."
(window as any).toggleAllGiverCheckboxes = toggleAllGiverCheckboxes;

function setupGiverCheckboxListeners(): void {
    const checkboxes = document.querySelectorAll<HTMLInputElement>('input[type="checkbox"][name="invites"]');
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateGiverSelectionState);
    });
}

function setupGiverSearch(): void {
    const searchInput = document.getElementById('giverSearch') as HTMLInputElement | null;
    const clearButton = document.getElementById('clearGiverSearch') as HTMLButtonElement | null;
    const rows = document.querySelectorAll<HTMLTableRowElement>('.giver-row');

    function filterGivers(): void {
        const query = searchInput?.value.toLowerCase() || '';
        rows.forEach(row => {
            const label = row.querySelector('label');
            const text = label?.innerText.toLowerCase() || '';
            const match = text.includes(query);
            row.style.display = match ? '' : 'none';
        });
    }

    searchInput?.addEventListener('input', filterGivers);
    clearButton?.addEventListener('click', () => {
        if (searchInput) {
            searchInput.value = '';
            filterGivers();
            searchInput.focus();
        }
    });

    filterGivers(); // Initial state
}

document.addEventListener('DOMContentLoaded', () => {
    setupGiverCheckboxListeners();
    setupGiverSearch();
    updateGiverSelectionState();
});