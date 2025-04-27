"use strict";
function updateGiverSelectionState() {
    const checkedCount = document.querySelectorAll('input[type="checkbox"][name="invites"]:checked').length;
    const submitButton = document.querySelector('button[type="submit"]');
    if (submitButton) {
        submitButton.disabled = checkedCount < 5;
    }
}
function toggleAllGiverCheckboxes(select) {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][name="invites"]');
    checkboxes.forEach(checkbox => {
        checkbox.checked = select;
    });
    updateGiverSelectionState();
}
// Make it available for onclick="..."
window.toggleAllGiverCheckboxes = toggleAllGiverCheckboxes;
function setupGiverCheckboxListeners() {
    const checkboxes = document.querySelectorAll('input[type="checkbox"][name="invites"]');
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', updateGiverSelectionState);
    });
}
function setupGiverSearch() {
    const searchInput = document.getElementById('giverSearch');
    const clearButton = document.getElementById('clearGiverSearch');
    const rows = document.querySelectorAll('.giver-row');
    function filterGivers() {
        const query = (searchInput === null || searchInput === void 0 ? void 0 : searchInput.value.toLowerCase()) || '';
        rows.forEach(row => {
            const label = row.querySelector('label');
            const text = (label === null || label === void 0 ? void 0 : label.innerText.toLowerCase()) || '';
            const match = text.includes(query);
            row.style.display = match ? '' : 'none';
        });
    }
    searchInput === null || searchInput === void 0 ? void 0 : searchInput.addEventListener('input', filterGivers);
    clearButton === null || clearButton === void 0 ? void 0 : clearButton.addEventListener('click', () => {
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
