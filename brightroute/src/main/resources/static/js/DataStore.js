 
const state = {
    users: [],  
    courses: [],  
    subscribedCourses: [],  
    currentUser: null
};

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

function saveUserSession(user) {
    if (!user) return;
    localStorage.setItem('currentUser', JSON.stringify(user));
    state.currentUser = user;
}
window.saveUserSession = saveUserSession;

async function fetchAndStoreCourses() {
    try {
        const response = await fetch('http://localhost:7070/api/courses');
        if (!response.ok) throw new Error('Failed to fetch courses');
        const courses = await response.json();

        state.courses = courses.map(c => ({
            id: c.courseId,
            title: c.courseTitle,
            instructor: c.courseInstructor,
            level: c.levelId,
            description: c.courseDescription,
             
            image: c.courseImageCover ? `data:image/jpeg;base64,${c.courseImageCover}` : 'https://placehold.co/600x400?text=Course+Image',
            progress: 0,  
            status: 'In Progress',  
            lectures: c.lectures || []  
        }));

        console.log("Courses loaded into state:", state.courses);
        return state.courses;
    } catch (error) {
        console.error('Error loading courses:', error);
        return [];
    }
}
window.fetchAndStoreCourses = fetchAndStoreCourses;

async function fetchSubscribedCourses() {
    const user = getLoggedInUser();
    if (!user) {
        console.log('No user logged in, returning empty array');
        return [];
    }

    try {
        const url = `http: 
        console.log('Fetching subscribed courses from:', url);

        const response = await fetch(url);
        console.log('Response status:', response.status, response.statusText);

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Error response:', errorText);
            throw new Error(`Failed to fetch subscribed courses: ${response.status} ${response.statusText}`);
        }

        const courses = await response.json();
        console.log('Raw courses from API:', courses);

        state.subscribedCourses = courses.map(c => ({
            id: c.courseId,
            title: c.courseTitle,
            instructor: c.courseInstructor,
            level: c.levelId,
            description: c.courseDescription,
            image: c.courseImageCover ? `data:image/jpeg;base64,${c.courseImageCover}` : 'https://placehold.co/600x400?text=Course+Image',
            progress: 0,  
            status: 'In Progress',
            lectures: c.lectures || []
        }));

        console.log("Subscribed courses loaded:", state.subscribedCourses);
        return state.subscribedCourses;
    } catch (error) {
        console.error('Error loading subscribed courses:', error);
        return [];
    }
}
window.fetchSubscribedCourses = fetchSubscribedCourses;