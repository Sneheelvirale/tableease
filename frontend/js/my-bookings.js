document.addEventListener('DOMContentLoaded', () => {
    const bookingsList = document.getElementById('booking-list');
    const loadingSpinner = document.getElementById('loading');
    const bookingsTable = document.getElementById('bookings-table'); 
    const alertContainer = document.getElementById('alert-container');

    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    fetch('http://127.0.0.1:8080/bookings', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json' 
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to load bookings. Session might be expired.');
        }
        return response.json();
    })
    .then(bookings => {
        loadingSpinner.classList.add('d-none');

        if (!bookings || bookings.length === 0) {
            alertContainer.innerHTML = `
                <div class="alert alert-info text-center" role="alert">
                    You have no active table bookings yet. <a href="booking.html" class="alert-link">Book a table now!</a>
                </div>
            `;
            return;
        }

        // Table ko display karo
        bookingsTable.classList.remove('d-none');

        // Target ONLY <tbody> (bookingsList) instead of bookingsTable
        bookingsList.innerHTML = bookings.map(b => `
            <tr>
                <td class="fw-bold">#${b.id}</td>
                <td>${b.customerName || 'N/A'}</td>
                <td>Table ${b.tableId || (b.diningTable ? b.diningTable.id : (b.table ? b.table.id : 'N/A'))}</td>
                <td>${b.guestCount}</td>
                <td>${new Date(b.bookingTime).toLocaleString()}</td>
            </tr>
        `).join('');
    })
    .catch(error => {
        loadingSpinner.classList.add('d-none');
        alertContainer.innerHTML = `
            <div class="alert alert-danger text-center" role="alert">
                ${error.message}
            </div>
        `;
        console.error('Error fetching bookings:', error);
    });
});