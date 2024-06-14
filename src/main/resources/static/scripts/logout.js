function logout() {
    localStorage.removeItem('jwtToken');
    sessionStorage.clear();

    fetch('/auth/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            // Redirect to the login page or any other appropriate page
            window.location.href = '/mainPage';
        } else {
            console.error('Logout failed');
        }
    }).catch(error => {
        console.error('Error:', error);
    });
}