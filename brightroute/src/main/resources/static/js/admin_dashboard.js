document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    fetchDashboardData();
});

function checkAdminAuth() {
    const userJson = localStorage.getItem('currentUser');
    if (!userJson) {
        window.location.href = 'Login&Register.html';
        return;
    }

    const user = JSON.parse(userJson);
    // Case-insensitive role check
    if (user.role.toUpperCase() !== 'ADMIN') {
        window.location.href = 'index.html'; // Redirect non-admins to student portal
        return;
    }

    // Update UI with user info
    updateUserProfile(user);
}

function updateUserProfile(user) {
    const fullName = `${user.firstName} ${user.lastName}`;

    const nameElement = document.getElementById('user-full-name');
    if (nameElement) nameElement.textContent = fullName;

    const greetingElement = document.getElementById('nav-greeting-text');
    if (greetingElement) greetingElement.textContent = `Hello, ${user.firstName}!`;

    const initialsElement = document.getElementById('user-initials');
    if (initialsElement) {
        const initials = (user.firstName.charAt(0) + user.lastName.charAt(0)).toUpperCase();
        initialsElement.textContent = initials;
    }
}

async function fetchDashboardData() {
    try {
        // Fetch Courses
        const coursesResponse = await fetch('/api/courses');
        if (coursesResponse.ok) {
            const courses = await coursesResponse.json();
            updateDashboardCard('active-courses-count', courses.length);
        } else {
            console.error('Failed to fetch courses');
        }

        // Fetch Lectures
        const lecturesResponse = await fetch('/api/lectures');
        if (lecturesResponse.ok) {
            const lectures = await lecturesResponse.json();
            updateDashboardCard('total-lectures-count', lectures.length);
        } else {
            console.error('Failed to fetch lectures');
        }

        // Fetch Lecture Parts
        const partsResponse = await fetch('/api/lecture-parts');
        if (partsResponse.ok) {
            const parts = await partsResponse.json();
            updateDashboardCard('total-lecture-parts-count', parts.length);
        } else {
            console.error('Failed to fetch lecture parts');
        }

        // Fetch Users and Count Students
        const usersResponse = await fetch('/api/users');
        if (usersResponse.ok) {
            const users = await usersResponse.json();
            const studentCount = users.filter(user => user.role === 'STUDENT').length;
            updateDashboardCard('total-students-count', studentCount);
        } else {
            console.error('Failed to fetch users');
        }

    } catch (error) {
        console.error('Error fetching dashboard data:', error);
    }
}

function updateDashboardCard(elementId, count) {
    const element = document.getElementById(elementId);
    if (element) {
        element.textContent = count;
        // Simple animation effect could be added here
    }
}
