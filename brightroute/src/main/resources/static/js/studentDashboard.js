// 1. Dashboard Overview
function renderStudentDashboard() {
    const container = document.getElementById('student-dashboard-view');
    const user = getLoggedInUser();

    // Stats Logic (Mock)
    const activeCourses = state.courses.filter(c => c.progress > 0 && c.progress < 100).length;
    const completedCourses = state.courses.filter(c => c.progress === 100).length;
    const avgScore = 88; // Mock

    // Render all courses or a message if empty
    const coursesHtml = state.courses.length > 0
        ? renderCourseCards(state.courses)
        : '<div class="col-span-full text-center py-10 text-gray-500">No courses available at the moment.</div>';

    container.innerHTML = `
        <!-- Welcome Banner -->
        <div class="bg-gradient-to-r from-brand-900 to-brand-800 rounded-2xl p-6 lg:p-10 text-white shadow-lg relative overflow-hidden mb-6">
            <div class="relative z-10">
                <h2 class="text-3xl font-bold mb-2">Welcome back, ${user.firstName}!</h2>
                <p class="text-gray-300 mb-6">You have ${state.courses.length} courses available.</p>
                <button onclick="navigate('my-courses-view')" class="px-6 py-2 bg-accent-DEFAULT hover:bg-accent-hover text-white rounded-lg font-medium transition-colors shadow-lg shadow-blue-900/50">
                    View My Courses
                </button>
            </div>
            <i class="ph ph-rocket absolute right-4 bottom-4 text-9xl text-white opacity-5 transform rotate-12"></i>
        </div>

        <!-- Stats & Charts Section -->
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
            <!-- Counters -->
            <div class="space-y-6">
                <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 flex items-center gap-4">
                    <div class="w-12 h-12 rounded-full bg-blue-100 text-blue-600 flex items-center justify-center text-2xl">
                        <i class="ph-fill ph-books"></i>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500 font-medium">Subscribed Courses</p>
                        <h3 class="text-2xl font-bold text-gray-900" id="subscribed-count">0</h3>
                    </div>
                </div>
                <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 flex items-center gap-4">
                    <div class="w-12 h-12 rounded-full bg-green-100 text-green-600 flex items-center justify-center text-2xl">
                        <i class="ph-fill ph-video"></i>
                    </div>
                    <div>
                        <p class="text-sm text-gray-500 font-medium">Enrolled Lectures</p>
                        <h3 class="text-2xl font-bold text-gray-900" id="enrolled-count">0</h3>
                    </div>
                </div>
            </div>

            <!-- Chart -->
            <div class="lg:col-span-2 bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                <h3 class="text-lg font-bold text-gray-900 mb-4">Activity Overview</h3>
                <div class="h-64">
                    <canvas id="activityChart"></canvas>
                </div>
            </div>
        </div>

        <!-- All Courses Grid -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <div class="flex items-center justify-between mb-6">
                <h3 class="text-lg font-bold text-gray-900">Available Courses</h3>
                <span class="text-sm text-gray-500">${state.courses.length} Courses</span>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                ${coursesHtml}
            </div>
        </div>
    `;

    // Fetch and Render Stats
    fetchDashboardStats(user.id);
}
window.renderStudentDashboard = renderStudentDashboard;

async function fetchDashboardStats(userId) {
    let subscribedCount = 0;
    let enrolledLecturesCount = 0;

    try {
        const [coursesRes, enrollmentsRes] = await Promise.all([
            fetch(`http://localhost:7070/api/course-subscription/user/${userId}`),
            fetch(`http://localhost:7070/api/enrollment/user/${userId}`)
        ]);

        if (coursesRes.ok) {
            const courses = await coursesRes.json();
            subscribedCount = courses.length;
        }

        if (enrollmentsRes.ok) {
            const enrollments = await enrollmentsRes.json();
            enrolledLecturesCount = enrollments.length;
        }
    } catch (error) {
        console.error('Error fetching dashboard stats:', error);
    }

    // Update Counters
    const subEl = document.getElementById('subscribed-count');
    const enrollEl = document.getElementById('enrolled-count');
    if (subEl) subEl.textContent = subscribedCount;
    if (enrollEl) enrollEl.textContent = enrolledLecturesCount;

    // Render Chart
    const ctx = document.getElementById('activityChart');
    if (ctx) {
        new Chart(ctx.getContext('2d'), {
            type: 'bar',
            data: {
                labels: ['Subscribed Courses', 'Enrolled Lectures'],
                datasets: [{
                    label: 'Count',
                    data: [subscribedCount, enrolledLecturesCount],
                    backgroundColor: [
                        'rgba(59, 130, 246, 0.8)', // Blue
                        'rgba(16, 185, 129, 0.8)'  // Green
                    ],
                    borderRadius: 6,
                    borderSkipped: false
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: { display: false }
                    },
                    x: {
                        grid: { display: false }
                    }
                }
            }
        });
    }
}

// 2. My Courses View
async function renderMyCourses() {
    const container = document.getElementById('my-courses-view');

    // Ensure we have the latest subscribed courses
    const courses = await fetchSubscribedCourses();

    if (courses && courses.length > 0) {
        container.innerHTML = `
            <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                <h2 class="text-2xl font-bold text-gray-900 mb-6">My Enrolled Courses</h2>
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    ${renderCourseCards(courses)}
                </div>
            </div>
        `;
    } else {
        container.innerHTML = `
            <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 text-center">
                <h2 class="text-2xl font-bold text-gray-900 mb-4">My Enrolled Courses</h2>
                <div class="py-12">
                    <i class="ph ph-student text-4xl text-gray-300 mb-3"></i>
                    <p class="text-gray-500 text-lg">You haven't enrolled in any courses yet.</p>
                    <button onclick="navigate('student-dashboard-view')" class="mt-4 px-6 py-2 bg-accent-DEFAULT text-white rounded-lg hover:bg-accent-hover transition-colors">
                        Browse Courses
                    </button>
                </div>
            </div>
        `;
    }
}
window.renderMyCourses = renderMyCourses;

// 3. Course Detail (Lectures)
async function renderCourseDetail(courseId) {
    const container = document.getElementById('course-detail-view');
    const course = state.courses.find(c => c.id === courseId) || state.subscribedCourses.find(c => c.id === courseId);
    const user = getLoggedInUser();

    if (!course) return;

    // Show loading state
    container.innerHTML = `
        <div class="flex justify-center items-center py-20">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-accent-DEFAULT"></div>
        </div>
    `;
    document.querySelectorAll('.view-section').forEach(el => el.classList.add('hidden'));
    container.classList.remove('hidden');

    // Fetch lectures and user enrollments in parallel
    let lectures = [];
    let enrollments = [];
    try {
        const [lecturesRes, enrollmentsRes] = await Promise.all([
            fetch(`http://localhost:7070/api/courses/${courseId}/lectures`),
            fetch(`http://localhost:7070/api/enrollment/user/${user.id}`)
        ]);

        if (lecturesRes.ok) {
            lectures = await lecturesRes.json();
            console.log('Fetched lectures:', lectures);
        }
        if (enrollmentsRes.ok) {
            enrollments = await enrollmentsRes.json();
            console.log('Fetched user enrollments:', enrollments);
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }

    // Build lectures list
    const lecturesList = lectures && lectures.length > 0 ? lectures.map((lecture, index) => {
        // Check if user is enrolled in this lecture
        // FIX: Use lectureId from EnrollmentDTO
        const isEnrolled = enrollments.some(e => e.lectureId === lecture.id);

        // Determine action based on enrollment
        const clickAction = isEnrolled
            ? `renderLecturePlayer(${lecture.id}, ${courseId})`
            : `promptAccessCode(${lecture.id}, ${courseId})`;

        const statusIcon = isEnrolled
            ? '<i class="ph-fill ph-check-circle text-green-500 text-2xl"></i>'
            : '<i class="ph-fill ph-lock-key text-gray-400 text-2xl"></i>';

        const statusText = isEnrolled
            ? '<span class="text-xs text-green-600 font-bold">Enrolled</span>'
            : '<span class="text-xs text-gray-500">Locked</span>';

        return `
            <div onclick="${clickAction}" class="flex items-center justify-between p-4 bg-gray-50 rounded-lg border border-gray-200 hover:bg-gray-100 transition-colors cursor-pointer">
                <div class="flex items-center gap-4">
                    <div class="text-lg font-bold text-gray-400">0${index + 1}</div>
                    <div>
                        <h4 class="font-semibold text-gray-800">${lecture.lectureTitle}</h4>
                        <div class="flex items-center gap-2">
                            ${statusText}
                        </div>
                    </div>
                </div>
                ${statusIcon}
            </div>
        `;
    }).join('') : '<p class="text-gray-500">No lectures available for this course.</p>';

    container.innerHTML = `
        <button onclick="renderCourseDetail(${courseId})" class="mb-4 text-sm text-gray-500 hover:text-accent-DEFAULT flex items-center gap-1">
            <i class="ph-bold ph-arrow-left"></i> Back to Courses
        </button>

        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="h-48 bg-gray-800 relative">
                <img src="${course.image}" class="w-full h-full object-cover opacity-50">
                <div class="absolute bottom-0 left-0 p-6 text-white">
                    <span class="bg-accent-DEFAULT px-2 py-1 rounded text-xs font-bold mb-2 inline-block">Level ${course.level}</span>
                    <h1 class="text-3xl font-bold">${course.title}</h1>
                    <p class="text-gray-200 text-sm mt-1">Instructor: ${course.instructor}</p>
                </div>
            </div>
            
            <div class="p-6 grid grid-cols-1 lg:grid-cols-3 gap-8">
                <!-- Course Info -->
                <div class="lg:col-span-2 space-y-6">
                    <div>
                        <h3 class="text-xl font-bold text-gray-900 mb-2">About this Course</h3>
                        <p class="text-gray-600 leading-relaxed">${course.description}</p>
                    </div>
                    
                    <div>
                        <h3 class="text-xl font-bold text-gray-900 mb-4">Lectures Content</h3>
                        <div class="space-y-3">
                            ${lecturesList}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;
}
window.renderCourseDetail = renderCourseDetail;

// Helper Card Renderer
function renderCourseCards(coursesList) {
    return coursesList.map(course => `
        <div class="bg-white border border-gray-200 rounded-xl overflow-hidden hover:shadow-md transition-shadow cursor-pointer group" onclick="renderCourseDetail(${course.id})">
            <div class="h-32 bg-gray-200 relative overflow-hidden">
                <img src="${course.image}" alt="${course.title}" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500">
                <div class="absolute top-2 right-2 bg-white/90 backdrop-blur-sm px-2 py-1 rounded text-xs font-bold text-gray-800">
                    Level ${course.level}
                </div>
            </div>
            <div class="p-4">
                <h4 class="font-bold text-gray-900 mb-1 truncate">${course.title}</h4>
                <p class="text-xs text-gray-500 mb-3">By ${course.instructor}</p>
                
                <div class="w-full bg-gray-100 rounded-full h-2 overflow-hidden">
                </div>
            </div>
        </div>
    `).join('');
}
window.renderCourseCards = renderCourseCards;

// --- ACCESS CODE & PLAYER LOGIC ---

window.promptAccessCode = function (lectureId, courseId) {
    // Shows popup asking for code
    showModal('Unlock Lecture', `
        <p class="mb-4 text-gray-600">Please enter the access code to view the lecture parts.</p>
        <div class="space-y-2">
            <label class="text-xs font-semibold text-gray-500 uppercase">Access Code</label>
            <input type="text" id="access-code-input" class="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-accent-DEFAULT focus:border-accent-DEFAULT outline-none" placeholder="Enter access code...">
        </div>
    `, `
        <button onclick="hideModal()" class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">Cancel</button>
        <button onclick="validateLectureAccess(${lectureId}, ${courseId})" class="px-4 py-2 bg-brand-900 text-white rounded-lg hover:bg-brand-800 transition-colors shadow-lg">Access Content</button>
    `);
}

window.validateLectureAccess = async function (lectureId, courseId) {
    const input = document.getElementById('access-code-input').value.trim();
    const user = getLoggedInUser();

    if (!input) {
        alert('Please enter an access code');
        return;
    }

    // Redeem access code via API
    try {
        const response = await fetch(`http://localhost:7070/api/access-codes/redeem?codeValue=${encodeURIComponent(input)}&userId=${user.id}&lectureId=${lectureId}`, {
            method: 'POST'
        });

        if (response.ok) {
            const redeemedCode = await response.json();
            console.log('Access code redeemed:', redeemedCode);
            hideModal();
            renderLecturePlayer(lectureId, courseId);
        } else {
            let errorMessage = 'Access Code incorrect or already used';
            try {
                const errorData = await response.json();
                if (errorData.message) {
                    errorMessage = errorData.message;
                }
            } catch (e) {
                const text = await response.text();
                if (text) errorMessage = text;
            }

            console.error('Redeem error:', errorMessage);
            alert('❌ ' + errorMessage);
        }
    } catch (error) {
        console.error('Error redeeming access code:', error);
        alert('Error validating access code. Please try again.');
    }
};

window.renderLecturePlayer = async function (lectureId, courseId) {
    const container = document.getElementById('lecture-detail-view');

    // Show loading state
    container.innerHTML = `
        <div class="flex justify-center items-center py-20">
            <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-accent-DEFAULT"></div>
        </div>
    `;
    document.querySelectorAll('.view-section').forEach(el => el.classList.add('hidden'));
    container.classList.remove('hidden');

    // Fetch lecture details with parts
    let lecture = null;
    try {
        const response = await fetch(`http://localhost:7070/api/lectures/${lectureId}`);
        if (response.ok) {
            lecture = await response.json();
            console.log('Fetched lecture with parts:', lecture);
        }
    } catch (error) {
        console.error('Error fetching lecture:', error);
    }

    if (!lecture) {
        container.innerHTML = '<p class="text-red-500">Error loading lecture</p>';
        return;
    }

    // Build lecture parts list
    // FIX: Use correct field names for LecturePart
    const lectureParts = lecture.parts && lecture.parts.length > 0
        ? lecture.parts.map((part, index) => {
            const isVideo = part.partType === 'VIDEO';
            const isPdf = part.partType === 'PDF';

            return `
                <div class="border border-gray-200 rounded-xl overflow-hidden">
                    <button onclick="toggleLecturePart('part-${part.id}')" class="w-full flex justify-between items-center bg-gray-50 px-4 py-3 font-bold text-gray-800 hover:bg-gray-100 transition-colors text-left">
                        <span>Part ${index + 1}: ${part.partDescription || 'No Description'}</span>
                        <i id="icon-part-${part.id}" class="ph-bold ph-caret-down transition-transform duration-300"></i>
                    </button>
                    <div id="part-${part.id}" class="hidden border-t border-gray-200">
                        ${isVideo ? `
                            <div class="aspect-video bg-gray-900">
                                <video controls class="w-full h-full">
                                    <source src="${part.partContentUrl}" type="video/mp4">
                                    Your browser does not support the video tag.
                                </video>
                            </div>
                        ` : isPdf ? `
                            <div class="bg-gray-50 p-6">
                                <div class="flex items-center gap-4 p-4 bg-white border border-gray-200 rounded-lg hover:shadow-md transition-shadow cursor-pointer" onclick="window.open('${part.partContentUrl}', '_blank')">
                                    <div class="w-12 h-12 bg-red-100 text-red-600 rounded-lg flex items-center justify-center text-2xl">
                                        <i class="ph-fill ph-file-pdf"></i>
                                    </div>
                                    <div>
                                        <h4 class="font-bold text-gray-900">${part.partDescription || 'PDF Document'}</h4>
                                        <p class="text-xs text-gray-500">PDF Document</p>
                                    </div>
                                    <i class="ph-bold ph-download-simple text-gray-400 ml-auto"></i>
                                </div>
                            </div>
                        ` : `<p class="p-4 text-gray-500">Unsupported content type</p>`}
                    </div>
                </div>
            `;
        }).join('')
        : '<p class="text-gray-500">No lecture parts available.</p>';

    container.innerHTML = `
        <button onclick="renderCourseDetail(${courseId})" class="mb-4 text-sm text-gray-500 hover:text-accent-DEFAULT flex items-center gap-1">
            <i class="ph-bold ph-arrow-left"></i> Back to Lectures
        </button>

        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-6 border-b border-gray-100 flex justify-between items-start">
                <div>
                    <h2 class="text-2xl font-bold text-gray-900">${lecture.lectureTitle}</h2>
                    <p class="text-gray-500 text-sm mt-1">Course ID: ${courseId}</p>
                </div>
                <span class="bg-blue-100 text-blue-700 px-3 py-1 rounded-full text-xs font-bold">Access Granted</span>
            </div>
            
            <div class="p-6 space-y-4">
                ${lectureParts}
            </div>
        </div>
    `;
}
window.renderLecturePlayer = renderLecturePlayer;

window.toggleLecturePart = function (partId) {
    const content = document.getElementById(partId);
    const icon = document.getElementById('icon-' + partId);

    if (content.classList.contains('hidden')) {
        content.classList.remove('hidden');
        icon.style.transform = 'rotate(180deg)';
    } else {
        content.classList.add('hidden');
        icon.style.transform = 'rotate(0deg)';
    }
}