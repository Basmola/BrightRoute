// =======================================================
// 3. NAVIGATION AND ROUTING + SIDEBAR (navigation.js) - START
// =======================================================

function navigate(viewId, levelNumber = null) {
    if (window.innerWidth < 1024) {
        toggleSidebar(false);
    }

    localStorage.setItem('currentView', viewId);

    document.querySelectorAll('.app-view').forEach(view => {
        view.classList.add('hidden');
    });
    document.getElementById(viewId).classList.remove('hidden');

    if (viewId === 'home-page-view') {
        renderLevelCards();
    } else if (viewId === 'dashboard-view') {
        renderStudentDashboardCourses();
    } else if (viewId === 'level-lectures-view') {
        if (levelNumber) {
            document.getElementById('current-level-title').textContent = `Lectures in Level ${levelNumber}`;
        }
        renderLevelLectures();
    } else if (viewId === 'manage-users-view') {
        renderUserManagement();
    } else if (viewId === 'manage-courses-view') {
        renderContentManagement();
    } else if (viewId === 'profile-view') {
        renderProfileView();
    }

    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('bg-gray-700', 'text-white');
        link.classList.add('text-gray-300', 'hover:bg-gray-700');
    });

    const activeLink = document.querySelector(`.nav-link[onclick*='${viewId}']`);
    if (activeLink) {
        activeLink.classList.add('bg-gray-700', 'text-white');
        activeLink.classList.remove('text-gray-300', 'hover:bg-gray-700');
    }
}
window.navigate = navigate;

function switchAuthView(viewId) {
    document.getElementById('register-view').classList.add('hidden');
    document.getElementById('login-view').classList.add('hidden');
    document.getElementById(viewId).classList.remove('hidden');
}
window.switchAuthView = switchAuthView;

// =======================================================
// SIDEBAR TOGGLE FUNCTIONS
// =======================================================

function toggleSidebar(forceClose = false) {
    const sidebar = document.getElementById("portal-sidebar");
    const backdrop = document.getElementById("sidebar-backdrop");

    if (!sidebar || !backdrop) return;

    if (forceClose) {
        sidebar.classList.add("hidden");
        backdrop.classList.add("hidden");
        backdrop.classList.remove("opacity-50");
        return;
    }

    const isHidden = sidebar.classList.contains("hidden");

    if (isHidden) {
        sidebar.classList.remove("hidden");
        backdrop.classList.remove("hidden");
        backdrop.classList.add("opacity-50");
    } else {
        sidebar.classList.add("hidden");
        backdrop.classList.add("hidden");
        backdrop.classList.remove("opacity-50");
    }
}

// Event listeners
document.getElementById("sidebar-close-btn-in-sidebar")?.addEventListener("click", () => toggleSidebar(true));
document.getElementById("sidebar-backdrop")?.addEventListener("click", () => toggleSidebar(true));

window.toggleSidebar = toggleSidebar;

// =======================================================
// 3. NAVIGATION AND ROUTING + SIDEBAR (navigation.js) - END
// =======================================================
