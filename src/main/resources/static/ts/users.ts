document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('emailSearch') as HTMLInputElement | null;
    const clearButton = document.getElementById('clearSearch') as HTMLButtonElement | null;
    const rows = document.querySelectorAll<HTMLTableRowElement>('#userTableBody tr');

    function filterRows(): void {
        if (!searchInput) return;
        const query = searchInput.value.toLowerCase();
        rows.forEach(row => {
            const emailCell = row.querySelector('td');
            const email = emailCell ? emailCell.innerText.toLowerCase() : '';
            row.style.display = email.includes(query) ? '' : 'none';
        });
    }

    if (searchInput) {
        searchInput.addEventListener('input', filterRows);
    }

    if (clearButton) {
        clearButton.addEventListener('click', () => {
            if (searchInput) {
                searchInput.value = '';
                filterRows();
                searchInput.focus();
            }
        });
    }
});