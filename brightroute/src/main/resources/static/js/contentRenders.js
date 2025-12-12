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

        function renderUserManagement() {
            const container = document.getElementById('user-table-body');
            if (!container) return;

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
                            <a href="#" class="text-primary hover:text-gray-800" onclick="showMessage('Edit User', 'Editing user: ${user.email}')">Edit</a> | 
                            <a href="#" class="text-red-600 hover:text-red-900" onclick="showMessage('Delete User', 'Confirm deletion of: ${user.email}')">Delete</a>
                        </td>
                    </tr>
                `;
            }).join('');

            container.innerHTML = rows;
        }

        function renderContentManagement() {
            const container = document.getElementById('course-table-body');
            if (!container) return;

            const rows = mockCourses.map(course => {
                let statusBadge;
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

                statusBadge = `<span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${statusColor}">${course.status}</span>`;

                return `
                    <tr>
                        <td class="px-6 py-4 whitespace-nowrap">${course.title}</td>
                        <td class="px-6 py-4 whitespace-nowrap">${course.instructor}</td>
                        <td class="px-6 py-4 whitespace-nowrap">${course.lectures}</td>
                        <td class="px-6 py-4 whitespace-nowrap">${statusBadge}</td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                            <a href="#" class="text-primary hover:text-gray-800" onclick="showMessage('Edit Course', 'Editing course: ${course.title}')">Edit</a> | 
                            <a href="#" class="text-red-600 hover:text-red-900" onclick="showMessage('Archive Course', 'Confirm archiving: ${course.title}')">Archive</a>
                        </td>
                    </tr>
                `;
            }).join('');

            container.innerHTML = rows;
        }

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
        // =======================================================
        // 6. CONTENT RENDERING (Conceptual: content_renderers.js) - END
        // =======================================================
