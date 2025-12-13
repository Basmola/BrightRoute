// =======================================================
// MOCK/HELPER FUNCTIONS (Required by the core logic)
// =======================================================
const SERVER_URL = 'http://localhost:7070/api/users'; // **UPDATE THIS TO YOUR REAL SERVER URL**

let registeredUsers = [
    // Mock Student
    { id: 1, firstName: 'Demo', lastName: 'Student', email: 'student@br.com', password: '123', role: 'student', level: 'level 2', phoneNumber: '123456789', imageUrl: null },
    // Mock Admin
    { id: 2, firstName: 'Demo', lastName: 'Admin', email: 'admin12@gmail.com', password: 'admin123', role: 'admin', level: 'N/A', phoneNumber: '987654321', imageUrl: null }
];

function showMessage(title, message) {
    // Function to display messages (Using the provided modal structure)
    const modal = document.getElementById('custom-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalMessage = document.getElementById('modal-message');
    const modalCloseBtn = document.getElementById('modal-close-btn');

    if (modal && modalTitle && modalMessage) {
        modalTitle.textContent = title;
        modalMessage.textContent = message;
        modal.classList.remove('hidden');

        modalCloseBtn.onclick = () => {
            modal.classList.add('hidden');
        };
    }
    console.log(`[${title}]: ${message}`);
}

function updateProfileIcon(user) {
    const initials = (user.firstName ? user.firstName[0] : 'U') + (user.lastName ? user.lastName[0] : '');
    document.getElementById('user-initials').textContent = initials;
    document.getElementById('nav-greeting-text').textContent = `Hello, ${user.firstName || 'User'}!`;
    // Note: In a real app, if user.imageUrl is a URL, you'd set a background image on nav-profile-icon
}

function updateSidebarUserInfo(user) {
    const fullName = `${user.firstName || ''} ${user.lastName || ''}`.trim() || 'User';
    document.getElementById('user-full-name').textContent = fullName;
}

function navigate(viewId) {
    // Function to handle switching between main portal views
    localStorage.setItem('currentView', viewId);
    document.querySelectorAll('.app-view').forEach(view => {
        view.classList.add('hidden');
    });
    const targetView = document.getElementById(viewId);
    if (targetView) {
        targetView.classList.remove('hidden');
    } else {
        console.error(`View with ID ${viewId} not found. Falling back to home-page-view.`);
        // We assume 'home-page-view' exists based on the HTML provided
        const homeView = document.getElementById('home-page-view');
        if (homeView) homeView.classList.remove('hidden');
    }
}

function toggleSidebar(shouldOpen) {
    const sidebar = document.getElementById('portal-sidebar');
    const backdrop = document.getElementById('sidebar-backdrop');
    const wrapper = document.querySelector('.main-content-wrapper');

    // Determine current state if not explicitly passed
    const isOpen = shouldOpen === undefined ? sidebar.classList.contains('translate-x-0') : shouldOpen;

    if (window.innerWidth < 1024) { // Mobile toggle behavior
        if (isOpen) {
            sidebar.classList.remove('translate-x-0');
            sidebar.classList.add('-translate-x-full');
            backdrop.classList.add('hidden');
            backdrop.classList.remove('opacity-50');
        } else {
            sidebar.classList.remove('-translate-x-full');
            sidebar.classList.add('translate-x-0');
            backdrop.classList.remove('hidden');
            backdrop.classList.add('opacity-50');
        }
    } else { // Desktop sticky behavior
        if (isOpen) {
            sidebar.style.transform = 'translateX(0)';
            wrapper.style.marginLeft = '256px'; 
        } else {
            sidebar.style.transform = 'translateX(-100%)';
            wrapper.style.marginLeft = '0px';
        }
    }
    localStorage.setItem('isSidebarOpen', isOpen);
}

function switchAuthView(viewId) {
    document.getElementById('register-view').classList.toggle('hidden', viewId !== 'register-view');
    document.getElementById('login-view').classList.toggle('hidden', viewId !== 'login-view');
}

// --- File to Base64 Converter (Async to handle file read) ---
function convertFileToBase64(file) {
    return new Promise((resolve) => {
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
        reader.onerror = () => resolve(null);
    });
}

// --- Conditional Field Toggler ---
window.toggleIdTypeFields = function(selectedType) {
    const nationalIdGroup = document.getElementById('national-id-front-group');
    const birthCertGroup = document.getElementById('birth-certificate-group');

    nationalIdGroup.classList.add('hidden');
    birthCertGroup.classList.add('hidden');

    // Reset file inputs when hiding (important to prevent submitting old file data)
    document.getElementById('nationalIdFrontInput').value = '';
    document.getElementById('birthCertificateInput').value = '';

    if (selectedType === 'NATIONAL_ID') {
        nationalIdGroup.classList.remove('hidden');
    } else if (selectedType === 'BIRTH_CERTIFICATE') {
        birthCertGroup.classList.remove('hidden');
    }
};

// --- Image Preview Handler (for the profile image input) ---
window.handleImageUpload = function(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('image-preview');
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.innerHTML = `<img src="${e.target.result}" alt="Profile" class="w-full h-full object-cover">`;
            document.getElementById('register-image-url').value = e.target.result;
        };
        reader.readAsDataURL(file);
    } else {
        preview.innerHTML = 'Preview';
        document.getElementById('register-image-url').value = '';
    }
}

// =======================================================
// CORE AUTH LOGIC FUNCTIONS
// =======================================================
function setUserRole(newRole, userName, imageUrl) {
    const roleDisplay = document.getElementById('role-display');
    const currentPortalHeader = document.getElementById('current-portal-header');
    const currentUserRoleBadge = document.getElementById('current-user-role-badge');
    const navAdmin = document.getElementById('nav-admin');
    const navStudent = document.getElementById('nav-student');

    // Ensure the role is uppercase for consistent checks against HTML IDs/data
    const standardizedRole = newRole.toUpperCase(); 

    if (roleDisplay) roleDisplay.textContent = standardizedRole === 'ADMIN' ? 'Admin' : 'Student';

    if (navAdmin) navAdmin.classList.toggle('hidden', standardizedRole !== 'ADMIN');
    if (navStudent) navStudent.classList.toggle('hidden', standardizedRole !== 'STUDENT');

    updateProfileIcon({ firstName: userName, imageUrl: imageUrl });

    if (currentPortalHeader) {
        currentPortalHeader.textContent = standardizedRole === 'ADMIN' ? 'Admin Dashboard' : 'Student Dashboard';
    }

    if (currentUserRoleBadge) {
        currentUserRoleBadge.textContent = standardizedRole;
        currentUserRoleBadge.classList.toggle('bg-primary', standardizedRole !== 'ADMIN');
        currentUserRoleBadge.classList.toggle('bg-red-500', standardizedRole === 'ADMIN');
    }

    const toggleIcon = document.getElementById('sidebar-toggle-icon');
    if (toggleIcon) {
        toggleIcon.innerHTML = `<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>`;
    }
}

function finalizeLogin(user) {
    // Standardize role to uppercase for internal checks
    const userRole = user.role.toUpperCase(); 

    setUserRole(user.role, user.firstName, user.imageUrl);

    const authFlow = document.getElementById('auth-flow');
    const portalFlow = document.getElementById('portal-flow');
    if (authFlow) authFlow.classList.add('hidden');
    if (portalFlow) portalFlow.classList.remove('hidden');

    updateSidebarUserInfo(user);

    const lastView = localStorage.getItem('currentView');
    
    // Core logic: Redirect based on the user's role
    let defaultView = userRole === 'ADMIN' ? 'admin-dashboard-view' : 'dashboard-view'; // 'dashboard-view' is the student/user page.

    if (lastView && lastView !== 'lecture-detail-view') {
        navigate(lastView);
    } else {
        navigate(defaultView);
    }

    // Save user data to localStorage
    localStorage.setItem('currentUser', JSON.stringify({
        id: user.id,
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName,
        role: user.role, // Keep original casing here for consistency with API/mock data
        phoneNumber: user.phoneNumber || '',
        imageUrl: user.imageUrl || null
    }));

    const persistedSidebarOpen = localStorage.getItem('isSidebarOpen') !== 'false';
    if (window.innerWidth >= 1024) {
        toggleSidebar(persistedSidebarOpen);
    } else {
        toggleSidebar(false);
    }
}

// =======================================================
// REGISTER HANDLER (API INTEGRATION)
// =======================================================
async function handleRegister(event) {
    if (event) event.preventDefault();

    // 1. Collect form data
    const firstName = document.getElementById('register-first-name').value.trim();
    const lastName = document.getElementById('register-last-name').value.trim();
    const email = document.getElementById('register-email').value.toLowerCase().trim();
    const phoneNumber = document.getElementById('register-phone-number').value.trim();
    const parentPhoneNumber = document.getElementById('register-parent-phone-number').value.trim();
    const password = document.getElementById('register-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const roleSelect = document.getElementById('register-role');
    const role = roleSelect.value;
    const levelSelect = document.getElementById('register-level');
    const level = levelSelect.value;
    const nationalId = document.getElementById('register-national-id').value.trim(); 
    const idType = document.getElementById('register-id-type').value; 

    // File input elements
    const profileFile = document.getElementById('register-image-input').files[0];
    const nationalIdFrontFile = document.getElementById('nationalIdFrontInput').files[0];
    const birthCertificateFile = document.getElementById('birthCertificateInput').files[0];

    // 2. Client-side Validation
    if (password !== confirmPassword) {
        showMessage('Registration Failed', 'Password and Confirm Password must match.');
        return;
    }
    if (!role || !level || !idType) {
        showMessage('Registration Failed', 'Please complete all required fields: Role, Level, and Identification Type.');
        return;
    }
    const parentNumberLong = parseInt(parentPhoneNumber.replace(/\D/g, ''));
    if (isNaN(parentNumberLong)) {
        showMessage('Registration Failed', 'Invalid Parent Phone Number format.');
        return;
    }
    if (!nationalId) {
        showMessage('Registration Failed', 'National ID is required.');
        return;
    }

    // 3. Conditional File Validation and Conversion (Awaited)
    showMessage('Processing...', 'Converting images and sending data to the server.');

    // User Profile Image (Optional field in DTO)
    const userImageBase64 = await convertFileToBase64(profileFile);

    // Conditional Document Image (Required based on selection)
    let nationalIdFrontBase64 = null;
    let birthCertificateBase64 = null;

    if (idType === 'NATIONAL_ID') {
        if (!nationalIdFrontFile) {
            showMessage('Registration Failed', 'Please upload the National ID Front image.');
            return;
        }
        nationalIdFrontBase64 = await convertFileToBase64(nationalIdFrontFile);
    } else if (idType === 'BIRTH_CERTIFICATE') {
         if (!birthCertificateFile) {
            showMessage('Registration Failed', 'Please upload the Birth Certificate image.');
            return;
        }
        birthCertificateBase64 = await convertFileToBase64(birthCertificateFile);
    }

    // 4. API Payload
    try {
        const payload = {
            firstName: firstName,
            lastName: lastName,
            email: email,
            phoneNumber: phoneNumber,
            password: password,
            role: role.toUpperCase(), 
            
            // User Profile Image
            userImage: userImageBase64,
            
            // Student-specific fields
            nationalId: nationalId,
            parentNumber: parentNumberLong,
            idType: idType,
            levelOfEducation: level,
            nationalIdFront: nationalIdFrontBase64,
            birthCertificate: birthCertificateBase64
        };

        // 5. API Call
        const response = await fetch(`${SERVER_URL}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        });

        if (response.status === 409) {
            showMessage('Registration Failed', 'An account with this email already exists. Please log in.');
            return;
        }

        if (!response.ok) {
            const errorDetails = await response.json().catch(() => ({ message: 'Server error occurred during registration.' }));
            throw new Error(errorDetails.message || `Registration failed with status: ${response.status}`);
        }

        const registeredUser = await response.json();

        // Successful registration
        showMessage('Registration Successful', `Account created for ${registeredUser.email}! Please log in with your credentials.`);
        
        // Add to mock data for local testing fallback (optional)
        if (!registeredUsers.find(u => u.email === registeredUser.email)) {
            registeredUsers.push({ ...registeredUser, password: password });
        }
        
        switchAuthView('login-view');
        document.getElementById('login-email').value = registeredUser.email;

    } catch (error) {
        console.error('Registration Error:', error);
        showMessage('Registration Failed', `An error occurred: ${error.message}`);
    }
}
window.handleRegister = handleRegister;


// =======================================================
// LOGIN HANDLER (API INTEGRATION)
// =======================================================
async function handleLogin(event) {
    if (event) event.preventDefault();
    const email = document.getElementById('login-email').value.toLowerCase().trim();
    const password = document.getElementById('login-password').value;

    try {
        // 1. API Call
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

        // 2. Client-side Login Finalization
        localStorage.removeItem('currentView');
        localStorage.setItem('isSidebarOpen', 'true');
        finalizeLogin(user);
        showMessage('Login Successful', `Welcome back, ${user.firstName || user.email}!`);

    } catch (error) {
        // --- MOCK FALLBACK ---
        const mockUser = registeredUsers.find(u => u.email === email && u.password === password);
        
        if (mockUser) {
            localStorage.removeItem('currentView');
            localStorage.setItem('isSidebarOpen', 'true');
            finalizeLogin(mockUser);
            showMessage('Login Successful (Mock)', `Welcome back (Mock), ${mockUser.firstName || mockUser.email}!`);
        } else {
            console.error('Login Error:', error);
            showMessage('Login Failed', error.message || 'Invalid email or password.');
        }
    }
}
window.handleLogin = handleLogin;

// =======================================================
// LOGOUT HANDLER (API INTEGRATION)
// =======================================================
async function handleLogout() {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));
    
    try {
        if (currentUser && currentUser.id) {
            await fetch(`${SERVER_URL}/logout?userId=${currentUser.id}`, {
                method: 'POST'
            });
        }
    } catch (error) {
        console.warn("Logout API call failed, continuing local cleanup:", error);
    }

    // Local Cleanup
    localStorage.removeItem('currentUser');
    localStorage.removeItem('currentView');
    localStorage.setItem('isSidebarOpen', 'true');

    document.getElementById('portal-flow').classList.add('hidden');
    document.getElementById('auth-flow').classList.remove('hidden');
    
    switchAuthView('login-view');
    showMessage('Logout Successful', 'You have been logged out. Please register or log in again.');
}
window.handleLogout = handleLogout;


// =======================================================
// PERSISTENT LOGIN CHECK ON PAGE LOAD
// =======================================================
window.addEventListener('DOMContentLoaded', () => {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));

    if (currentUser) {
        finalizeLogin(currentUser);
    } else {
        document.getElementById('portal-flow').classList.add('hidden');
        document.getElementById('auth-flow').classList.remove('hidden');
        switchAuthView('login-view');
    }

    const persistedSidebarOpen = localStorage.getItem('isSidebarOpen') !== 'false';
    if (window.innerWidth >= 1024) {
        toggleSidebar(persistedSidebarOpen);
    } else {
        toggleSidebar(false);
    }
});