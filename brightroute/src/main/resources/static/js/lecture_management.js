
const API_BASE_URL = 'http://localhost:7070/api/lectures';
const COURSE_API_URL = 'http://localhost:7070/api/courses';

let allLectures = [];
let allCourses = [];

document.addEventListener('DOMContentLoaded', async () => {
    checkAdminAuth();
    await Promise.all([fetchLectures(), fetchCourses()]);
    renderLectureTable();

    const searchInput = document.getElementById('lecture-search-input');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase();
            const filteredLectures = allLectures.filter(lecture => {
                 
                let courseName = '';
                if (lecture.course && lecture.course.courseTitle) {
                    courseName = lecture.course.courseTitle;
                } else if (lecture.courseId) {
                    const course = allCourses.find(c => c.courseId === lecture.courseId);
                    if (course) courseName = course.courseTitle;
                }

                return lecture.lectureTitle.toLowerCase().includes(searchTerm) ||
                    courseName.toLowerCase().includes(searchTerm) ||
                    (lecture.lectureDescription && lecture.lectureDescription.toLowerCase().includes(searchTerm));
            });
            renderLectureTable(filteredLectures);
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

async function fetchLectures() {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error('Failed to fetch lectures');
        allLectures = await response.json();
    } catch (error) {
        console.error('Error fetching lectures:', error);
        alert('Failed to load lectures.');
    }
}

async function fetchCourses() {
    try {
        const response = await fetch(COURSE_API_URL);
        if (!response.ok) throw new Error('Failed to fetch courses');
        allCourses = await response.json();
    } catch (error) {
        console.error('Error fetching courses:', error);
    }
}

function renderLectureTable(lecturesToRender = allLectures) {
    const tableBody = document.getElementById('lecture-table-body');
    if (!tableBody) return;

    if (lecturesToRender.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4" class="px-6 py-4 text-center text-gray-500">No lectures found.</td></tr>';
        return;
    }

    tableBody.innerHTML = lecturesToRender.map(lecture => {
         
        let courseName = 'N/A';
        if (lecture.course && lecture.course.courseTitle) {
            courseName = lecture.course.courseTitle;
        } else if (lecture.courseId) {
            const course = allCourses.find(c => c.courseId === lecture.courseId);
            if (course) courseName = course.courseTitle;
        }
        const partsCount = lecture.parts ? lecture.parts.length : 0;

        return `
        <tr>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${lecture.lectureTitle}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${courseName}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${partsCount}</td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <a href="lecture_part_management.html?lectureId=${lecture.id}" class="text-green-600 hover:text-green-900 mr-3">Parts</a>
                <button onclick="openEditLectureModal(${lecture.id})" class="text-indigo-600 hover:text-indigo-900 mr-3">Edit</button>
                <button onclick="confirmDeleteLecture(${lecture.id})" class="text-red-600 hover:text-red-900">Delete</button>
            </td>
        </tr>
    `}).join('');
}

function openCreateLectureModal() {
    showModal('Create New Lecture', null);
}

function openEditLectureModal(lectureId) {
    const lecture = allLectures.find(l => l.id === lectureId);
    if (lecture) {
        showModal('Edit Lecture', lecture);
    }
}

function showModal(title, lectureData) {
    const modal = document.getElementById('custom-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalMessage = document.getElementById('modal-message');  
    const modalActions = document.getElementById('modal-actions');

    modalTitle.textContent = title;

    const isEditing = !!lectureData;
    const lectureId = isEditing ? lectureData.id : '';
    const lectureTitle = isEditing ? lectureData.lectureTitle : '';
    const lectureDescription = isEditing ? lectureData.lectureDescription : '';
    const lectureOrder = isEditing ? lectureData.lectureOrderNumber : 1;
     
    let courseId = '';
    if (isEditing) {
        if (lectureData.course && lectureData.course.courseId) {
            courseId = lectureData.course.courseId;
        } else if (lectureData.courseId) {
            courseId = lectureData.courseId;
        }
    }

    const courseOptions = allCourses.map(course =>
        `<option value="${course.courseId}" ${course.courseId === courseId ? 'selected' : ''}>${course.courseTitle}</option>`
    ).join('');

    modalMessage.innerHTML = `
        <form id="lecture-form" class="space-y-4">
            <input type="hidden" id="lecture-id" value="${lectureId}">
            <div>
                <label class="block text-sm font-medium text-gray-700">Lecture Title</label>
                <input type="text" id="lecture-title" value="${lectureTitle}" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" required>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Course</label>
                <select id="lecture-course" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" required>
                    <option value="">Select a Course</option>
                    ${courseOptions}
                </select>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Order Number</label>
                <input type="number" id="lecture-order" value="${lectureOrder}" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" required>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Description</label>
                <textarea id="lecture-description" rows="3" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2">${lectureDescription}</textarea>
            </div>
        </form>
    `;

    modalActions.innerHTML = `
        <button onclick="closeModal()" class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition mr-2">Cancel</button>
        <button onclick="saveLecture()" class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-gray-800 transition">Save</button>
    `;

    modal.classList.remove('hidden');
}

function closeModal() {
    document.getElementById('custom-modal').classList.add('hidden');
}

async function saveLecture() {
    const id = document.getElementById('lecture-id').value;
    const title = document.getElementById('lecture-title').value;
    const courseId = document.getElementById('lecture-course').value;
    const order = document.getElementById('lecture-order').value;
    const description = document.getElementById('lecture-description').value;

    if (!title || !courseId || !order) {
        alert('Please fill in all required fields.');
        return;
    }

    const lectureData = {
        lectureTitle: title,
        lectureDescription: description,
        lectureOrderNumber: parseInt(order),
        course: {
            courseId: parseInt(courseId)
        }
    };

    try {
        let response;
        if (id) {
             
            response = await fetch(`${API_BASE_URL}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(lectureData)
            });
        } else {
             
            response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(lectureData)
            });
        }

        if (response.ok) {
            closeModal();
            await fetchLectures();
            renderLectureTable();
        } else {
            const errorText = await response.text();
            alert('Error saving lecture: ' + errorText);
        }
    } catch (error) {
        console.error('API Error:', error);
        alert('Failed to save lecture.');
    }
}

function confirmDeleteLecture(lectureId) {
    if (confirm('Are you sure you want to delete this lecture?')) {
        deleteLecture(lectureId);
    }
}

async function deleteLecture(lectureId) {
    try {
        const response = await fetch(`${API_BASE_URL}/${lectureId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            await fetchLectures();
            renderLectureTable();
        } else {
            alert('Failed to delete lecture.');
        }
    } catch (error) {
        console.error('API Error:', error);
        alert('Error deleting lecture.');
    }
}
