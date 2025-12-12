// =======================================================
        // 4. AUTHENTICATION LOGIC (Conceptual: auth.js) - START
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
                currentPortalHeader.textContent = newRole === 'admin' ? 'Admin Dashboard' : `Student Dashboard`;
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

            localStorage.setItem('currentUser', JSON.stringify({
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

        function handleRegister() {
            if(event) event.preventDefault();
            const firstName = document.getElementById('register-first-name').value.trim();
            const lastName = document.getElementById('register-last-name').value.trim();
            const email = document.getElementById('register-email').value.toLowerCase().trim();
            const phoneNumber = document.getElementById('register-phone-number').value.trim();
            const password = document.getElementById('register-password').value;
            const confirmPassword = document.getElementById('confirm-password').value;
            const roleSelect = document.getElementById('register-role');
            const role = roleSelect.value;
            const imageUrl = document.getElementById('register-image-url').value;

            if (password !== confirmPassword) {
                showMessage('Registration Failed', 'Password and Confirm Password must match.');
                return;
            }
            if (!role) {
                showMessage('Registration Failed', 'Please select a role.');
                return;
            }

            if (registeredUsers.find(user => user.email === email)) {
                showMessage('Registration Failed', 'An account with this email already exists. Please log in.');
                return;
            }

            registeredUsers.push({
                id: registeredUsers.length + 10, // Mock ID assignment
                firstName: firstName,
                lastName: lastName,
                email: email,
                phoneNumber: phoneNumber,
                password: password,
                role: role,
                imageUrl: imageUrl
            });

            showMessage('Registration Successful', `Account created for ${email}! Please log in with your credentials.`);
            switchAuthView('login-view');
        }
        window.handleRegister = handleRegister;

        function handleLogin() {
            if(event) event.preventDefault();
            const email = document.getElementById('login-email').value.toLowerCase().trim();
            const password = document.getElementById('login-password').value;

            const user = registeredUsers.find(u => u.email === email && u.password === password);

            if (!user) {
                showMessage('Login Failed', 'Invalid email or password.');
                return;
            }

            localStorage.removeItem('currentView');
            localStorage.setItem('isSidebarOpen', 'true');

            finalizeLogin(user);
            showMessage('Login Successful', `Welcome back, ${user.firstName || user.email}!`);
        }
        window.handleLogin = handleLogin;

        function handleLogout() {
            localStorage.removeItem('currentUser');
            localStorage.removeItem('currentView');
            localStorage.setItem('isSidebarOpen', 'true');

            document.getElementById('portal-flow').classList.add('hidden');
            document.getElementById('auth-flow').classList.remove('hidden');
            showMessage('Logout Successful', 'You have been logged out. Please register or log in again.');
        }
        window.handleLogout = handleLogout;

        // =======================================================
// PERSISTENT LOGIN CHECK ON PAGE LOAD
// =======================================================

window.addEventListener('DOMContentLoaded', () => {
    const currentUser = JSON.parse(localStorage.getItem('currentUser'));

    if (currentUser) {
        // لو في مستخدم محفوظ في localStorage، نعيد تهيئة واجهة المستخدم
        finalizeLogin(currentUser);
    } else {
        // لو مفيش مستخدم، عرض صفحة تسجيل الدخول
        document.getElementById('portal-flow').classList.add('hidden');
        document.getElementById('auth-flow').classList.remove('hidden');
    }

    // إعادة حالة الـ sidebar من localStorage
    const persistedSidebarOpen = localStorage.getItem('isSidebarOpen') !== 'false';
    if (window.innerWidth >= 1024) {
        toggleSidebar(persistedSidebarOpen);
    } else {
        toggleSidebar(false);
    }
});

        // =======================================================
        // 4. AUTHENTICATION LOGIC (Conceptual: auth.js) - END
        // =======================================================
