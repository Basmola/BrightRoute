// 1. Dashboard Overview
function renderStudentDashboard() {
    const container = document.getElementById('student-dashboard-view');
    const user = getLoggedInUser();

    // Stats Logic (Mock)
    const activeCourses = state.courses.filter(c => c.progress > 0 && c.progress < 100).length;
    const completedCourses = state.courses.filter(c => c.progress === 100).length;
    const avgScore = 88; // Mock

    container.innerHTML = `
        <!-- Welcome Banner -->
        <div class="bg-gradient-to-r from-brand-900 to-brand-800 rounded-2xl p-6 lg:p-10 text-white shadow-lg relative overflow-hidden mb-6">
            <div class="relative z-10">
                <h2 class="text-3xl font-bold mb-2">Welcome back, ${user.firstName}!</h2>
                <button onclick="navigate('my-courses-view')" class="mt-6 px-6 py-2 bg-accent-DEFAULT hover:bg-accent-hover text-white rounded-lg font-medium transition-colors shadow-lg shadow-blue-900/50">
                    Continue Learning
                </button>
            </div>
            <i class="ph ph-rocket absolute right-4 bottom-4 text-9xl text-white opacity-5 transform rotate-12"></i>
        </div>

        <!-- Recent Activity / In Progress -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <h3 class="text-lg font-bold text-gray-900 mb-6">Courses Learning</h3>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                ${renderCourseCards(state.courses.slice(0, 3))}
            </div>
        </div>
    `;
}

// 2. My Courses View
function renderMyCourses() {
    const container = document.getElementById('my-courses-view');
    const courses = state.courses;

    container.innerHTML = `
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <h2 class="text-2xl font-bold text-gray-900 mb-6">My Enrolled Courses</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                ${renderCourseCards(courses)}
            </div>
        </div>
    `;
}

// 3. Course Detail (Lectures)
function renderCourseDetail(courseId) {
    const container = document.getElementById('course-detail-view');
    const course = state.courses.find(c => c.id === courseId);

    if (!course) return;

    // Build lectures list
    const lecturesList = course.lectures ? course.lectures.map((lecture, index) => {
        // VISUAL: Ensure all look unlocked (Play icon, Blue color) by default
        let iconColor = 'text-accent-DEFAULT';
        let iconType = 'ph-play-circle';
        let statusText = lecture.status;
        
        // Handle visual status text: Replace "Locked" with "Start"
        if (statusText === 'Locked') {
            statusText = 'Start';
        }

        // Only change visual style if explicitly completed
        // if (lecture.status === 'Completed') { 
        //     iconColor = 'text-green-500'; 
        //     iconType = 'ph-check-circle'; 
        // }

        // Interaction: onclick triggers promptAccessCode
        return `
            <div onclick="promptAccessCode('${lecture.id}')" class="flex items-center justify-between p-4 bg-gray-50 rounded-lg border border-gray-200 hover:bg-gray-100 transition-colors cursor-pointer">
                <div class="flex items-center gap-4">
                    <div class="text-lg font-bold text-gray-400">0${index + 1}</div>
                    <div>
                        <h4 class="font-semibold text-gray-800">${lecture.title}</h4>
                        <span class="text-xs text-gray-500">${statusText}</span>
                    </div>
                </div>
                <i class="ph-fill ${iconType} ${iconColor} text-2xl"></i>
            </div>
        `;
    }).join('') : '<p class="text-gray-500">No lectures available.</p>';

    container.innerHTML = `
        <button onclick="navigate('my-courses-view')" class="mb-4 text-sm text-gray-500 hover:text-accent-DEFAULT flex items-center gap-1">
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
    
    document.querySelectorAll('.view-section').forEach(el => el.classList.add('hidden'));
    container.classList.remove('hidden');
}

// Helper Card Renderer
function renderCourseCards(coursesList) {
    return coursesList.map(course => `
        <div class="bg-white border border-gray-200 rounded-xl overflow-hidden hover:shadow-md transition-shadow cursor-pointer group" onclick="renderCourseDetail('${course.id}')">
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

function showModal(title, body, actions) {
    document.getElementById('modal-title').innerHTML = title;
    document.getElementById('modal-body').innerHTML = body;
    document.getElementById('modal-actions').innerHTML = actions;

    document.getElementById('modal').classList.remove('hidden');
    document.getElementById('modal-backdrop').classList.remove('hidden');
}

function hideModal() {
    document.getElementById('modal').classList.add('hidden');
    document.getElementById('modal-backdrop').classList.add('hidden');
}


// --- ACCESS CODE & PLAYER LOGIC ---

window.promptAccessCode = function(lectureId) {
    // Shows popup asking for code
    showModal('Unlock Lecture', `
        <p class="mb-4 text-gray-600">Please enter the access code to view the lecture parts.</p>
        <div class="space-y-2">
            <label class="text-xs font-semibold text-gray-500 uppercase">Access Code</label>
            <input type="text" id="access-code-input" class="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-accent-DEFAULT focus:border-accent-DEFAULT outline-none" placeholder="Enter access code...">
        </div>
    `, `
        <button onclick="hideModal()" class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">Cancel</button>
        <button onclick="validateLectureAccess('${lectureId}')" class="px-4 py-2 bg-brand-900 text-white rounded-lg hover:bg-brand-800 transition-colors shadow-lg">Access Content</button>
    `);
}

window.validateLectureAccess = function(lectureId) {
    const input = document.getElementById('access-code-input').value.trim();

    let foundLecture = null;

    for (const c of state.courses) {
        if (c.lectures) {
            const l = c.lectures.find(lec => lec.id === lectureId);
            if (l) {
                foundLecture = l;
                break;
            }
        }
    }

    if (!foundLecture) return;

    // Check if the input matches the lecture's access code or if it's '12345' as a generic fallback for testing
    // Since mock data might not have accessCode property set for all, we allow '12345'
    if (input === (foundLecture.accessCode || '12345')) {
        hideModal();
        renderLecturePlayer(lectureId);
    } else {
        alert('❌ Access Code incorrect');
    }
};


window.renderLecturePlayer = function(lectureId) {
    let foundLecture = null;
    let foundCourse = null;
    
    // Find the lecture data
    for (const c of state.courses) {
        if (c.lectures) {
            const l = c.lectures.find(lec => lec.id === lectureId);
            if (l) {
                foundLecture = l;
                foundCourse = c;
                break;
            }
        }
    }

    if (!foundLecture) return;

    const container = document.getElementById('lecture-detail-view');
    
    // Render the Lecture Parts: 3 Videos and 1 PDF (Accordion Style)
    container.innerHTML = `
        <button onclick="renderCourseDetail('${foundCourse.id}')" class="mb-4 text-sm text-gray-500 hover:text-accent-DEFAULT flex items-center gap-1">
            <i class="ph-bold ph-arrow-left"></i> Back to Lectures
        </button>

        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
            <div class="p-6 border-b border-gray-100 flex justify-between items-start">
                <div>
                    <h2 class="text-2xl font-bold text-gray-900">${foundLecture.title}</h2>
                    <p class="text-gray-500 text-sm mt-1">${foundCourse.title}</p>
                </div>
                <span class="bg-blue-100 text-blue-700 px-3 py-1 rounded-full text-xs font-bold">Access Granted</span>
            </div>
            
            <div class="p-6 space-y-4">
                
                <!-- Part 1: Basics -->
                <div class="border border-gray-200 rounded-xl overflow-hidden">
                    <button onclick="toggleLecturePart('part-1')" class="w-full flex justify-between items-center bg-gray-50 px-4 py-3 font-bold text-gray-800 hover:bg-gray-100 transition-colors text-left">
                        <span>Part 1: Introduction & Basics</span>
                        <i id="icon-part-1" class="ph-bold ph-caret-down transition-transform duration-300"></i>
                    </button>
                    <div id="part-1" class="hidden border-t border-gray-200">
                        <div class="aspect-video bg-gray-900 flex flex-col items-center justify-center relative group cursor-pointer">
                            <div class="w-16 h-16 bg-white/20 backdrop-blur-md rounded-full flex items-center justify-center group-hover:scale-110 transition-transform z-10">
                                <i class="ph-fill ph-play text-white text-3xl ml-1"></i>
                            </div>
                            <p class="mt-2 text-white text-sm">Watch Video 1</p>
                        </div>
                    </div>
                </div>

                <!-- Part 2: Loops -->
                <div class="border border-gray-200 rounded-xl overflow-hidden">
                    <button onclick="toggleLecturePart('part-2')" class="w-full flex justify-between items-center bg-gray-50 px-4 py-3 font-bold text-gray-800 hover:bg-gray-100 transition-colors text-left">
                        <span>Part 2: Core Logic & Loops</span>
                        <i id="icon-part-2" class="ph-bold ph-caret-down transition-transform duration-300"></i>
                    </button>
                    <div id="part-2" class="hidden border-t border-gray-200">
                        <div class="aspect-video bg-gray-900 flex flex-col items-center justify-center relative group cursor-pointer">
                            <div class="w-16 h-16 bg-white/20 backdrop-blur-md rounded-full flex items-center justify-center group-hover:scale-110 transition-transform z-10">
                                <i class="ph-fill ph-play text-white text-3xl ml-1"></i>
                            </div>
                            <p class="mt-2 text-white text-sm">Watch Video 2</p>
                        </div>
                    </div>
                </div>

                <!-- Part 3: Advanced -->
                <div class="border border-gray-200 rounded-xl overflow-hidden">
                    <button onclick="toggleLecturePart('part-3')" class="w-full flex justify-between items-center bg-gray-50 px-4 py-3 font-bold text-gray-800 hover:bg-gray-100 transition-colors text-left">
                        <span>Part 3: Advanced Topics</span>
                        <i id="icon-part-3" class="ph-bold ph-caret-down transition-transform duration-300"></i>
                    </button>
                    <div id="part-3" class="hidden border-t border-gray-200">
                        <div class="aspect-video bg-gray-900 flex flex-col items-center justify-center relative group cursor-pointer">
                            <div class="w-16 h-16 bg-white/20 backdrop-blur-md rounded-full flex items-center justify-center group-hover:scale-110 transition-transform z-10">
                                <i class="ph-fill ph-play text-white text-3xl ml-1"></i>
                            </div>
                            <p class="mt-2 text-white text-sm">Watch Video 3</p>
                        </div>
                    </div>
                </div>

                <!-- Part 4: PDF Material -->
                <div class="border border-gray-200 rounded-xl overflow-hidden">
                    <button onclick="toggleLecturePart('part-4')" class="w-full flex justify-between items-center bg-gray-50 px-4 py-3 font-bold text-gray-800 hover:bg-gray-100 transition-colors text-left">
                        <span>Part 4: Lecture Materials (PDF)</span>
                        <i id="icon-part-4" class="ph-bold ph-caret-down transition-transform duration-300"></i>
                    </button>
                    <div id="part-4" class="hidden border-t border-gray-200">
                        <div class="p-6 flex items-center justify-between hover:bg-gray-50 transition-colors">
                            <div class="flex items-center gap-4">
                                <div class="w-12 h-12 bg-red-100 text-red-600 rounded-lg flex items-center justify-center">
                                    <i class="ph-fill ph-file-pdf text-3xl"></i>
                                </div>
                                <div>
                                    <h4 class="font-bold text-gray-900">Lecture Notes & Slides</h4>
                                    <p class="text-sm text-gray-500">Comprehensive guide for this lecture.</p>
                                </div>
                            </div>
                            <button class="px-4 py-2 bg-brand-900 text-white rounded-lg hover:bg-brand-800 transition shadow-sm flex items-center gap-2">
                                <i class="ph-bold ph-download-simple"></i> Download PDF
                            </button>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    `;

    document.querySelectorAll('.view-section').forEach(el => el.classList.add('hidden'));
    container.classList.remove('hidden');
    document.getElementById('content-area').scrollTo(0,0);
}

// Function to handle opening/closing lecture parts
window.toggleLecturePart = function(partId) {
    const partContent = document.getElementById(partId);
    const icon = document.getElementById('icon-' + partId);
    
    if (partContent.classList.contains('hidden')) {
        // Open
        partContent.classList.remove('hidden');
        icon.classList.add('rotate-180');
    } else {
        // Close
        partContent.classList.add('hidden');
        icon.classList.remove('rotate-180');
    }
}