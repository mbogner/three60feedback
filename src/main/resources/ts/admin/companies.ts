function confirmCompanyDelete(): boolean {
    return confirm('Are you sure? This will delete the company.');
}

// Expose it to global window, because it's called inline via onsubmit="return confirmDelete()"
(window as any).confirmDelete = confirmCompanyDelete;