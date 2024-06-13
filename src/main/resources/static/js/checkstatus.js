
window.addEventListener('online', () => {
  showToast('Estamos en linea otra vez! ', 'success');
});

window.addEventListener('offline', () => {
  showToast('Conexión perdida! Verifica tu Internet y refresca la plataforma.', 'danger');
});

if (!navigator.onLine) {
  showToast('Conexión perdida! Verifica tu Internet y refresca la plataforma.', 'danger');
}
