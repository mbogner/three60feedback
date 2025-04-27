"use strict";
document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('userFilter');
    const clearButton = document.getElementById('clearFilter');
    const rows = document.querySelectorAll('tbody tr');
    function filterRows() {
        const query = (input === null || input === void 0 ? void 0 : input.value.trim().toLowerCase()) || '';
        rows.forEach(row => {
            var _a;
            const emailCell = row.querySelector('td');
            const email = ((_a = emailCell === null || emailCell === void 0 ? void 0 : emailCell.textContent) === null || _a === void 0 ? void 0 : _a.trim().toLowerCase()) || '';
            row.style.display = (query.length < 3 || email.includes(query)) ? '' : 'none';
        });
    }
    input === null || input === void 0 ? void 0 : input.addEventListener('input', filterRows);
    clearButton === null || clearButton === void 0 ? void 0 : clearButton.addEventListener('click', () => {
        if (input) {
            input.value = '';
            filterRows();
            input.focus();
        }
    });
    filterRows(); // Initial state
});
