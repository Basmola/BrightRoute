 

import { initializeApp } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-app.js";

window.initApp = function () {
    initSidebar();

    const user = getLoggedInUser();
    const authFlow = document.getElementById('auth-flow');
    const portalFlow = document.getElementById('portal-flow');
    const studentNav = document.getElementById('nav-student');
    const adminNav = document.getElementById('nav-admin');

    if (user) {
         
        authFlow.classList.add('hidden');
        portalFlow.classList.remove('hidden');

        document.getElementById('sidebar-name').textContent = `${user.firstName} ${user.lastName}`;
        document.getElementById('sidebar-role').textContent = user.role.charAt(0).toUpperCase() + user.role.slice(1);
        document.getElementById('sidebar-avatar').textContent = user.firstName.charAt(0).toUpperCase();

        if (user.role === 'student') {
            studentNav.classList.remove('hidden');
            adminNav.classList.add('hidden');
            navigate('student-dashboard-view');
        } else if (user.role === 'admin') {
            studentNav.classList.add('hidden');
            adminNav.classList.remove('hidden');
            navigate('admin-dashboard-view');
        }
    } else {
         
        authFlow.classList.remove('hidden');
        portalFlow.classList.add('hidden');
        renderAuth();
    }
};

document.addEventListener('DOMContentLoaded', initApp);