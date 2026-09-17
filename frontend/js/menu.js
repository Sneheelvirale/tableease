document.addEventListener('DOMContentLoaded', () => {
    const menuContainer = document.getElementById('menu-container');
    const loadingSpinner = document.getElementById('loading');

    fetch('http://127.0.0.1:8080/menu')
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch menu items');
            }
            return response.json();
        })
        .then(menuItems => {
            loadingSpinner.style.display = 'none';

            if (menuItems.length === 0) {
                menuContainer.innerHTML = `<div class="col-12 text-center text-muted"><p>No menu items available right now.</p></div>`;
                return;
            }

            const cardsHTML = menuItems.map(item => `
                <div class="col-md-4 mb-4">
                    <div class="card h-100 shadow-sm menu-card">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title fw-bold">${item.name}</h5>
                            <p class="card-text text-muted flex-grow-1">Delicious and freshly prepared</p>
                            <p class="fs-4 fw-bold text-success mb-0">$${Number(item.price).toFixed(2)}</p>
                        </div>
                    </div>
                </div>
            `).join('');

            menuContainer.innerHTML = cardsHTML;
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            menuContainer.innerHTML = `
                <div class="col-12">
                    <div class="alert alert-danger text-center" role="alert">
                        Unable to load the menu. Please make sure the Spring Boot server is running.
                    </div>
                </div>
            `;
            console.error('Fetch Error:', error);
        });
});