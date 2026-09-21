const API_BASE_URL = 'http://localhost:8080/auth';

//Helper function to show bootstrap alerts
function showAlert(message,type='danger'){
    const alertContainer = document.getElementById('alert-container');
    if(alertContainer){
        alertContainer.innerHTML = `
            <div class="alert alert-${type} alert-dismissible fade show" role ="alert">
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // 1. Dynamic Navbar State Handling
    const authLink = document.getElementById('navbar-auth-link');
    const myBookingLink = document.getElementById('my-booking-link');
    const token = localStorage.getItem('token');

    if (token) {
        if (authLink) {
            authLink.textContent = 'Logout';
            authLink.href = '#';
            authLink.classList.add('text-danger');
            authLink.addEventListener('click', (e) => {
                e.preventDefault();
                if(confirm('Are you sure you want to logout?')){
                    localStorage.removeItem('token');
                    window.location.href = 'login.html';
                }
            });
        }
        if (myBookingLink) { 
            myBookingLink.style.display = 'block';
        }
    } else {
        if (authLink) {
            authLink.textContent = 'Login';
            authLink.href = 'login.html';
            authLink.classList.remove('text-danger');
        }
        if (myBookingLink) {
            myBookingLink.style.display = 'none';
        }
    }

// Handle User Registration
const registerForm = document.getElementById('register-form');
if(registerForm){
    registerForm.addEventListener('submit',async(e)=>{
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        try{
            const response = await fetch(`${API_BASE_URL}/register`,{
                method:'POST',
                headers:{
                    'Content-Type':'application/json'
                },
                body : JSON.stringify({ username, password })
            });
            if(response.ok){
                showAlert('Registration successful! Redirecting to login...','success');
                setTimeout(()=>{
                    window.location.href = 'login.html';
                },1500);
            }else{
                let errorMessage = 'Registration failed.';
                const rawTest = await response.text().catch(()=>'');
                if(rawTest){
                    try{
                        const errorData = JSON.parse(rawTest);
                        errorMessage = errorData.message || errorData.error || errorMessage;
                    }catch(_){
                        errorMessage = rawTest;
                    }
                }
                showAlert(errorMessage);
            }
        }catch(error){
            console.error('Error: ',error);
            showAlert('Server connection error.');
        }
    });
}

const loginForm = document.getElementById('login-form');
    if(loginForm){
        loginForm.addEventListener('submit',async(e)=>{
            e.preventDefault();

            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            try{
                const response = await fetch(`${API_BASE_URL}/login`,{
                    method: 'POST',
                    headers: {
                        'Content-Type':'application/json'  
                    },
                    body: JSON.stringify({username,password})
                });

                if(response.ok){
                    const data = await response.json();
                    const jwtToken = data.token || data.jwtToken || data.jwt;
                    if(jwtToken){
                        localStorage.setItem('token',jwtToken);
                        showAlert('Login successful! Redirecting...','success');
                        setTimeout(()=>{
                            window.location.href = 'booking.html';
                        },1500);
                    }else{
                        showAlert('Invalid token received from server.');
                    }
                }else{
                    showAlert('Invalid username or password.');
                }
            }catch(error){
                console.error('Error: ',error);
                showAlert('Server connection error.');
            }
        });
    }
});