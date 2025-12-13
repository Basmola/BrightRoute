
const API_BASE_URL = 'http://localhost:7070/api/lectures';
const COURSE_API_URL = 'http://localhost:7070/api/courses';

let allLectures = [];
let allCourses = [];

// Fetch lectures and courses on load
document.addEventListener('DOMContentLoaded', async () => {
    await Promise.all([fetchLectures(), fetchCourses()]);
});

async function fetchLectures() {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error('Failed to fetch lectures');
        allLectures = await response.json();
        renderLectureTable();
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

function renderLectureTable() {
    const tableBody = document.getElementById('lecture-table-body');
    if (!tableBody) return;

    if (allLectures.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="4" class="px-6 py-4 text-center text-gray-500">No lectures found.</td></tr>';
        return;
    }

    tableBody.innerHTML = allLectures.map(lecture => {
        const courseName = lecture.course ? lecture.course.courseTitle : 'N/A';
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
    const modalMessage = document.getElementById('modal-message'); // We repurpose this for the form
    const modalActions = document.getElementById('modal-actions');

    modalTitle.textContent = title;

    const isEditing = !!lectureData;
    const lectureId = isEditing ? lectureData.id : '';
    const lectureTitle = isEditing ? lectureData.lectureTitle : '';
    const lectureDescription = isEditing ? lectureData.lectureDescription : '';
    const lectureOrder = isEditing ? lectureData.lectureOrderNumber : 1;
    const courseId = isEditing && lectureData.course ? lectureData.course.courseId : '';

    // Generate Course Options
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
            // Update
            response = await fetch(`${API_BASE_URL}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(lectureData)
            });
        } else {
            // Create
            response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(lectureData)
            });
        }

        if (response.ok) {
            closeModal();
            fetchLectures();
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
            fetchLectures();
        } else {
            alert('Failed to delete lecture.');
        }
    } catch (error) {
        console.error('API Error:', error);
        alert('Error deleting lecture.');
    }
}
