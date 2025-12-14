 

function navigate(viewId) {
     
    document.querySelectorAll('.view-section').forEach(el => el.classList.add('hidden'));

    const target = document.getElementById(viewId);
    if (target) {
        target.classList.remove('hidden');

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

    document.querySelectorAll('.nav-item').forEach(btn => {
        btn.classList.remove('bg-gray-800', 'text-white');
        btn.classList.add('text-gray-300');

        if (btn.getAttribute('onclick') && btn.getAttribute('onclick').includes(viewId)) {
            btn.classList.add('bg-gray-800', 'text-white');
            btn.classList.remove('text-gray-300');
        }
    });

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

    if (window.innerWidth < 1024) {
        const sidebar = document.getElementById('sidebar');
        const backdrop = document.getElementById('sidebar-backdrop');
        if (sidebar) sidebar.classList.add('-translate-x-full');
        if (backdrop) backdrop.classList.add('hidden');
    }
}