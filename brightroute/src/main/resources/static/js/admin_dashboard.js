document.addEventListener('DOMContentLoaded', () => {
    fetchDashboardData();
});

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
