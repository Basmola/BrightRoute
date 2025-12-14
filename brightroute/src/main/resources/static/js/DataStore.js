// Global State
const state = {
    users: [], // Cache for users if needed
    courses: [], // Store courses here
    currentUser: null
};

// Helper: Get Logged In User
function getLoggedInUser() {
    const userJson = localStorage.getItem('currentUser');
    if (!userJson) return null;
    try {
        state.currentUser = JSON.parse(userJson);
        return state.currentUser;
    } catch (e) {
        console.error("Error parsing user data", e);
        return null;
    }
}
window.getLoggedInUser = getLoggedInUser;

// Helper: Save User Session
function saveUserSession(user) {
    if (!user) return;
    localStorage.setItem('currentUser', JSON.stringify(user));
    state.currentUser = user;
}
window.saveUserSession = saveUserSession;

// Helper: Fetch Courses (Populates state.courses)
async function fetchAndStoreCourses() {
    try {
        const response = await fetch('http://localhost:7070/api/courses');
        if (!response.ok) throw new Error('Failed to fetch courses');
        const courses = await response.json();

        // Transform if necessary to match UI expectations (e.g. image, progress)
        state.courses = courses.map(c => ({
            id: c.courseId,
            title: c.courseTitle,
            instructor: c.courseInstructor,
            level: c.levelId,
            description: c.courseDescription,
            image: c.courseImage || 'https://via.placeholder.com/300x200?text=Course+Image', // Fallback
            progress: 0, // Mock progress
            status: 'In Progress', // Mock status
            lectures: [] // Will be populated when course detail is loaded or if API returns it
        }));

        console.log("Courses loaded into state:", state.courses);
        return state.courses;
    } catch (error) {
        console.error('Error loading courses:', error);
        return [];
    }
}
window.fetchAndStoreCourses = fetchAndStoreCourses;