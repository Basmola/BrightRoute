
const API_BASE_URL = 'http://localhost:7070/api/courses';

// Global store for courses
let allCourses = [];

// Fetch courses from API
async function fetchCourses() {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error('Failed to fetch courses');
        allCourses = await response.json();
        renderCourseTable();
    } catch (error) {
        console.error('API Error:', error);
        alert('Failed to load courses. Please try again.');
    }
}

// Render the course table
function renderCourseTable(coursesToRender = allCourses) {
    const tableBody = document.getElementById('course-table-body');
    if (!tableBody) return;

    if (coursesToRender.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4" class="px-6 py-4 text-center text-gray-500">No courses found.</td></tr>';
        return;
    }

    tableBody.innerHTML = coursesToRender.map(course => `
        <tr>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${course.courseTitle}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${getLevelLabel(course.levelId)}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${course.courseNumberOfLectures || 0}</td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button onclick="openEditCourseModal(${course.courseId})" class="text-indigo-600 hover:text-indigo-900 mr-3">Edit</button>
                <button onclick="confirmDeleteCourse(${course.courseId})" class="text-red-600 hover:text-red-900">Delete</button>
            </td>
        </tr>
    `).join('');
}

// Helper to get level label
function getLevelLabel(levelId) {
    switch (levelId) {
        case 1: return 'Level 1 (Junior)';
        case 2: return 'Level 2 (Intermediate)';
        case 3: return 'Level 3 (Expert)';
        default: return 'Unknown Level';
    }
}

// Open Modal for Creating a New Course
function openCreateCourseModal() {
    showModal('Create New Course', null);
}

// Open Modal for Editing an Existing Course
function openEditCourseModal(courseId) {
    const course = allCourses.find(c => c.courseId === courseId);
    if (course) {
        showModal('Edit Course', course);
    }
}

// Show Modal with Form
function showModal(title, courseData) {
    const modal = document.getElementById('custom-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalMessage = document.getElementById('modal-message');
    const modalActions = document.getElementById('modal-actions');

    modalTitle.textContent = title;

    const isEditing = !!courseData;
    const courseId = isEditing ? courseData.courseId : '';
    const courseTitle = isEditing ? courseData.courseTitle : '';
    const courseInstructor = isEditing ? courseData.courseInstructor : '';
    const courseDescription = isEditing ? courseData.courseDescription : '';
    const levelId = isEditing ? courseData.levelId : 1;

    modalMessage.innerHTML = `
        <form id="course-form" class="space-y-4">
            <input type="hidden" id="course-id" value="${courseId}">
            <div>
                <label class="block text-sm font-medium text-gray-700">Course Title</label>
                <input type="text" id="course-title" value="${courseTitle}" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" required>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Instructor</label>
                <input type="text" id="course-instructor" value="${courseInstructor}" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" required>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Level</label>
                <select id="course-level" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2">
                    <option value="1" ${levelId === 1 ? 'selected' : ''}>Level 1 (Junior)</option>
                    <option value="2" ${levelId === 2 ? 'selected' : ''}>Level 2 (Intermediate)</option>
                    <option value="3" ${levelId === 3 ? 'selected' : ''}>Level 3 (Expert)</option>
                </select>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Description</label>
                <textarea id="course-description" rows="3" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2">${courseDescription}</textarea>
            </div>
        </form>
    `;

    modalActions.innerHTML = `
        <button onclick="closeModal()" class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition mr-2">Cancel</button>
        <button onclick="saveCourse()" class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-gray-800 transition">Save</button>
    `;

    modal.classList.remove('hidden');
}

// Close Modal
function closeModal() {
    document.getElementById('custom-modal').classList.add('hidden');
}

// Save Course (Create or Update)
async function saveCourse() {
    const id = document.getElementById('course-id').value;
    const title = document.getElementById('course-title').value;
    const instructor = document.getElementById('course-instructor').value;
    const level = parseInt(document.getElementById('course-level').value);
    const description = document.getElementById('course-description').value;

    if (!title || !instructor) {
        alert('Please fill in all required fields.');
        return;
    }

    const courseData = {
        courseTitle: title,
        courseInstructor: instructor,
        levelId: level,
        courseDescription: description
    };

    try {
        let response;
        if (id) {
            // Update
            response = await fetch(`${API_BASE_URL}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(courseData)
            });
        } else {
            // Create
            response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(courseData)
            });
        }

        if (response.ok) {
            closeModal();
            fetchCourses(); // Refresh table
        } else {
            const errorText = await response.text();
            alert('Error saving course: ' + errorText);
        }
    } catch (error) {
        console.error('API Error:', error);
        alert('Failed to save course.');
    }
}

// Confirm Delete
function confirmDeleteCourse(courseId) {
    if (confirm('Are you sure you want to delete this course?')) {
        deleteCourse(courseId);
    }
}

// Delete Course
async function deleteCourse(courseId) {
    try {
        const response = await fetch(`${API_BASE_URL}/${courseId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            fetchCourses(); // Refresh table
        } else {
            alert('Failed to delete course.');
        }
    } catch (error) {
        console.error('API Error:', error);
        alert('Error deleting course.');
    }
}

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    fetchCourses();

    // Search functionality
    const searchInput = document.getElementById('course-search-input');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase();
            const filteredCourses = allCourses.filter(course =>
                course.courseTitle.toLowerCase().includes(searchTerm) ||
                course.courseInstructor.toLowerCase().includes(searchTerm) ||
                (course.courseDescription && course.courseDescription.toLowerCase().includes(searchTerm))
            );
            renderCourseTable(filteredCourses);
        });
    }
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
        window.location.href = 'index.html';
        return;
    }

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
