"use strict";
function confirmUserSync() {
    return confirm('Are you sure? This will contact external systems and update the user list.');
}
// Expose it to global window because your HTML calls it inline `onclick="return confirmSync()"`
window.confirmSync = confirmUserSync;
