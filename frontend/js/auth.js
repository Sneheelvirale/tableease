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
                body : JSON.stringify({
                    username:username,
                    password:password                                    
                })
            });
            if(response.ok){
                showAlert('Registration successful! Redirecting to login...','success');
                setTimeout(()=>{
                    window.location.href = 'login.html';
                },1500);
            }else{
                const errorText = await response.text();
                showAlert(errorText || 'Registration failed.');
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
                if(data.token){
                    localStorage.setItem('token',data.token);
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