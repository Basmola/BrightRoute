// =======================================================
// 6. CONTENT RENDERING (contentRenders.js)
// Backend Integration: Connected to Spring Boot CourseController
// =======================================================

const API_BASE_URL = 'http://localhost:7070/api/courses';

// Global store for courses to avoid re-fetching constantly
window.allCourses = [];

// Configuration to map Database Level IDs to UI Styles
const LEVEL_CONFIG = {
    1: { title: 'Junior Level', tag: 'primary', label: 'Junior' },
    2: { title: 'Intermediate Level', tag: 'secondary', label: 'Intermediate' },
    3: { title: 'Expert Level', tag: 'accent', label: 'Expert' }
};

/**
 * HELPER: Fetches all courses from the API and updates the global store.
 * Returns the list of courses.
 */
async function fetchCourses() {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error('Failed to fetch courses');
        const courses = await response.json();
        window.allCourses = courses; // Update global store
        return courses;
    } catch (error) {
        console.error('API Error:', error);
        if (typeof showMessage !== 'undefined') showMessage('Connection Error', 'Could not load data from the server.');
        return [];
    }
}

// =======================================================
// STUDENT / PUBLIC VIEWS
// =======================================================

/**
 * UPDATED: Renders individual COURSE Cards (fetched from API).
 * Iterates through actual courses instead of static level summaries.
 */
async function renderLevelCards() {
    const container = document.getElementById('level-cards-container');
    if (!container) return;

    // 1. Fetch fresh data
    const courses = await fetchCourses();

    if (courses.length === 0) {
        container.innerHTML = '<p class="text-center text-gray-500 w-full col-span-3 py-10">No courses available at the moment.</p>';
        return;
    }

    // 2. Generate HTML for each course
    const cards = courses.map(course => {
        // Get style config based on levelId, default to Level 1 style if not found
        const levelConfig = LEVEL_CONFIG[course.levelId] || LEVEL_CONFIG[1];
        
        // Handle potentially missing description or lecture count
        const desc = course.courseDescription || 'No description provided.';
        const lectureCount = course.courseNumberOfLectures || 0;

        return `
        <div class="group bg-white rounded-xl shadow-soft border-t-4 border-${levelConfig.tag} flex flex-col card-hover-effect h-full">
            <div class="p-5 flex-1 flex flex-col">
                <div class="flex justify-between items-start mb-3">
                    <span class="text-xs font-bold uppercase tracking-wider text-${levelConfig.tag} bg-${levelConfig.tag}/10 px-2 py-1 rounded">
                        ${levelConfig.label} (L${course.levelId})
                    </span>
                </div>

                <h3 class="text-xl font-bold text-gray-900 mb-2 leading-tight">${course.courseTitle}</h3>
                
                <p class="text-gray-600 text-sm mb-4 flex-grow">
                    ${desc}
                </p>
                
                <div class="border-t border-gray-100 pt-4 mt-auto">
                    <ul class="space-y-2 text-gray-700 text-sm">
                        <li class="flex items-center">
                            <svg class="w-4 h-4 mr-2 text-${levelConfig.tag}" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>
                            <span class="font-medium">Instructor:</span>&nbsp;${course.courseInstructor}
                        </li>
                        <li class="flex items-center">
                            <svg class="w-4 h-4 mr-2 text-${levelConfig.tag}" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M5 18h8a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z"></path></svg>
                            <span>${lectureCount} Lectures</span>
                        </li>
                    </ul>
                </div>
            </div>
            
            <button onclick="navigate('course-detail-view', '${course.courseId}')" 
                class="mt-auto py-3 bg-${levelConfig.tag} text-white rounded-b-xl font-semibold hover:bg-gray-800 transition shadow-lg flex items-center justify-center gap-2">
                Start Course <span class="text-lg">&rarr;</span>
            </button>
        </div>
        `;
    }).join('');

    container.innerHTML = cards;
}
window.renderLevelCards = renderLevelCards;


/**
 * Renders the list of enrolled/published courses on the Student Dashboard.
 */
async function renderStudentDashboardCourses() {
    const container = document.getElementById('student-courses-list');
    if (!container) return;

    // Use global store if available, or fetch if empty
    let courses = window.allCourses;
    if (courses.length === 0) courses = await fetchCourses();

    const validCourses = courses; 

    if (validCourses.length === 0) {
        container.innerHTML = `<p class="text-gray-500 p-4">No courses available at the moment.</p>`;
        return;
    }

    const courseList = validCourses.map(course => {
        // Determine styling based on Level ID
        const levelId = course.levelId || 1;
        const config = LEVEL_CONFIG[levelId] || LEVEL_CONFIG[1];
        
        // Colors for placeholder image
        let bgColor = 'cccccc', textColor = '000000';
        if(config.tag === 'primary') { bgColor = '111827'; textColor = 'ffffff'; }
        if(config.tag === 'secondary') { bgColor = '374151'; textColor = 'ffffff'; }
        if(config.tag === 'accent') { bgColor = '9ca3af'; textColor = '111827'; }

        return `
            <div class="flex flex-col sm:flex-row items-start sm:items-center p-4 border border-gray-100 rounded-xl hover:bg-gray-50 transition duration-150 cursor-pointer"
                onclick="navigate('course-detail-view', '${course.courseId}')">
                <img class="w-16 h-16 rounded-lg object-cover mr-4 mb-4 sm:mb-0"
                    src="https://placehold.co/64x64/${bgColor}/${textColor}?text=${course.courseTitle.substring(0, 3)}" alt="Course Image">
                <div class="flex-1 min-w-0">
                    <h4 class="font-bold text-primary truncate">${course.courseTitle}</h4>
                    <p class="text-sm text-gray-500">Instructor: ${course.courseInstructor}</p>
                </div>
                <button
                    class="w-full sm:w-auto mt-4 sm:mt-0 sm:ml-6 px-3 py-1 bg-primary text-white rounded-lg hover:bg-gray-800 text-sm transition"
                    onclick="event.stopPropagation(); navigate('lecture-detail-view')">Continue</button>
            </div>
        `;
    }).join('');

    container.innerHTML = `<div class="space-y-4">${courseList}</div>`;
}
window.renderStudentDashboardCourses = renderStudentDashboardCourses;


/**
 * Renders lectures for a specific level (filtered from API courses).
 */
async function renderLevelLectures(levelId) {
    const container = document.getElementById('lectures-grid');
    if (!container) return;

    let courses = window.allCourses;
    if (courses.length === 0) courses = await fetchCourses();

    // 1. Filter courses by Level ID
    const levelCourses = courses.filter(c => c.levelId == levelId);

    // 2. Extract all lectures from these courses
    let allLectures = [];
    levelCourses.forEach(c => {
        if(c.lectures) {
            c.lectures.forEach(l => {
                // Add course metadata to lecture for display
                l.color = LEVEL_CONFIG[levelId] ? LEVEL_CONFIG[levelId].tag : 'primary';
                l.parentCourseTitle = c.courseTitle;
                allLectures.push(l);
            });
        }
    });

    if (allLectures.length === 0) {
        container.innerHTML = '<p class="text-gray-500 col-span-3">No lectures found for this level yet.</p>';
        return;
    }

    const cards = allLectures.map(lecture => `
        <div class="bg-white rounded-xl shadow-soft card-hover-effect overflow-hidden border-t-4 border-${lecture.color}">
            <h3 class="text-xl font-bold text-gray-900 p-4 pb-0">${lecture.lectureTitle}</h3>
            <p class="text-xs text-gray-500 px-4 pt-1">From: ${lecture.parentCourseTitle}</p>
            <div class="p-4">
                <p class="text-sm text-gray-600 mb-4">${lecture.lectureDescription || 'No description available.'}</p>
                <button onclick="showAccessCodeModal('${lecture.id}')" class="w-full py-2 bg-${lecture.color} text-white rounded-lg hover:bg-gray-800 transition font-semibold shadow-md">
                    Go To Lecture
                </button>
            </div>
        </div>
    `).join('');

    container.innerHTML = cards;
}
window.renderLevelLectures = renderLevelLectures;


// =======================================================
// ADMIN RENDERERS (Content Management)
// =======================================================

/**
 * Renders the Course Management table (Admin View).
 */
async function renderContentManagement() {
    const container = document.getElementById('course-table-body');
    if (!container) return;

    const courses = await fetchCourses();

    const rows = courses.map(course => {
        const status = "Published"; 
        const statusColor = 'bg-secondary text-white';
        const statusBadge = `<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${statusColor}">${status}</span>`;

        return `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">${course.courseTitle}</td> 
                <td class="px-6 py-4 whitespace-nowrap">${course.courseInstructor}</td> 
                <td class="px-6 py-4 whitespace-nowrap">${course.courseNumberOfLectures || 0}</td> 
                <td class="px-6 py-4 whitespace-nowrap">${statusBadge}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <a href="#" class="text-primary hover:text-gray-800" onclick="editCourse(${course.courseId})">Edit</a> | 
                    <a href="#" class="text-red-600 hover:text-red-900" onclick="deleteCourse(${course.courseId})">Delete</a>
                </td>
            </tr>
        `;
    }).join('');

    container.innerHTML = rows;
}
window.renderContentManagement = renderContentManagement;

/**
 * Renders the Lecture Management table.
 */
async function renderLectureManagement() {
    const container = document.getElementById('lecture-table-body');
    if (!container) return;

    let courses = window.allCourses;
    if (courses.length === 0) courses = await fetchCourses();

    let allLectures = [];
    
    // Aggregate lectures
    courses.forEach(course => {
        if (course.lectures && Array.isArray(course.lectures)) {
            course.lectures.forEach(lecture => {
                allLectures.push({
                    ...lecture,
                    parentCourseTitle: course.courseTitle,
                    lectureId: lecture.id,
                    courseId: course.courseId 
                });
            });
        }
    });

    const rows = allLectures.map(lecture => {
        const partsCount = lecture.parts ? lecture.parts.length : 0;

        return `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">${lecture.lectureTitle}</td>
                <td class="px-6 py-4 whitespace-nowrap text-gray-600">${lecture.parentCourseTitle}</td>
                <td class="px-6 py-4 whitespace-nowrap">${partsCount}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <a href="#" class="text-secondary hover:text-gray-600" 
                        onclick="navigate('add-lecture-part-view'); populateLectureDropdown('${lecture.courseId}', 'part-target-lecture');">Add Parts</a>
                </td>
            </tr>
        `;
    }).join('');

    container.innerHTML = rows;
}
window.renderLectureManagement = renderLectureManagement;


/**
 * Renders User Management.
 */
async function renderUserManagement() {
    const container = document.getElementById('user-table-body');
    if (!container) return;
    
    const users = []; // Empty for now

    if(users.length === 0) {
        container.innerHTML = '<tr><td colspan="4" class="px-6 py-4 text-center text-gray-500">No User API Endpoint connected.</td></tr>';
        return;
    }
}
window.renderUserManagement = renderUserManagement;


// =======================================================
// ACTION HANDLERS (CRUD)
// =======================================================

/**
 * PREPARE MODAL: Shows the modal for adding or editing a Course.
 */
function showAddCourseModal(courseDataToEdit = null) {
    const modal = document.getElementById('custom-modal');
    const titleElement = document.getElementById('modal-title');
    const messageElement = document.getElementById('modal-message');
    const actionsElement = document.getElementById('modal-actions');

    const isEditing = courseDataToEdit !== null;
    const currentCourseId = isEditing ? courseDataToEdit.courseId : null;
    
    // Default/Existing values
    const defaultTitle = isEditing ? courseDataToEdit.courseTitle : '';
    const defaultInstructor = isEditing ? courseDataToEdit.courseInstructor : '';
    const defaultDescription = isEditing ? courseDataToEdit.courseDescription : ''; 
    const defaultLevel = isEditing ? courseDataToEdit.levelId : '';

    titleElement.textContent = isEditing ? `Edit Course: ${defaultTitle}` : 'Create New Course';
    
    // Modal Content (Form)
    messageElement.innerHTML = `
        <div class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">Course Title</label>
                    <input type="text" id="new-course-title" value="${defaultTitle}" required class="mt-1 w-full p-2 border border-gray-300 rounded-lg">
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700">Instructor</label>
                    <input type="text" id="new-course-instructor" value="${defaultInstructor}" required class="mt-1 w-full p-2 border border-gray-300 rounded-lg">
                </div>
            </div>
            
            <div>
                <label class="block text-sm font-medium text-gray-700">Description</label>
                <textarea id="new-course-description" rows="2" class="mt-1 w-full p-2 border border-gray-300 rounded-lg">${defaultDescription}</textarea>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">Target Level</label>
                <select id="new-course-level" required class="mt-1 w-full p-2 border border-gray-300 rounded-lg bg-white">
                    <option value="" ${defaultLevel === '' ? 'selected' : ''}>Select Level</option>
                    <option value="1" ${defaultLevel === 1 ? 'selected' : ''}>Level 1 (Junior)</option>
                    <option value="2" ${defaultLevel === 2 ? 'selected' : ''}>Level 2 (Intermediate)</option>
                    <option value="3" ${defaultLevel === 3 ? 'selected' : ''}>Level 3 (Expert)</option>
                </select>
            </div>
        </div>
        <input type="hidden" id="course-edit-id" value="${currentCourseId || ''}">
    `;

    // Action Buttons
    const buttonText = isEditing ? 'Update Course' : 'Save Course';
    actionsElement.innerHTML = `
        <button onclick="saveCourseToApi();" class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-gray-800 transition shadow-md mr-3">${buttonText}</button>
        <button onclick="hideModal();" class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition">Cancel</button>
    `;

    modal.classList.remove('hidden');
    setTimeout(() => modal.classList.add('modal-visible'), 10);
}
window.showAddCourseModal = showAddCourseModal; 


/**
 * ACTION: Creates (POST) or Updates (PUT) a course via API.
 */
async function saveCourseToApi() {
    const editId = document.getElementById('course-edit-id').value.trim(); 
    const title = document.getElementById('new-course-title').value.trim();
    const instructor = document.getElementById('new-course-instructor').value.trim();
    const levelId = parseInt(document.getElementById('new-course-level').value); 
    const description = document.getElementById('new-course-description').value.trim();

    if (!title || !instructor || !levelId) {
        if (typeof showMessage !== 'undefined') showMessage('Missing Data', 'Please fill all required fields.');
        return; 
    }

    const courseData = {
        courseTitle: title,
        courseDescription: description,
        courseInstructor: instructor,
        levelId: levelId
    };

    try {
        let response;
        if (editId) {
            // UPDATE (PUT)
            response = await fetch(`${API_BASE_URL}/${editId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(courseData)
            });
        } else {
            // CREATE (POST)
            response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(courseData)
            });
        }

        if (response.ok) {
            showMessage('Success', `Course saved successfully!`);
            hideModal();
            fetchCourses().then(() => {
                // Refresh views
                if(document.getElementById('course-table-body')) renderContentManagement();
                if(document.getElementById('level-cards-container')) renderLevelCards();
            });
        } else {
            throw new Error('Server returned ' + response.status);
        }
    } catch (error) {
        console.error(error);
        showMessage('Error', 'Failed to save course. Check console.');
    }
}
window.saveCourseToApi = saveCourseToApi; 


/**
 * ACTION: Triggered by Edit button in table. Finds course and opens modal.
 */
function editCourse(courseId) {
    const courseToEdit = window.allCourses.find(c => c.courseId === courseId);
    if (courseToEdit) {
        showAddCourseModal(courseToEdit);
    } else {
        showMessage('Error', 'Course not found in memory. Try refreshing.');
    }
}
window.editCourse = editCourse;


/**
 * ACTION: Triggered by Delete button. Shows confirmation.
 */
function deleteCourse(courseId) {
    const confirmationMessage = `Are you sure you want to delete course ID: ${courseId}?`;
    const actions = `
        <button onclick="confirmDeleteCourseApi(${courseId});" class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition shadow-md mr-3">Confirm Delete</button>
        <button onclick="hideModal();" class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition">Cancel</button>
    `;
    showModal('Confirm Deletion', `<p class="text-gray-600">${confirmationMessage}</p>`, actions);
}
window.deleteCourse = deleteCourse;

/**
 * ACTION: Executes DELETE request.
 */
async function confirmDeleteCourseApi(courseId) {
    try {
        const response = await fetch(`${API_BASE_URL}/${courseId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            hideModal();
            showMessage('Success', 'Course deleted.');
            // Refresh data
            fetchCourses().then(() => renderContentManagement());
        } else {
            throw new Error('Failed to delete');
        }
    } catch (error) {
        hideModal();
        showMessage('Error', 'Could not delete course.');
    }
}
window.confirmDeleteCourseApi = confirmDeleteCourseApi;


// =======================================================
// LECTURE & DROPDOWN HELPERS
// =======================================================

/**
 * POPULATE: Fills course dropdown for "Add Lecture" forms.
 */
function populateCourseDropdowns() {
    const courseDropdown = document.getElementById('part-target-course'); 
    const lectureCourseDropdown = document.getElementById('lecture-target-course');
    
    const targets = [courseDropdown, lectureCourseDropdown];
    
    const courses = window.allCourses;

    targets.forEach(dropdown => {
        if (!dropdown) return;
        dropdown.innerHTML = '<option value="">-- Select Course --</option>';
        courses.forEach(course => {
            const option = document.createElement('option');
            option.value = course.courseId;
            option.textContent = course.courseTitle;
            dropdown.appendChild(option);
        });
    });
}
window.populateCourseDropdowns = populateCourseDropdowns;

/**
 * POPULATE: Fills lecture dropdown based on selected course.
 */
function populateLectureDropdown(courseId, targetElementId) {
    const lectureDropdown = document.getElementById(targetElementId);
    if (!lectureDropdown) return;
    
    lectureDropdown.innerHTML = '<option value="">-- Select Lecture --</option>';

    // Find course in global store
    const course = window.allCourses.find(c => c.courseId == courseId);

    if (course && course.lectures && course.lectures.length > 0) {
        course.lectures.forEach(lecture => {
            const option = document.createElement('option');
            option.value = `${courseId},${lecture.id}`; 
            option.textContent = `L${lecture.lectureOrderNumber}: ${lecture.lectureTitle}`;
            lectureDropdown.appendChild(option);
        });
    }
}
window.populateLectureDropdown = populateLectureDropdown;

/**
 * ACTION: Add New Lecture.
 */
async function addNewLecture() {
    // 1. Capture Data
    const courseId = document.getElementById('lecture-target-course').value;
    const lectureTitle = document.getElementById('new-lecture-title').value.trim();
    const lectureDescription = document.getElementById('new-lecture-description').value.trim();
    
    if (!courseId || !lectureTitle) {
        showMessage('Missing Data', 'Please select a Course and enter a Title.');
        return; 
    }

    // 2. Get the current course object
    const course = window.allCourses.find(c => c.courseId == courseId);
    if (!course) return showMessage('Error', 'Course not found.');

    // 3. Prepare new Lecture object
    const newLecture = {
        lectureTitle: lectureTitle,
        lectureDescription: lectureDescription,
        lectureOrderNumber: (course.lectures ? course.lectures.length : 0) + 1,
        courseId: parseInt(courseId),
        parts: []
    };

    // 4. Update Course via PUT
    if (!course.lectures) course.lectures = [];
    course.lectures.push(newLecture);

    try {
        const response = await fetch(`${API_BASE_URL}/${courseId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(course)
        });

        if (response.ok) {
            hideModal();
            showMessage('Success', 'Lecture added!');
            fetchCourses().then(() => renderLectureManagement());
        } else {
            throw new Error('Failed to update course with new lecture');
        }
    } catch (e) {
        console.error(e);
        showMessage('Error', 'Could not add lecture. See console.');
    }
}
window.addNewLecture = addNewLecture;