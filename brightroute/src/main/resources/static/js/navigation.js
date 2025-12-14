// // =======================================================
// // 3. NAVIGATION AND ROUTING + SIDEBAR (navigation.js) - START
// // =======================================================

// function navigate(viewId, levelNumber = null) {
//     if (window.innerWidth < 1024) {
//         toggleSidebar(false);
//     }

//     localStorage.setItem('currentView', viewId);

//     document.querySelectorAll('.app-view').forEach(view => {
//         view.classList.add('hidden');
//     });
//     document.getElementById(viewId).classList.remove('hidden');

//     if (viewId === 'home-page-view') {
//         renderLevelCards();
//     } else if (viewId === 'dashboard-view') {
//         renderStudentDashboardCourses();
//     } else if (viewId === 'level-lectures-view') {
//         if (levelNumber) {
//             document.getElementById('current-level-title').textContent = `Lectures in Level ${levelNumber}`;
//         }
//         renderLevelLectures();
//     } else if (viewId === 'manage-users-view') {
//         renderUserManagement();
//     } else if (viewId === 'manage-courses-view') {
//         renderContentManagement();
//     } else if (viewId === 'profile-view') {
//         renderProfileView();
//     }

//     document.querySelectorAll('.nav-link').forEach(link => {
//         link.classList.remove('bg-gray-700', 'text-white');
//         link.classList.add('text-gray-300', 'hover:bg-gray-700');
//     });

//     const activeLink = document.querySelector(`.nav-link[onclick*='${viewId}']`);
//     if (activeLink) {
//         activeLink.classList.add('bg-gray-700', 'text-white');
//         activeLink.classList.remove('text-gray-300', 'hover:bg-gray-700');
//     }
// }
// window.navigate = navigate;

// function switchAuthView(viewId) {
//     document.getElementById('register-view').classList.add('hidden');
//     document.getElementById('login-view').classList.add('hidden');
//     document.getElementById(viewId).classList.remove('hidden');
// }
// window.switchAuthView = switchAuthView;

// // =======================================================
// // SIDEBAR TOGGLE FUNCTIONS
// // =======================================================

// function toggleSidebar(forceClose = false) {
//     const sidebar = document.getElementById("portal-sidebar");
//     const backdrop = document.getElementById("sidebar-backdrop");

//     if (!sidebar || !backdrop) return;

//     if (forceClose) {
//         sidebar.classList.add("hidden");
//         backdrop.classList.add("hidden");
//         backdrop.classList.remove("opacity-50");
//         return;
//     }

//     const isHidden = sidebar.classList.contains("hidden");

//     if (isHidden) {
//         sidebar.classList.remove("hidden");
//         backdrop.classList.remove("hidden");
//         backdrop.classList.add("opacity-50");
//     } else {
//         sidebar.classList.add("hidden");
//         backdrop.classList.add("hidden");
//         backdrop.classList.remove("opacity-50");
//     }
// }

// // Event listeners
// document.getElementById("sidebar-close-btn-in-sidebar")?.addEventListener("click", () => toggleSidebar(true));
// document.getElementById("sidebar-backdrop")?.addEventListener("click", () => toggleSidebar(true));

// window.toggleSidebar = toggleSidebar;

// // =======================================================
// // 3. NAVIGATION AND ROUTING + SIDEBAR (navigation.js) - END
// // =======================================================



function navigate(viewId) {
    // 1. Hide all views in the current HTML file
    document.querySelectorAll('.view-section').forEach(el => el.classList.add('hidden'));

    // 2. Show target view
    const target = document.getElementById(viewId);
    if (target) {
        target.classList.remove('hidden');

        // Trigger specific render functions based on View ID
        if (viewId === 'student-dashboard-view' && typeof renderStudentDashboard === 'function') {
            renderStudentDashboard();
        }
        else if (viewId === 'my-courses-view' && typeof renderMyCourses === 'function') {
            renderMyCourses();
        }
        else if (viewId === 'profile-view' && typeof renderProfileView === 'function') {
            renderProfileView();
        }
        else if (viewId === 'admin-dashboard-view' && typeof renderAdminDashboard === 'function') {
            renderAdminDashboard();
        }
        else if (viewId === 'manage-courses-view' && typeof renderCourseManagement === 'function') {
            renderCourseManagement();
        }
        else if (viewId === 'manage-users-view' && typeof renderUserManagement === 'function') {
            renderUserManagement();
        }

    } else {
        console.warn('View ID not found in this file:', viewId);
    }

    // 3. Update Sidebar Active State
    document.querySelectorAll('.nav-item').forEach(btn => {
        btn.classList.remove('bg-gray-800', 'text-white');
        btn.classList.add('text-gray-300');

        // Check if the button's onclick matches the current viewId
        if (btn.getAttribute('onclick') && btn.getAttribute('onclick').includes(viewId)) {
            btn.classList.add('bg-gray-800', 'text-white');
            btn.classList.remove('text-gray-300');
        }
    });

    // 4. Update Header Title
    const titles = {
        'student-dashboard-view': 'Student Dashboard',
        'admin-dashboard-view': 'Admin Overview',
        'manage-courses-view': 'Course Management',
        'manage-users-view': 'User Management',
        'my-courses-view': 'My Courses',
        'profile-view': 'My Profile',
        'course-detail-view': 'Course Detail'
    };
    const titleEl = document.getElementById('page-title');
    if (titleEl) titleEl.textContent = titles[viewId] || 'Dashboard';

    // 5. Close sidebar on mobile if open
    if (window.innerWidth < 1024) {
        const sidebar = document.getElementById('sidebar');
        const backdrop = document.getElementById('sidebar-backdrop');
        if (sidebar) sidebar.classList.add('-translate-x-full');
        if (backdrop) backdrop.classList.add('hidden');
    }
}