const SERVER_URL = 'http://localhost:7070/api/users';

// --- Helper: File to Base64 Converter ---
function convertFileToBase64(file) {
    return new Promise((resolve, reject) => {
        if (!file) {
            resolve(null);
            return;
        }
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
            // Strips the "data:mime/type;base64," prefix.
            const base64String = reader.result.split(',')[1];
            resolve(base64String);
        };
        reader.onerror = error => reject(error);
    });
}

// Check if user is authenticated and on the correct page
function checkAuth(requiredRole) {
    const user = JSON.parse(localStorage.getItem('currentUser'));

    if (!user) {
        // If we are not already on the login page, redirect
        if (!window.location.href.includes('Login&Register.html')) {
            window.location.href = 'Login&Register.html';
        }
        return;
    }

    if (requiredRole && user.role.toUpperCase() !== requiredRole.toUpperCase()) {
        // Redirect to correct dashboard if role mismatch
        if (user.role === 'admin') {
            window.location.href = 'admin_dashboard.html';
        } else if (user.role === 'student') {
            window.location.href = 'student_dashboard.html';
        }
        return;
    }

    // Update Sidebar UI with user data if elements exist
    const nameEl = document.getElementById('user-name');
    const avatarEl = document.getElementById('sidebar-avatar');
    if (nameEl) nameEl.textContent = `${user.firstName} ${user.lastName}`;
    if (avatarEl) avatarEl.textContent = (user.firstName || 'U').charAt(0).toUpperCase();
}

// Toggle between Login and Register views
function toggleAuthMode(mode) {
    const loginView = document.getElementById('login-view');
    const registerView = document.getElementById('register-view');

    if (mode === 'register') {
        loginView.classList.add('hidden');
        registerView.classList.remove('hidden');
    } else {
        registerView.classList.add('hidden');
        loginView.classList.remove('hidden');
    }
}

// --- Specific Register UI Logic (Toggling ID fields) ---
function toggleIdTypeFields(selectedType) {
    const nationalIdGroup = document.getElementById('national-id-front-group');
    const birthCertGroup = document.getElementById('birth-certificate-group');

    // Hide both first
    if (nationalIdGroup) nationalIdGroup.classList.add('hidden');
    if (birthCertGroup) birthCertGroup.classList.add('hidden');

    // Reset inputs
    if (document.getElementById('nationalIdFrontInput')) document.getElementById('nationalIdFrontInput').value = '';
    if (document.getElementById('birthCertificateInput')) document.getElementById('birthCertificateInput').value = '';

    // Show selected
    if (selectedType === 'NATIONAL_ID' && nationalIdGroup) {
        nationalIdGroup.classList.remove('hidden');
    } else if (selectedType === 'BIRTH_CERTIFICATE' && birthCertGroup) {
        birthCertGroup.classList.remove('hidden');
    }
}
// Expose to window for inline onclick in HTML
window.toggleIdTypeFields = toggleIdTypeFields;

// --- Image Preview Handler ---
function handleImagePreview(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('image-preview');
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.innerHTML = `<img src="${e.target.result}" alt="Profile" class="w-full h-full object-cover">`;
            document.getElementById('reg-image-url').value = e.target.result;
        };
        reader.readAsDataURL(file);
    } else {
        preview.innerHTML = 'Preview';
        document.getElementById('reg-image-url').value = '';
    }
}
window.handleImagePreview = handleImagePreview;

async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    try {
        const response = await fetch(`${SERVER_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email: email, password: password }),
        });

        if (!response.ok) {
            throw new Error('Invalid email or password.');
        }

        const user = await response.json();
        console.log('Login successful. User:', user);
        console.log('User Role:', user.role);

        if (user.accountStatus === 'SUSPENDED') {
            alert('Your account has been suspended. Please contact support.');
            return;
        }

        localStorage.setItem('currentUser', JSON.stringify(user));

        // REDIRECT based on role
        if (user.role && user.role.toUpperCase() === 'ADMIN') {
            console.log('Redirecting to Admin Dashboard');
            window.location.href = 'admin_dashboard.html';
        } else if (user.role && user.role.toUpperCase() === 'STUDENT') {
            console.log('Redirecting to Student Dashboard');
            window.location.href = 'student_dashboard.html';
        } else {
            // Default fallback
            console.log('Redirecting to Default Dashboard');
            window.location.href = 'student_dashboard.html';
        }

    } catch (error) {
        console.error('Login Error:', error);
        alert(error.message || 'Login failed.');
    }
}
window.handleLogin = handleLogin;

async function handleRegister(e) {
    e.preventDefault();

    // Collecting ALL fields from the detailed form
    const firstName = document.getElementById('reg-firstname').value;
    const lastName = document.getElementById('reg-lastname').value;
    const email = document.getElementById('reg-email').value;
    const phone = document.getElementById('reg-phone').value;
    const parentPhone = document.getElementById('reg-parent-phone').value;
    const nationalId = document.getElementById('reg-national-id').value;
    const level = document.getElementById('reg-level').value;
    const password = document.getElementById('reg-password').value;
    const confirmPassword = document.getElementById('reg-confirm-password').value;
    const role = document.getElementById('reg-role').value;
    const idType = document.getElementById('reg-id-type').value;

    // Files
    const profileFile = document.getElementById('reg-image-input').files[0];
    const nationalIdFrontFile = document.getElementById('nationalIdFrontInput').files[0];
    const birthCertificateFile = document.getElementById('birthCertificateInput').files[0];

    if (password !== confirmPassword) {
        alert('Passwords do not match');
        return;
    }

    try {
        // Convert files to Base64
        const userImageBase64 = await convertFileToBase64(profileFile);
        let nationalIdFrontBase64 = null;
        let birthCertificateBase64 = null;

        if (idType === 'NATIONAL_ID') {
            if (!nationalIdFrontFile) {
                alert('Please upload the National ID Front image.');
                return;
            }
            nationalIdFrontBase64 = await convertFileToBase64(nationalIdFrontFile);
        } else if (idType === 'BIRTH_CERTIFICATE') {
            if (!birthCertificateFile) {
                alert('Please upload the Birth Certificate image.');
                return;
            }
            birthCertificateBase64 = await convertFileToBase64(birthCertificateFile);
        }

        const payload = {
            firstName: firstName,
            lastName: lastName,
            email: email,
            phoneNumber: phone,
            password: password,
            role: role.toUpperCase(),
            userImage: userImageBase64,

            // Student fields
            nationalId: nationalId,
            parentNumber: parentPhone ? parseInt(parentPhone.replace(/\D/g, '')) : null,
            idType: idType,
            levelOfEducation: level,
            nationalIdFront: nationalIdFrontBase64,
            birthCertificate: birthCertificateBase64
        };

        const response = await fetch(`${SERVER_URL}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        });

        if (response.status === 409) {
            alert('An account with this email already exists. Please log in.');
            return;
        }

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || `Registration failed with status: ${response.status}`);
        }

        // Show success message and switch to login view
        alert('Account created successfully! Please log in.');
        toggleAuthMode('login');

        // Pre-fill email for convenience
        if (document.getElementById('login-email')) {
            document.getElementById('login-email').value = email;
        }

    } catch (error) {
        console.error('Registration Error:', error);
        alert(`Registration failed: ${error.message}`);
    }
}
window.handleRegister = handleRegister;

function handleLogout() {
    localStorage.removeItem('currentUser');
    window.location.href = 'Login&Register.html';
}
window.handleLogout = handleLogout;