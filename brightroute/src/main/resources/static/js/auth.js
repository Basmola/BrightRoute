// =======================================================
        // MOCK/HELPER FUNCTIONS (Needed for provided code to run)
        // =======================================================
        const SERVER_URL = 'http://localhost:7070/api/users'; // **UPDATE THIS TO YOUR REAL SERVER URL**

        // Mock data to allow local login when API is not available
        let registeredUsers = [
            // Mock Student
            { id: 1, firstName: 'Demo', lastName: 'Student', email: 'student@br.com', password: '123', role: 'student', level: 'level 2', phoneNumber: '123456789', imageUrl: null },
            // Mock Admin
            { id: 2, firstName: 'Demo', lastName: 'Admin', email: 'admin12@gmail.com', password: 'admin123', role: 'admin', level: 'N/A', phoneNumber: '987654321', imageUrl: null }
        ];

        function showMessage(title, message) {
            console.log(`[${title}]: ${message}`);
            alert(`${title}\n${message}`);
        }

        function updateProfileIcon(user) {
            console.log('Profile icon updated for:', user.firstName);
        }

        function updateSidebarUserInfo(user) {
            console.log('Sidebar user info updated for:', user.firstName);
        }

        function navigate(view) {
            console.log('Navigating to:', view);
            localStorage.setItem('currentView', view);
        }

        function toggleSidebar(isOpen) {
            console.log('Sidebar toggled:', isOpen ? 'Open' : 'Closed');
            localStorage.setItem('isSidebarOpen', isOpen);
        }

        function switchAuthView(viewId) {
            document.getElementById('register-view').classList.toggle('hidden', viewId !== 'register-view');
            document.getElementById('login-view').classList.toggle('hidden', viewId !== 'login-view');
        }

        // --- File to Base64 Converter ---
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
                reader.onerror = () => resolve(null); // Resolve null on error
            });
        }

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
        // CORE AUTH LOGIC FUNCTIONS (Provided by user)
        // =======================================================

        function setUserRole(newRole, userName, imageUrl) {
            const roleDisplay = document.getElementById('role-display');
            const currentPortalHeader = document.getElementById('current-portal-header');
            const currentUserRoleBadge = document.getElementById('current-user-role-badge');
            const navAdmin = document.getElementById('nav-admin');
            const navStudent = document.getElementById('nav-student');

            if (roleDisplay) roleDisplay.textContent = newRole === 'admin' ? 'Admin' : 'Student';

            if (navAdmin) navAdmin.classList.toggle('hidden', newRole !== 'admin');
            if (navStudent) navStudent.classList.toggle('hidden', newRole !== 'student');

            updateProfileIcon({ firstName: userName, imageUrl: imageUrl });

            if (currentPortalHeader) {
                currentPortalHeader.textContent = newRole === 'admin' ? 'Admin Dashboard' : 'Student Dashboard';
            }

            if (currentUserRoleBadge) {
                currentUserRoleBadge.textContent = newRole === 'admin' ? 'ADMIN' : 'STUDENT';
                currentUserRoleBadge.classList.toggle('bg-primary', newRole !== 'admin');
                currentUserRoleBadge.classList.toggle('bg-red-500', newRole === 'admin');
            }

            const toggleIcon = document.getElementById('sidebar-toggle-icon');
            if (toggleIcon) {
                toggleIcon.innerHTML = `<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>`;
            }
        }

        function finalizeLogin(user) {
            setUserRole(user.role, user.firstName, user.imageUrl);

            const authFlow = document.getElementById('auth-flow');
            const portalFlow = document.getElementById('portal-flow');
            if (authFlow) authFlow.classList.add('hidden');
            if (portalFlow) portalFlow.classList.remove('hidden');

            updateSidebarUserInfo(user);

            const lastView = localStorage.getItem('currentView');
            let defaultView = user.role === 'admin' ? 'admin-dashboard-view' : 'dashboard-view';

            if (lastView && lastView !== 'lecture-detail-view') {
                navigate(lastView);
            } else {
                navigate(defaultView);
            }

            // Save user data to localStorage
            localStorage.setItem('currentUser', JSON.stringify({
                // Ensure all fields expected by other functions are here, even if null/default
                id: user.id, // Important for future API calls
                email: user.email,
                firstName: user.firstName,
                lastName: user.lastName,
                role: user.role,
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
        // REGISTER HANDLER (MODIFIED FOR API)
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
            
            // File inputs (not fully implemented in HTML but declared for DTO)
            // const nationalIdFrontFile = document.getElementById('nationalIdFrontInput')?.files?.[0]; 
            // const birthCertificateFile = document.getElementById('birthCertificateInput')?.files?.[0]; 

            // 2. Client-side Validation
            if (password !== confirmPassword) {
                showMessage('Registration Failed', 'Password and Confirm Password must match.');
                return;
            }
            if (!role) {
                showMessage('Registration Failed', 'Please select a role.');
                return;
            }
            if (!level) {
                showMessage('Registration Failed', 'Please select a level.');
                return;
            }
            // Simple mock for phone number to Long conversion for DTO
            const parentNumberLong = parseInt(parentPhoneNumber.replace(/\D/g, ''));
            if (isNaN(parentNumberLong)) {
                showMessage('Registration Failed', 'Invalid Parent Phone Number.');
                return;
            }
            if (!nationalId) {
                showMessage('Registration Failed', 'National ID is required.');
                return;
            }

            // 3. API Payload (Including DTO-required fields)
            try {
                // You would convert files here if you implemented the file inputs
                // const nationalIdFrontBase64 = await convertFileToBase64(nationalIdFrontFile);
                // const birthCertificateBase64 = await convertFileToBase64(birthCertificateFile);

                const payload = {
                    firstName: firstName,
                    lastName: lastName,
                    email: email,
                    phoneNumber: phoneNumber,
                    password: password,
                    role: role.toUpperCase(), // Ensure it matches Java enum (STUDENT)
                    
                    // Student-specific fields (Required by DTO)
                    nationalId: nationalId,
                    parentNumber: parentNumberLong,
                    idType: "NATIONAL_ID", // Default value
                    levelOfEducation: level,
                    nationalIdFront: null, // Sending null for byte[] fields
                    birthCertificate: null
                };

                // 4. API Call
                const response = await fetch(`${SERVER_URL}/register`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(payload),
                });

                if (response.status === 409) { // Assuming 409 Conflict for existing email
                    showMessage('Registration Failed', 'An account with this email already exists. Please log in.');
                    return;
                }

                if (!response.ok) {
                    // Try to read a detailed error message if available
                    const errorDetails = await response.json().catch(() => ({ message: 'Server error occurred during registration.' }));
                    throw new Error(errorDetails.message || `Registration failed with status: ${response.status}`);
                }

                const registeredUser = await response.json();

                // Successful registration
                showMessage('Registration Successful', `Account created for ${registeredUser.email}! Please log in with your credentials.`);
                
                // OPTIONAL: Update mock data for local testing if needed
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
        // LOGIN HANDLER (MODIFIED FOR API)
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
                    // Assuming API returns 401 or similar on invalid credentials
                    throw new Error('Invalid email or password.');
                }

                const user = await response.json();

                // 2. Client-side Login Finalization
                localStorage.removeItem('currentView');
                localStorage.setItem('isSidebarOpen', 'true');
                finalizeLogin(user);
                showMessage('Login Successful', `Welcome back, ${user.firstName || user.email}!`);

            } catch (error) {
                // Fallback to mock data for local development if API call fails
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
        // LOGOUT HANDLER (Modified to use userId for potential API call)
        // =======================================================
        async function handleLogout() {
            const currentUser = JSON.parse(localStorage.getItem('currentUser'));
            
            try {
                if (currentUser && currentUser.id) {
                    // API Call to notify server of logout
                    await fetch(`${SERVER_URL}/logout?userId=${currentUser.id}`, {
                        method: 'POST'
                    });
                }
            } catch (error) {
                console.warn("Logout API call failed, continuing local logout:", error);
                // Continue with local cleanup even if API call fails
            }

            // Local Cleanup
            localStorage.removeItem('currentUser');
            localStorage.removeItem('currentView');
            localStorage.setItem('isSidebarOpen', 'true');

            document.getElementById('portal-flow').classList.add('hidden');
            document.getElementById('auth-flow').classList.remove('hidden');
            
            switchAuthView('login-view'); // Default back to login screen
            showMessage('Logout Successful', 'You have been logged out. Please register or log in again.');
        }
        window.handleLogout = handleLogout;


        // =======================================================
        // PERSISTENT LOGIN CHECK ON PAGE LOAD
        // =======================================================
        window.addEventListener('DOMContentLoaded', () => {
            const currentUser = JSON.parse(localStorage.getItem('currentUser'));

            if (currentUser) {
                // Re-initialize UI with persisted user data
                finalizeLogin(currentUser);
            } else {
                // Show login page by default
                document.getElementById('portal-flow').classList.add('hidden');
                document.getElementById('auth-flow').classList.remove('hidden');
                switchAuthView('login-view'); // Ensure the login view is shown first
            }

            // Restore sidebar state
            const persistedSidebarOpen = localStorage.getItem('isSidebarOpen') !== 'false';
            if (window.innerWidth >= 1024) {
                toggleSidebar(persistedSidebarOpen);
            } else {
                toggleSidebar(false);
            }
        });