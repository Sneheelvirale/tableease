document.addEventListener('DOMContentLoaded', () => {
    const bookingForm = document.getElementById('booking-form');
    const alertContainer = document.getElementById('alert-container');
    const tableIdInput = document.getElementById('tableId');

    const urlParams = new URLSearchParams(window.location.search);
    const selectedTableId = urlParams.get('tableId');
    if (selectedTableId) {
        tableIdInput.value = selectedTableId;
    }

    bookingForm.addEventListener('submit', (e) => {
        e.preventDefault();

        const dateVal = document.getElementById('bookingDate').value;
        const timeVal = document.getElementById('bookingTime').value;

        // ISO-8601 format: YYYY-MM-DDTHH:MM:SS (Spring Boot LocalDateTime compatibility)
        const formattedBookingTime = `${dateVal}T${timeVal}:00`;

        const bookingData = {
            tableId: parseInt(document.getElementById('tableId').value, 10),
            bookingTime: formattedBookingTime,
            customerName: document.getElementById('customerName').value,
            guestCount: parseInt(document.getElementById('guests').value, 10)
        };

        fetch('http://127.0.0.1:8080/bookings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(bookingData)
        })
        .then(async response => {
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || 'Validation failed on server side');
            }
            return response.json();
        })
        .then(data => {
            alertContainer.innerHTML = `
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Table booked successfully.
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            `;
            bookingForm.reset();
        })
        .catch(error => {
            alertContainer.innerHTML = `
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Booking Failed:</strong> ${error.message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            `;
            console.error('Booking Request Error:', error);
        });
    });
});