
const API_BASE_URL = 'http://localhost:7070/api/access-codes';
const COURSES_API_URL = 'http://localhost:7070/api/courses';

let allAccessCodes = [];
let allCourses = [];

// Fetch data on load
document.addEventListener('DOMContentLoaded', async () => {
    await Promise.all([fetchAccessCodes(), fetchCourses()]);

    // Search functionality
    const searchInput = document.getElementById('access-code-search-input');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase();
            const filteredCodes = allAccessCodes.filter(code =>
                code.codeValue.toLowerCase().includes(searchTerm) ||
                (code.course && code.course.courseTitle.toLowerCase().includes(searchTerm)) ||
                (code.usedBy && (code.usedBy.firstName.toLowerCase().includes(searchTerm) || code.usedBy.lastName.toLowerCase().includes(searchTerm)))
            );
            renderAccessCodeTable(filteredCodes);
        });
    }
});

async function fetchAccessCodes() {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error('Failed to fetch access codes');
        allAccessCodes = await response.json();
        renderAccessCodeTable(allAccessCodes);
    } catch (error) {
        console.error('Error fetching access codes:', error);
        alert('Failed to load access codes.');
    }
}

async function fetchCourses() {
    try {
        const response = await fetch(COURSES_API_URL);
        if (!response.ok) throw new Error('Failed to fetch courses');
        allCourses = await response.json();
    } catch (error) {
        console.error('Error fetching courses:', error);
    }
}

function renderAccessCodeTable(codesToRender = allAccessCodes) {
    const tableBody = document.getElementById('access-code-table-body');
    if (!tableBody) return;

    if (codesToRender.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="6" class="px-6 py-4 text-center text-gray-500">No access codes found.</td></tr>';
        return;
    }

    tableBody.innerHTML = codesToRender.map(code => `
        <tr>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${code.codeValue}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${code.course ? code.course.courseTitle : 'N/A'}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${code.lecture ? code.lecture.lectureTitle : 'Any'}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${code.codeIsUsed ? 'bg-red-100 text-red-800' : 'bg-green-100 text-green-800'}">
                    ${code.codeIsUsed ? 'USED' : 'ACTIVE'}
                </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${code.usedBy ? code.usedBy.firstName + ' ' + code.usedBy.lastName : '-'}</td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                ${!code.codeIsUsed ? `<button onclick="revokeAccessCode(${code.accessCodeId})" class="text-red-600 hover:text-red-900">Revoke</button>` : ''}
            </td>
        </tr>
    `).join('');
}

function openCreateAccessCodeModal() {
    showModal('Create Access Code');
}

function showModal(title) {
    const modal = document.getElementById('custom-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalMessage = document.getElementById('modal-message');
    const modalActions = document.getElementById('modal-actions');

    modalTitle.textContent = title;

    const courseOptions = allCourses.map(course => `<option value="${course.courseId}">${course.courseTitle}</option>`).join('');

    modalMessage.innerHTML = `
        <form id="access-code-form" class="space-y-4">
            <div>
                <label class="block text-sm font-medium text-gray-700">Course</label>
                <select id="code-course" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" required>
                    <option value="">Select a course</option>
                    ${courseOptions}
                </select>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Custom Code (Optional)</label>
                <input type="text" id="code-value" placeholder="Leave blank for auto-generated" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2">
            </div>
        </form>
    `;

    modalActions.innerHTML = `
        <button onclick="closeModal()" class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition mr-2">Cancel</button>
        <button onclick="createAccessCode()" class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-gray-800 transition">Create</button>
    `;

    modal.classList.remove('hidden');
}

function closeModal() {
    document.getElementById('custom-modal').classList.add('hidden');
}

async function createAccessCode() {
    const courseId = document.getElementById('code-course').value;
    const codeValue = document.getElementById('code-value').value;

    if (!courseId) {
        alert('Please select a course.');
        return;
    }

    const params = new URLSearchParams();
    params.append('courseId', courseId);
    if (codeValue) params.append('codeValue', codeValue);

    try {
        const response = await fetch(`${API_BASE_URL}?${params.toString()}`, {
            method: 'POST'
        });

        if (response.ok) {
            closeModal();
            fetchAccessCodes();
        } else {
            const errorText = await response.text();
            alert('Error creating access code: ' + errorText);
        }
    } catch (error) {
        console.error('API Error:', error);
        alert('Failed to create access code.');
    }
}

async function revokeAccessCode(id) {
    if (!confirm('Are you sure you want to revoke this access code?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            fetchAccessCodes();
        } else {
            alert('Failed to revoke access code.');
        }
    } catch (error) {
        console.error('API Error:', error);
        alert('Error revoking access code.');
    }
}
