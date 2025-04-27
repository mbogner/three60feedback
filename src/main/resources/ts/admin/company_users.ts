document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('userFilter') as HTMLInputElement | null;
    const clearButton = document.getElementById('clearFilter') as HTMLButtonElement | null;
    const rows = document.querySelectorAll<HTMLTableRowElement>('tbody tr');

    function filterRows(): void {
        const query = input?.value.trim().toLowerCase() || '';
        rows.forEach(row => {
            const emailCell = row.querySelector('td');
            const email = emailCell?.textContent?.trim().toLowerCase() || '';
            row.style.display = (query.length < 3 || email.includes(query)) ? '' : 'none';
        });
    }

    input?.addEventListener('input', filterRows);

    clearButton?.addEventListener('click', () => {
        if (input) {
            input.value = '';
            filterRows();
            input.focus();
        }
    });

    filterRows(); // Initial state
});