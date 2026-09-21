document.addEventListener('DOMContentLoaded', () => {
    const bookingForm = document.getElementById('booking-form');
    const alertContainer = document.getElementById('alert-container');
    const tableIdInput = document.getElementById('tableId');
    const bookingDateInput = document.getElementById('bookingDate');
    const submitBtn = document.getElementById('submit-btn');


    const token = localStorage.getItem('token');
    if(!token){
        window.location.href = 'login.html';
        return;
    }

    const now = new Date();
    const localToday = new Date(now.getTime()-(now.getTimezoneOffset()*60000)).toISOString().split('T')[0];
    if(bookingDateInput){
        bookingDateInput.min = localToday;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const selectedTableId = urlParams.get('tableId');
    if (selectedTableId && tableIdInput) {
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
            guestCount: parseInt(document.getElementById('guests').value, 10)
        };

        if(submitBtn){
            submitBtn.disabled = true;
            submitBtn.innerHTML = 'Booking...';
        }

        fetch('http://127.0.0.1:8080/bookings', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
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

            setTimeout(() => {
                window.location.href = 'my-booking.html';
            }, 1500);
        })
        .catch(error => {
            if(submitBtn){
                submitBtn.disabled= false;
                submitBtn.innerText = 'Confirm Booking';
            }

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