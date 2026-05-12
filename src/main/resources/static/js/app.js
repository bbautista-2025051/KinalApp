
document.addEventListener('DOMContentLoaded', () => {

    const links = document.querySelectorAll('.sidebar-nav a');
    const path  = window.location.pathname;
    links.forEach(link => {
        const href = link.getAttribute('href');
        if (href && href !== '/' && path.startsWith(href)) {
            link.classList.add('active');
        } else if (href === '/' && path === '/') {
            link.classList.add('active');
        }
    });

    document.querySelectorAll('.btn-delete').forEach(btn => {
        btn.addEventListener('click', () => {
            const url  = btn.getAttribute('data-url');
            const name = btn.getAttribute('data-name') || 'este registro';
            openDeleteModal(url, name);
        });
    });

    initTableSearch();

    autoCloseAlerts();
});

function openDeleteModal(url, name) {
    const overlay    = document.getElementById('deleteModal');
    const nameEl     = document.getElementById('deleteItemName');
    const btnConfirm = document.getElementById('btnConfirmDelete');
    if (!overlay) return;
    if (nameEl) nameEl.textContent = name;
    btnConfirm.onclick = () => { window.location.href = url; };
    overlay.classList.add('open');
}

function closeModal(id) {
    const el = document.getElementById(id || 'deleteModal');
    if (el) el.classList.remove('open');
}

// Cerrar modal al hacer clic en el overlay (fondo oscuro)
document.addEventListener('click', e => {
    if (e.target && e.target.classList.contains('modal-overlay')) {
        e.target.classList.remove('open');
    }
});

function initTableSearch() {
    const searchInput = document.getElementById('tableSearch');
    if (!searchInput) return;

    searchInput.addEventListener('input', () => {
        const term  = searchInput.value.toLowerCase();
        const rows  = document.querySelectorAll('tbody tr');
        let visible = 0;

        rows.forEach(row => {
            const match = row.textContent.toLowerCase().includes(term);
            row.style.display = match ? '' : 'none';
            if (match) visible++;
        });

        const emptyEl = document.getElementById('emptySearch');
        if (emptyEl) emptyEl.style.display = visible === 0 ? 'block' : 'none';
    });
}

function autoCloseAlerts() {
    document.querySelectorAll('.alert[data-auto-close]').forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity .5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 3500);
    });
}
