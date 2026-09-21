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

    if(bookingsList){
        bookingsList.addEventListener('click',(e) =>{
                const cancelBtn = e.target.closest('.cancel-btn');
                if(cancelBtn){
                    const bookingId = cancelBtn.getAttribute('data-id');
                    if(bookingId){
                        cancelBooking(bookingId);
                    }
                }
            });
    }

    function loadBookings(){
        if (loadingSpinner) loadingSpinner.classList.remove('d-none');
        fetch('http://127.0.0.1:8080/bookings', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json' 
            }
        })
        .then(async response => {
            if (!response.ok) {
                const errorData = await response.json().catch(()=>({}));
                throw new Error(errorData.message ||'Failed to load bookings. Session might be expired.');
            }
            return response.json();
        })
        .then(bookings => {
            if(loadingSpinner) loadingSpinner.classList.add('d-none');

            if (!bookings || bookings.length === 0) {
                if(bookingsTable) bookingsTable.classList.add('d-none');
                if(alertContainer){
                    alertContainer.innerHTML = `
                        <div class="alert alert-info text-center" role="alert">
                            You have no active table bookings yet. <a href="booking.html" class="alert-link">Book a table now!</a>
                        </div>
                    `;
                }
                return;
            }

            // Table ko display karo
            if(bookingsTable) bookingsTable.classList.remove('d-none');

            // Target ONLY <tbody> (bookingsList) instead of bookingsTable
            bookingsList.innerHTML = bookings.map(b => `
                <tr>
                    <td class="fw-bold">#${b.id}</td>
                    <td>${b.customerName || 'N/A'}</td>
                    <td>Table ${b.tableId || (b.diningTable ? b.diningTable.id : (b.table ? b.table.id : 'N/A'))}</td>
                    <td>${b.guestCount || b.guests || 'N/A'}</td>
                    <td>${new Date(b.bookingTime).toLocaleString()}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-danger cancel-btn" data-id="${b.id}">
                            Cancel
                        </button>
                    </td>
                </tr>
            `).join('');
            
        })
        .catch(error => {
            if(loadingSpinner) loadingSpinner.classList.add('d-none');
            if (bookingsTable) bookingsTable.classList.add('d-none');
            if(alertContainer){
                alertContainer.innerHTML = `
                    <div class="alert alert-danger text-center" role="alert">
                        ${error.message}
                    </div>
                `;
            }
            console.error('Error fetching bookings:', error);
        });
    }

    function cancelBooking(id){
        if(!confirm(`Are you sure you want to cancel booking #${id}`)){
            return;
        }
        fetch(`http://127.0.0.1:8080/bookings/${id}`,{
            method:'DELETE',
            headers:{
                'Authorization':`Bearer ${token}`,
                'Content-Type':'application/json' 
            }
        })
        .then(async response =>{
            if(!response.ok){
                const errorData = await response.json().catch(()=>({}));
                throw new Error(errorData.message ||'Failed to cancel booking');
            }
            if(alertContainer){
                alertContainer.innerHTML=`
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        Booking #${id} cancelled successfully.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                `;
            }
            loadBookings();
        })
        .catch(error =>{
            if(alertContainer){
                alertContainer.innerHTML =`
                    <div class="alert alert-danger text-center" role="alert">
                        ${error.message}
                    </div>
                `;
            }
            console.error('Error cancelling booking :',error);
        });
    }
    loadBookings();
});