document.addEventListener('DOMContentLoaded', () => {
    const tablesContainer = document.getElementById('tables-container');
    const loadingSpinner = document.getElementById('loading');

    fetch('http://127.0.0.1:8080/tables') 
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch table data');
            }
            return response.json();
        })
        .then(tables => {
            loadingSpinner.style.display = 'none';

            if (tables.length === 0) {
                tablesContainer.innerHTML = `<div class="col-12 text-center text-muted"><p>No tables found.</p></div>`;
                return;
            }

            const cardsHTML = tables.map(table => {
                
                const isAvailable = table.status === 'AVAILABLE'; 
                const badgeClass = isAvailable ? 'bg-success' : 'bg-danger';
                const statusText = isAvailable ? 'Available' : 'Reserved';

                return `
                    <div class="col-md-4 mb-4">
                        <div class="card h-100 shadow-sm menu-card">
                            <div class="card-body d-flex flex-column justify-content-between">
                                <div>
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <h5 class="card-title fw-bold mb-0">Table #${table.tableNumber || table.id}</h5>
                                        <span class="badge ${badgeClass}">${statusText}</span>
                                    </div>
                                    <p class="card-text text-muted">Capacity: ${table.capacity || 4} Persons</p>
                                </div>
                                <div class="mt-3">
                                    <a href="booking.html?tableId=${table.id}" 
                                       class="btn btn-outline-primary w-100 ${!isAvailable ? 'disabled' : ''}">
                                       ${isAvailable ? 'Book This Table' : 'Already Reserved'}
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
            }).join('');

            tablesContainer.innerHTML = cardsHTML;
        })
        .catch(error => {
            loadingSpinner.style.display = 'none';
            tablesContainer.innerHTML = `
                <div class="col-12">
                    <div class="alert alert-danger text-center" role="alert">
                        Unable to load tables. Make sure your Backend server is running.
                    </div>
                </div>
            `;
            console.error('Tables Fetch Error:', error);
        });
});