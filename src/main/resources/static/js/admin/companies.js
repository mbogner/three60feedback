"use strict";
function confirmCompanyDelete() {
    return confirm('Are you sure? This will delete the company.');
}
// Expose it to global window, because it's called inline via onsubmit="return confirmDelete()"
window.confirmDelete = confirmCompanyDelete;
