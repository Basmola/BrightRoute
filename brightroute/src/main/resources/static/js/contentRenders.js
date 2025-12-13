// =======================================================
// 6. CONTENT RENDERING (Conceptual: content_renderers.js) - START
// =======================================================

function renderLevelCards() {
    const container = document.getElementById('level-cards-container');
    if (!container) return;

    const cards = mockLevels.map(level => `
                <div class="group bg-white rounded-xl shadow-soft border-t-4 border-${level.tag} flex flex-col card-hover-effect">
                    <div class="p-5">
                        <span class="text-3xl font-black text-${level.tag} mb-3">Level ${level.level}</span>
                        <h3 class="text-xl font-bold text-primary mb-3">${level.title}</h3>
                        <p class="text-gray-600 text-sm mb-4 flex-grow">${level.desc}</p>
                        
                        <ul class="space-y-1 text-gray-700 list-disc list-inside text-sm mb-5">
                            <li>${level.courses} Foundational Courses</li>
                            <li>${level.lectures} Video Lectures</li>
                            <li>${level.projects}</li>
                        </ul>
                    </div>
                    
                    <button onclick="navigate('level-lectures-view', ${level.level})" 
                        class="mt-auto card-content-paddings py-2 bg-${level.tag} text-white rounded-b-xl font-semibold hover:bg-gray-800 transition shadow-lg">
                        ${level.level === 3 ? 'Join Experts' : 'Explore Level'}
                    </button>
                </div>
            `).join('');

    container.innerHTML = cards;
}

function showAddCourseModal(courseDataToEdit = null) {
    const modal = document.getElementById('custom-modal');
    const titleElement = document.getElementById('modal-title');
    const messageElement = document.getElementById('modal-message');
    const actionsElement = document.getElementById('modal-actions');

    const isEditing = courseDataToEdit !== null;
    const currentCourseId = isEditing ? courseDataToEdit.id : null;
    
    // القيم الافتراضية
    const defaultTitle = isEditing ? courseDataToEdit.title : '';
    const defaultInstructor = isEditing ? courseDataToEdit.instructor : '';
    const defaultStatus = isEditing ? courseDataToEdit.status : 'Draft';
    const defaultLevel = isEditing ? courseDataToEdit.levelTag : '';
    const defaultDescription = isEditing ? courseDataToEdit.courseDescription || '' : ''; // حقل الوصف الجديد

    // 1. إخفاء الـ Modal أولاً (لإعادة تهيئته)
    // if (typeof hideModal !== 'undefined') hideModal();

    // 2. تحديد العنوان
    titleElement.textContent = isEditing ? `Edit Course: ${defaultTitle}` : 'Create New Course';
    
    // 3. بناء محتوى الـ Modal (بدون حقول Lectures المعقدة)
    messageElement.innerHTML = `
        <div class="space-y-4">
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">Course Title</label>
                    <input type="text" id="new-course-title" value="${defaultTitle}" placeholder="e.g., Python Fundamentals" class="mt-1 w-full p-2 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary">
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700">Instructor</label>
                    <input type="text" id="new-course-instructor" value="${defaultInstructor}" placeholder="Instructor Name" class="mt-1 w-full p-2 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary">
                </div>
            </div>
            
            <div>
                <label class="block text-sm font-medium text-gray-700">Description (وصف الدورة)</label>
                <textarea id="new-course-description" rows="2" placeholder="Brief description of the course content." class="mt-1 w-full p-2 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary">${defaultDescription}</textarea>
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">Target Level</label>
                    <select id="new-course-level" class="mt-1 w-full p-2 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary bg-white">
                        <option value="" ${defaultLevel === '' ? 'selected' : ''}>Select Target Level</option>
                        <option value="primary" ${defaultLevel === 'primary' ? 'selected' : ''}>Level 1 (Primary)</option>
                        <option value="secondary" ${defaultLevel === 'secondary' ? 'selected' : ''}>Level 2 (Secondary)</option>
                        <option value="highschool" ${defaultLevel === 'highschool' ? 'selected' : ''}>Level 3 (High School)</option>
                    </select>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700">Status</label>
                    <select id="new-course-status" class="mt-1 w-full p-2 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary bg-white">
                        <option value="Draft" ${defaultStatus === 'Draft' ? 'selected' : ''}>Draft</option>
                        <option value="Published" ${defaultStatus === 'Published' ? 'selected' : ''}>Published</option>
                    </select>
                </div>
            </div>
        </div>
        <input type="hidden" id="course-edit-id" value="${currentCourseId || ''}">
    `;

    // 4. تحديث نص زر الحفظ
    const buttonText = isEditing ? 'Update Course' : 'Save Course';
    actionsElement.innerHTML = `
        <button onclick="addNewCourse();" class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-gray-800 transition shadow-md mr-3">${buttonText}</button>
        <button onclick="hideModal();" class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition">Cancel</button>
    `;

    modal.classList.remove('hidden');
    setTimeout(() => modal.classList.add('modal-visible'), 10);
}
window.showAddCourseModal = showAddCourseModal; 


function addNewCourse() {
    const editId = document.getElementById('course-edit-id').value.trim(); 
    const title = document.getElementById('new-course-title').value.trim();
    const instructor = document.getElementById('new-course-instructor').value.trim();
    const status = document.getElementById('new-course-status').value;
    const level = document.getElementById('new-course-level').value; 
    const description = document.getElementById('new-course-description').value.trim();

    // التحقق من الحقول الإجبارية
    if (!title || !instructor || !level || level === '') {
        if (typeof showMessage !== 'undefined') {
            showMessage('Missing Data', 'Please enter the Title, Instructor, and select a Target Level.');
        }
        return; 
    }
    
    const courseData = {
        courseTitle: title, // اسم الدورة
        courseDescription: description, // الوصف الجديد
        courseInstructor: instructor,
        courseNumberOfLectures: 0, // نبدأ بـ 0 ونضيف المحاضرات لاحقاً
        status: status,
        tag: level, 
        levelTag: level,
        levelId: (level === 'primary' ? 1 : level === 'secondary' ? 2 : 3),
        // افتراض: الفيديوهات والمواد فارغة في مرحلة الإضافة الأولية
        lectures: [] 
    };

    // 4. حفظ البيانات أو تحديثها
    if (typeof mockCourses === 'undefined') { /* Error */ return; }
    
    if (editId) {
        // وضع التعديل (Update)
        const index = mockCourses.findIndex(c => c.id === editId);
        if (index > -1) {
            // ندمج البيانات الجديدة مع القديمة للحفاظ على الـ lectures الموجودة
            mockCourses[index] = { ...mockCourses[index], ...courseData };
            if (typeof showMessage !== 'undefined') showMessage('Success', `Course "${title}" updated successfully!`);
        }
    } else {
        // وضع الإضافة (Create)
        courseData.id = 'C' + (mockCourses.length + 1); // ID وهمي
        courseData.courseId = mockCourses.length + 1; // ID حقيقي
        mockCourses.push(courseData);
        if (typeof showMessage !== 'undefined') showMessage('Success', `Course "${title}" added successfully!`);
    }

    // 5. تحديث الواجهة
    if (typeof hideModal !== 'undefined') hideModal();
    if (typeof renderContentManagement !== 'undefined') renderContentManagement();
}
window.addNewCourse = addNewCourse;
// في ملف contentRenders.js

function renderUserManagement() {
    const container = document.getElementById('user-table-body');
    if (!container) return;

    // التأكد من وجود مصفوفة المستخدمين
    if (typeof registeredUsers === 'undefined') return;

    const rows = registeredUsers.map(user => {
        const roleBadge = user.role === 'admin'
            ? `<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">Admin</span>`
            : `<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-300 text-primary">Student</span>`;

        return `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">${user.firstName} ${user.lastName}</td>
                <td class="px-6 py-4 whitespace-nowrap">${roleBadge}</td>
                <td class="px-6 py-4 whitespace-nowrap">${user.email}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <a href="#" class="text-red-600 hover:text-red-900" onclick="deleteUser('${user.email}')">Delete</a>
                </td>
            </tr>
        `;
    }).join('');

    container.innerHTML = rows;
}
window.renderUserManagement = renderUserManagement;
function renderContentManagement() {
    const container = document.getElementById('course-table-body');
    if (!container) return;

    if (typeof mockCourses === 'undefined') return;

    const rows = mockCourses.map(course => {
        const title = course.courseTitle || course.title;
        const instructor = course.courseInstructor || course.instructor;
        const lecturesCount = course.courseNumberOfLectures || course.lectures || 0;
        let statusColor;

        switch (course.status) {
            case 'Published':
                statusColor = 'bg-secondary text-white';
                break;
            case 'Draft':
                statusColor = 'bg-gray-400 text-gray-800';
                break;
            default:
                statusColor = 'bg-gray-200 text-gray-700';
        }

        const statusBadge = `<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${statusColor}">${course.status}</span>`;

        return `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">${title}</td> 
                <td class="px-6 py-4 whitespace-nowrap">${instructor}</td> 
                <td class="px-6 py-4 whitespace-nowrap">${lecturesCount}</td> 
                <td class="px-6 py-4 whitespace-nowrap">${statusBadge}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <a href="#" class="text-primary hover:text-gray-800" onclick="editCourse('${course.id}')">Edit</a> | 
                    <a href="#" class="text-red-600 hover:text-red-900" onclick="deleteCourse('${course.id}')">Delete</a>
                </td>
            </tr>
        `;
    }).join('');

    container.innerHTML = rows;
}
window.renderContentManagement = renderContentManagement;

function deleteCourse(courseId) {
    if (typeof showMessage === 'undefined') return;

    const confirmationMessage = `Are you sure you want to delete course ID: ${courseId}? This action cannot be undone.`;
    const actions = `
        <button onclick="confirmDeleteCourse('${courseId}');" class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition shadow-md mr-3">Confirm Delete</button>
        <button onclick="hideModal();" class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition">Cancel</button>
    `;

    // استخدام showModal أو showMessage لعرض التأكيد
    if (typeof showModal !== 'undefined') {
        showModal('Confirm Deletion', `<p class="text-gray-600">${confirmationMessage}</p>`, actions);
    } else {
        // بديل بسيط إذا لم تكن showModal متاحة
        if (confirm(confirmationMessage)) {
            confirmDeleteCourse(courseId);
        }
    }
}
window.deleteCourse = deleteCourse;

function confirmDeleteCourse(courseId) {
    if (typeof mockCourses === 'undefined' || typeof renderContentManagement === 'undefined' || typeof showMessage === 'undefined') return;
    
    // إيجاد وحذف الدورة من المصفوفة
    const indexToDelete = mockCourses.findIndex(c => c.id === courseId);
    
    if (indexToDelete > -1) {
        // استخدام splice لحذف عنصر واحد في هذا الموقع
        mockCourses.splice(indexToDelete, 1); 
        
        // إغلاق الـ modal (إذا كان مفتوحاً) وتحديث الجدول
        if (typeof hideModal !== 'undefined') hideModal();
        renderContentManagement();
        
        showMessage('Success', `Course ID ${courseId} has been deleted successfully.`);
    } else {
        if (typeof hideModal !== 'undefined') hideModal();
        showMessage('Error', `Course ID ${courseId} not found.`);
    }
}
window.confirmDeleteCourse = confirmDeleteCourse;


function editCourse(courseId) {
    if (typeof mockCourses === 'undefined' || typeof showAddCourseModal === 'undefined') return;

    const courseToEdit = mockCourses.find(c => c.id === courseId);

    if (courseToEdit) {
        // استدعاء دالة showAddCourseModal و تمرير بيانات الدورة لها
        showAddCourseModal(courseToEdit);
    } else {
        if (typeof showMessage !== 'undefined') {
            showMessage('Error', 'Course not found for editing.');
        }
    }
}
window.editCourse = editCourse;

function renderStudentDashboardCourses() {
    const container = document.getElementById('student-courses-list');
    if (!container) return;

    const enrolledCourses = mockCourses.filter(c => c.status === 'Published'); // Example filter

    if (enrolledCourses.length === 0) {
        container.innerHTML = `<p class="text-gray-500 p-4">You are not currently enrolled in any published courses.</p>`;
        return;
    }

    const courseList = enrolledCourses.map(course => {
        let bgColor, textColor;
        switch (course.tag) {
            case 'primary':
                bgColor = '111827';
                textColor = 'ffffff';
                break;
            case 'secondary':
                bgColor = '374151';
                textColor = 'ffffff';
                break;
            case 'accent':
                bgColor = '9ca3af';
                textColor = '111827';
                break;
            default:
                bgColor = 'cccccc';
                textColor = '000000';
        }

        return `
                    <div class="flex flex-col sm:flex-row items-start sm:items-center p-4 border border-gray-100 rounded-xl hover:bg-gray-50 transition duration-150 cursor-pointer"
                        onclick="navigate('course-detail-view', '${course.id}')">
                        <img class="w-16 h-16 rounded-lg object-cover mr-4 mb-4 sm:mb-0"
                            src="https://placehold.co/64x64/${bgColor}/${textColor}?text=${course.imageText}" alt="Course Image">
                        <div class="flex-1 min-w-0">
                            <h4 class="font-bold text-primary truncate">${course.title}</h4>
                            <p class="text-sm text-gray-500">Instructor: ${course.instructor}</p>
                        </div>
                        <button
                            class="w-full sm:w-auto mt-4 sm:mt-0 sm:ml-6 px-3 py-1 bg-primary text-white rounded-lg hover:bg-gray-800 text-sm transition"
                            onclick="event.stopPropagation(); navigate('lecture-detail-view')">Continue</button>
                    </div>
                `;
    }).join('');

    container.innerHTML = `<div class="space-y-4">${courseList}</div>`;
}

function renderLevelLectures() {
    const container = document.getElementById('lectures-grid');
    if (!container) return;

    const mockLectures = [
        { id: 'L1', title: 'Introduction to JavaScript', desc: 'Covers fundamental syntax, variables, and basic data types. Essential for all beginners.', image: 'https://placehold.co/400x144/111827/ffffff?text=JavaScript+Basics', color: 'primary', accessCode: 'L1' },
        { id: 'L2', title: 'Understanding Loops and Arrays', desc: 'Deep dive into iterative structures (for, while) and array manipulation methods.', image: 'https://placehold.co/400x144/374151/ffffff?text=Loops+Arrays', color: 'secondary', accessCode: 'L2' },
        { id: 'L3', title: 'Functions and Scope', desc: 'Explaining function declaration, expressions, and the concept of global vs. local scope.', image: 'https://placehold.co/400x144/9ca3af/ffffff?text=Functions+Scope', color: 'accent', accessCode: 'L3' },
    ];

    const cards = mockLectures.map(lecture => `
                <div class="bg-white rounded-xl shadow-soft card-hover-effect overflow-hidden border-t-4 border-${lecture.color}">
                    <h3 class="text-xl font-bold text-gray-900 p-4 pb-0">${lecture.title}</h3>
                    <img class="w-full h-36 object-cover mt-2" src="${lecture.image}" alt="Lecture Thumbnail">
                    <div class="p-4">
                        <p class="text-sm text-gray-600 mb-4">${lecture.desc}</p>
                        <button onclick="showAccessCodeModal('${lecture.accessCode}')" class="w-full py-2 bg-${lecture.color} text-white rounded-lg hover:bg-gray-800 transition font-semibold shadow-md">
                            Go To Lecture
                        </button>
                    </div>
                </div>
            `).join('');

    container.innerHTML = cards;
}

function renderLectureManagement() {
    const container = document.getElementById('lecture-table-body');
    if (!container) return;

    if (typeof mockCourses === 'undefined') return;

    let allLectures = [];
    
    // تجميع كل المحاضرات من كل الدورات
    mockCourses.forEach(course => {
        const parentCourseTitle = course.courseTitle || course.title; // للحصول على اسم الكورس الأم
        if (course.lectures && Array.isArray(course.lectures)) {
            course.lectures.forEach(lecture => {
                allLectures.push({
                    ...lecture,
                    parentCourseTitle: parentCourseTitle,
                    lectureId: lecture.id,
                    courseId: course.id 
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
                    <a href="#" class="text-primary hover:text-gray-800" 
                        onclick="showEditLectureModal('${lecture.courseId}', ${lecture.lectureId})">Edit</a> | 
                    <a href="#" class="text-red-600 hover:text-red-900" 
                        onclick="deleteLecture('${lecture.courseId}', ${lecture.lectureId})">Delete</a>
                </td>
            </tr>
        `;
    }).join('');

    container.innerHTML = rows;
}
window.renderLectureManagement = renderLectureManagement;
function showAddLectureModal() {
    const modal = document.getElementById('custom-modal');
    const titleElement = document.getElementById('modal-title');
    const messageElement = document.getElementById('modal-message');
    const actionsElement = document.getElementById('modal-actions');

    // لضمان عمل الانتقال بشكل سليم
    // if (typeof hideModal !== 'undefined') hideModal();
    if (typeof mockCourses === 'undefined') return;

    // بناء خيارات الدورات المتاحة
    const courseOptions = mockCourses.map(course => {
        const title = course.courseTitle || course.title;
        const id = course.id;
        return `<option value="${id}">${title} (ID: ${id})</option>`;
    }).join('');

    titleElement.textContent = 'Add New Lecture';

    // **محتوى النموذج المُبسط:**
    messageElement.innerHTML = `
        <div class="space-y-4">
            <div>
                <label class="block text-sm font-medium text-gray-700">Select Target Course (الكورس)</label>
                <select id="lecture-target-course" required
                        class="mt-1 w-full p-2 border border-gray-300 rounded-lg focus:ring-secondary focus:border-secondary bg-white">
                    <option value="">-- Select Course --</option>
                    ${courseOptions}
                </select>
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">Lecture Title (اسم المحاضرة)</label>
                    <input type="text" id="new-lecture-title" placeholder="e.g., RESTful API Basics"
                           class="mt-1 w-full p-2 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary">
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700">Lecture Description</label>
                    <input type="text" id="new-lecture-description" placeholder="Short summary"
                           class="mt-1 w-full p-2 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary">
                </div>
            </div>
        </div>
        `;

    actionsElement.innerHTML = `
        <button onclick="addNewLecture();" class="px-4 py-2 bg-secondary text-white rounded-lg hover:bg-gray-600 transition shadow-md mr-3">Save Lecture</button>
        <button onclick="hideModal();" class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition">Cancel</button>
    `;

    modal.classList.remove('hidden');
    setTimeout(() => modal.classList.add('modal-visible'), 10);
}
window.showAddLectureModal = showAddLectureModal;


// في ملف contentRenders.js
// ... (داخل دالة addNewLecture)
function addNewLecture() {
    // 1. التقاط البيانات
    const courseId = document.getElementById('lecture-target-course').value;
    const lectureTitle = document.getElementById('new-lecture-title').value.trim();
    const lectureDescription = document.getElementById('new-lecture-description').value.trim();
    
    // 2. التحقق من الحقول الإجبارية (فقط الكورس والعنوان)
    if (!courseId || !lectureTitle) {
        if (typeof showMessage !== 'undefined') {
            showMessage('Missing Data', 'Please select a Course and enter a Lecture Title.');
        }
        return; 
    }

    // 3. إيجاد الدورة الهدف
    const courseIndex = mockCourses.findIndex(c => c.id === courseId);
    if (courseIndex === -1) {
        if (typeof showMessage !== 'undefined') showMessage('Error', 'Target Course not found in mock data.');
        return;
    }
    
    let course = mockCourses[courseIndex];
    
    // **ضمان وجود مصفوفة المحاضرات**
    if (!course.lectures || !Array.isArray(course.lectures)) {
        course.lectures = [];
    }
    let lectures = course.lectures;

    // 4. بناء كائن المحاضرة (Lecture)
    const newLecture = {
        id: lectures.length + 1,
        lectureTitle: lectureTitle,
        lectureDescription: lectureDescription,
        lectureOrderNumber: lectures.length + 1,
        parts: [], // مصفوفة Parts فارغة ليتم إضافتها لاحقًا
        courseId: course.courseId || course.id
    };

    // 5. إضافة المحاضرة وتحديث الواجهة
    lectures.push(newLecture);
    course.courseNumberOfLectures = lectures.length; 

    if (typeof hideModal !== 'undefined') hideModal();
    if (typeof renderContentManagement !== 'undefined') renderContentManagement();
    if (typeof renderLectureManagement !== 'undefined') renderLectureManagement(); 
    if (typeof showMessage !== 'undefined') {
        showMessage('Success', `Lecture "${lectureTitle}" added to "${course.courseTitle || course.title}" successfully!`);
    }
}
window.addNewLecture = addNewLecture;


// دالة لملء قائمة الكورسات في شاشة Add Lecture Part
// في ملف contentRenders.js

function populateCourseDropdowns() {
    if (typeof mockCourses === 'undefined') return;

    const courseDropdown = document.getElementById('part-target-course');
    if (!courseDropdown) return;
    
    // إعادة تعيين القائمة
    courseDropdown.innerHTML = '<option value="">-- Select Course First --</option>';
    
    mockCourses.forEach(course => {
        const title = course.courseTitle || course.title;
        const option = document.createElement('option');
        option.value = course.id;
        option.textContent = title;
        courseDropdown.appendChild(option);
    });
}
window.populateCourseDropdowns = populateCourseDropdowns;


// ب. دالة ملء قائمة المحاضرات (populateLectureDropdown):
// هذه الدالة تتلقى قيمة الكورس المحدد وتقوم بملء قائمة المحاضرات الخاصة به.
function populateLectureDropdown(courseId, targetElementId) {
    const lectureDropdown = document.getElementById(targetElementId);
    if (!lectureDropdown) return;
    
    lectureDropdown.innerHTML = '<option value="">-- Select Lecture --</option>';

    const course = mockCourses.find(c => c.id === courseId);

    if (course && course.lectures && course.lectures.length > 0) {
        course.lectures.forEach(lecture => {
            const option = document.createElement('option');
            // نستخدم ID الكورس و ID المحاضرة مفصولين بفاصلة لتسهيل استرجاع البيانات عند الحفظ
            option.value = `${courseId},${lecture.id}`; 
            option.textContent = `L${lecture.lectureOrderNumber}: ${lecture.lectureTitle}`;
            lectureDropdown.appendChild(option);
        });
    }
}
window.populateLectureDropdown = populateLectureDropdown;

// دالة معالجة حفظ جزء المحاضرة الجديد
// في ملف contentRenders.js
// في ملف contentRenders.js
function addNewLecturePartFinal() {
    // 1. التقاط البيانات الأساسية
    const selectedLectureValue = document.getElementById('part-target-lecture').value; 
    
    if (!selectedLectureValue) {
        if (typeof showMessage !== 'undefined') showMessage('Missing Data', 'Please select a Target Lecture.');
        return;
    }

    const [courseId, lectureIdStr] = selectedLectureValue.split(',');
    const lectureId = parseInt(lectureIdStr);

    const course = mockCourses.find(c => c.id === courseId);
    const lecture = course ? course.lectures.find(l => l.id === lectureId) : null;
    
    if (!lecture) return showMessage('Error', 'Target Lecture not found.');

    // 2. بناء الأجزاء الجديدة (Parts)
    if (!lecture.parts || !Array.isArray(lecture.parts)) {
        lecture.parts = [];
    }
    let partsArray = lecture.parts;
    let currentPartOrder = lecture.parts.length + 1;
    let globalPartId = Date.now(); 
    let addedCount = 0;

    // 3. التكرار على الأجزاء الثابتة (Part 1, Part 2, Part 3)
    for (let i = 1; i <= 3; i++) {
        const partUrl = document.getElementById(`part-url-${i}`).value.trim();
        const partType = document.getElementById(`part-type-${i}`).value;
        const partDescription = document.getElementById(`part-desc-${i}`).value.trim();
        
        // التحقق من أن الجزء تم إدخاله (يعتبر مُدخلاً إذا كان له رابط URL)
        if (partUrl) {
            // التحقق من نوع الجزء
            if (!partType) {
                if (typeof showMessage !== 'undefined') {
                    showMessage('Missing Type', `Please select a Type (Video/Document) for Part ${i}.`);
                    return;
                }
            }
            
            partsArray.push({
                id: globalPartId++, 
                partType: partType, 
                partContentUrl: partUrl,
                partDescription: partDescription || (partType === 'VIDEO' ? 'Video content' : 'Document file'),
                partOrderNumber: currentPartOrder++, 
                lectureId: lectureId
            });
            addedCount++;
        }
    }
    
    // 4. التحقق من إضافة أجزاء فعلياً
    if (addedCount === 0) {
        if (typeof showMessage !== 'undefined') {
            showMessage('Missing Content', 'No new parts were added. Please fill in at least one URL field.');
        }
        return;
    }

    // 5. تحديث الواجهة
    if (typeof renderLectureManagement !== 'undefined') renderLectureManagement(); 
    
    if (typeof showMessage !== 'undefined') {
        showMessage('Success', `${addedCount} new part(s) added to lecture: ${lecture.lectureTitle}.`);
    }

    // 6. إغلاق النموذج
    if (typeof hideModal !== 'undefined') hideModal();
}
window.addNewLecturePartFinal = addNewLecturePartFinal;

// ===================== USER DELETE IMPLEMENTATION (المعدلة لاستخدام Email) ===========================

function deleteUser(userEmail) {
    if (typeof showMessage === 'undefined') return;

    // رسالة تأكيد حذف المستخدم
    const confirmationMessage = `Are you sure you want to delete the user with email: ${userEmail}? This action cannot be undone.`;
    
    const actions = `
        <button onclick="confirmDeleteUser('${userEmail}');" class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition shadow-md mr-3">Confirm Delete</button>
        <button onclick="hideModal();" class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition">Cancel</button>
    `;

    // استخدام showModal لعرض التأكيد
    if (typeof showModal !== 'undefined') {
        showModal('Confirm User Deletion', `<p class="text-gray-600">${confirmationMessage}</p>`, actions);
    } else {
        // بديل بسيط إذا لم تكن showModal متاحة
        if (confirm(confirmationMessage)) {
            confirmDeleteUser(userEmail);
        }
    }
}
window.deleteUser = deleteUser;


function confirmDeleteUser(userEmail) {
    // التأكد من وجود مصفوفة المستخدمين
    if (typeof registeredUsers === 'undefined' || typeof renderUserManagement === 'undefined' || typeof showMessage === 'undefined') return;
    
    // إيجاد مؤشر (Index) المستخدم في مصفوفة registeredUsers باستخدام البريد الإلكتروني (email)
    const indexToDelete = registeredUsers.findIndex(u => u.email === userEmail);
    
    if (indexToDelete > -1) {
        // حذف صف المستخدم بالكامل 
        registeredUsers.splice(indexToDelete, 1); 
        
        // إغلاق الـ modal وتحديث جدول المستخدمين
        if (typeof hideModal !== 'undefined') hideModal();
        renderUserManagement();
        
        showMessage('Success', `User with email ${userEmail} has been deleted successfully.`);
    } else {
        if (typeof hideModal !== 'undefined') hideModal();
        showMessage('Error',` User with email ${userEmail} not found.`);
    }
}
window.confirmDeleteUser = confirmDeleteUser;

// =======================================================
// 6. CONTENT RENDERING (Conceptual: content_renders.js) - END
// =======================================================
