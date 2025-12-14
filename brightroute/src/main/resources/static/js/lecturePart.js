 
const COURSE_API_URL = 'http://localhost:7070/api/courses';
const LECTURE_API_URL = 'http://localhost:7070/api/lectures';
const LECTURE_PART_API_URL = 'http://localhost:7070/api/lecture-parts';

let allCourses = [];
let allLectures = [];
let currentLectureParts = [];
let currentLectureId = null;

document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    fetchCourses();
    setupEventListeners();
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

function setupEventListeners() {
    const courseSelect = document.getElementById('course-select');
    const lectureSelect = document.getElementById('lecture-select');
    const partForm = document.getElementById('part-form');

    courseSelect.addEventListener('change', (e) => {
        const courseId = e.target.value;
        if (courseId) {
            fetchLectures(courseId);
            lectureSelect.disabled = false;
        } else {
            lectureSelect.innerHTML = '<option value="">-- Select a Lecture --</option>';
            lectureSelect.disabled = true;
            document.getElementById('parts-table-container').classList.add('hidden');
            document.getElementById('add-part-btn').disabled = true;
        }
    });

    lectureSelect.addEventListener('change', (e) => {
        const lectureId = e.target.value;
        if (lectureId) {
            currentLectureId = lectureId;
            fetchParts(lectureId);
            document.getElementById('add-part-btn').disabled = false;
        } else {
            currentLectureId = null;
            document.getElementById('parts-table-container').classList.add('hidden');
            document.getElementById('add-part-btn').disabled = true;
        }
    });

    partForm.addEventListener('submit', handlePartSubmit);
}

async function fetchCourses() {
    try {
        console.log('Fetching courses...');
        const response = await fetch(COURSE_API_URL);
        console.log('Courses response status:', response.status);
        if (!response.ok) throw new Error('Failed to fetch courses');
        allCourses = await response.json();
        console.log('Courses fetched:', allCourses);
        populateCourseSelect();
    } catch (error) {
        console.error('Error fetching courses:', error);
        showMessage('Error', 'Failed to load courses.');
    }
}

function populateCourseSelect() {
    const select = document.getElementById('course-select');
    if (!select) {
        console.error('Course select element not found!');
        return;
    }
    select.innerHTML = '<option value="">-- Select a Course --</option>';
    if (!allCourses || allCourses.length === 0) {
        console.warn('No courses available to populate.');
        return;
    }
    allCourses.forEach(course => {
        console.log('Adding course:', course);
        const option = document.createElement('option');
        option.value = course.courseId;
        option.textContent = course.courseTitle;
        select.appendChild(option);
    });
}

async function fetchLectures(courseId) {
    try {
        const response = await fetch(LECTURE_API_URL);
        if (!response.ok) throw new Error('Failed to fetch lectures');
        const lectures = await response.json();
         
        const courseLectures = lectures.filter(l => l.courseId == courseId || (l.course && l.course.courseId == courseId));
        populateLectureSelect(courseLectures);
    } catch (error) {
        console.error('Error fetching lectures:', error);
        showMessage('Error', 'Failed to load lectures.');
    }
}

function populateLectureSelect(lectures) {
    const select = document.getElementById('lecture-select');
    select.innerHTML = '<option value="">-- Select a Lecture --</option>';
    lectures.forEach(lecture => {
        const option = document.createElement('option');
        option.value = lecture.lectureId;
        option.textContent = lecture.lectureTitle;
        select.appendChild(option);
    });
}

async function fetchParts(lectureId) {
    try {
         
        const response = await fetch(`${LECTURE_API_URL}/${lectureId}`);
        if (!response.ok) throw new Error('Failed to fetch lecture details');
        const lecture = await response.json();

        currentLectureParts = lecture.parts || [];
        renderPartsTable();
    } catch (error) {
        console.error('Error fetching parts:', error);
        showMessage('Error', 'Failed to load lecture parts.');
    }
}

function renderPartsTable() {
    const container = document.getElementById('parts-table-container');
    const tbody = document.getElementById('parts-table-body');
    container.classList.remove('hidden');
    tbody.innerHTML = '';

    if (currentLectureParts.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="px-6 py-4 text-center text-gray-500">No parts found for this lecture.</td></tr>';
        return;
    }

    currentLectureParts.forEach(part => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${part.partTitle || 'Untitled'}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${part.partType || 'N/A'}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 truncate max-w-xs">${part.contentUrl || ''}</td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button onclick="openEditPartModal(${part.id})" class="text-indigo-600 hover:text-indigo-900 mr-3">Edit</button>
                <button onclick="deletePart(${part.id})" class="text-red-600 hover:text-red-900">Delete</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function openCreatePartModal() {
    document.getElementById('part-modal-title').textContent = 'Add Lecture Part';
    document.getElementById('part-form').reset();
    document.getElementById('part-id').value = '';
    document.getElementById('part-modal').classList.remove('hidden');
}

function openEditPartModal(partId) {
    const part = currentLectureParts.find(p => p.id === partId);
    if (!part) return;

    document.getElementById('part-modal-title').textContent = 'Edit Lecture Part';
    document.getElementById('part-id').value = part.id;
    document.getElementById('part-title').value = part.partTitle || '';
    document.getElementById('part-type').value = part.partType || 'VIDEO';
    document.getElementById('part-content').value = part.contentUrl || '';

    document.getElementById('part-modal').classList.remove('hidden');
}

function closePartModal() {
    document.getElementById('part-modal').classList.add('hidden');
}

async function handlePartSubmit(e) {
    e.preventDefault();

    const partId = document.getElementById('part-id').value;
    const partData = {
        partTitle: document.getElementById('part-title').value,
        partType: document.getElementById('part-type').value,
        contentUrl: document.getElementById('part-content').value
    };

    try {
        let response;
        if (partId) {
             
            response = await fetch(`${LECTURE_PART_API_URL}/${partId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(partData)
            });
        } else {
             
            response = await fetch(`${LECTURE_API_URL}/${currentLectureId}/parts`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(partData)
            });
        }

        if (!response.ok) throw new Error('Failed to save part');

        closePartModal();
        showMessage('Success', 'Lecture part saved successfully.');
        fetchParts(currentLectureId);  
    } catch (error) {
        console.error('Error saving part:', error);
        showMessage('Error', 'Failed to save lecture part.');
    }
}

async function deletePart(partId) {
    if (!confirm('Are you sure you want to delete this part?')) return;

    try {
        const response = await fetch(`${LECTURE_PART_API_URL}/${partId}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Failed to delete part');

        showMessage('Success', 'Lecture part deleted successfully.');
        fetchParts(currentLectureId);  
    } catch (error) {
        console.error('Error deleting part:', error);
        showMessage('Error', 'Failed to delete lecture part.');
    }
}

function showMessage(title, message) {
    const modal = document.getElementById('custom-modal');
    if (modal) {
        document.getElementById('modal-title').textContent = title;
        document.getElementById('modal-message').textContent = message;
        modal.classList.remove('hidden');
        document.getElementById('modal-close-btn').onclick = () => modal.classList.add('hidden');
    } else {
        alert(`${title}: ${message}`);
    }
}
