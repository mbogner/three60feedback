"use strict";
document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('emailSearch');
    const clearButton = document.getElementById('clearSearch');
    const rows = document.querySelectorAll('#userTableBody tr');
    function filterRows() {
        if (!searchInput)
            return;
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
