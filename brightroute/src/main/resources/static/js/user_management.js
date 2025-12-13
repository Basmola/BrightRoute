
const API_BASE_URL = 'http://localhost:7070/api/users';

let allUsers = [];

// Fetch users on load
document.addEventListener('DOMContentLoaded', async () => {
    await fetchUsers();

    // Search functionality
    const searchInput = document.getElementById('user-search-input');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const searchTerm = e.target.value.toLowerCase();
            const filteredUsers = allUsers.filter(user =>
                user.firstName.toLowerCase().includes(searchTerm) ||
                user.lastName.toLowerCase().includes(searchTerm) ||
                user.email.toLowerCase().includes(searchTerm)
            );
            renderUserTable(filteredUsers);
        });
    }
});

async function fetchUsers() {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error('Failed to fetch users');
        allUsers = await response.json();
        renderUserTable(allUsers);
    } catch (error) {
        console.error('Error fetching users:', error);
        alert('Failed to load users.');
    }
}

function renderUserTable(usersToRender = allUsers) {
    const tableBody = document.getElementById('user-table-body');
    if (!tableBody) return;

    if (usersToRender.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="5" class="px-6 py-4 text-center text-gray-500">No users found.</td></tr>';
        return;
    }

    tableBody.innerHTML = usersToRender.map(user => `
        <tr>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${user.firstName} ${user.lastName}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${user.email}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${user.role}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusClass(user.accountStatus)}">
                    ${user.accountStatus}
                </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button onclick="openEditUserModal(${user.id})" class="text-indigo-600 hover:text-indigo-900 mr-3">Edit</button>
                <button onclick="confirmDeleteUser(${user.id})" class="text-red-600 hover:text-red-900">Delete</button>
            </td>
        </tr>
    `).join('');
}

function getStatusClass(status) {
    switch (status) {
        case 'ACTIVE': return 'bg-green-100 text-green-800';
        case 'INACTIVE': return 'bg-gray-100 text-gray-800';
        case 'SUSPENDED': return 'bg-red-100 text-red-800';
        default: return 'bg-gray-100 text-gray-800';
    }
}

function openEditUserModal(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (user) {
        showModal('Edit User', user);
    }
}

function showModal(title, userData) {
    const modal = document.getElementById('custom-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalMessage = document.getElementById('modal-message'); // We repurpose this for the form
    const modalActions = document.getElementById('modal-actions');

    modalTitle.textContent = title;

    const userId = userData.id;
    const firstName = userData.firstName;
    const lastName = userData.lastName;
    const phoneNumber = userData.phoneNumber;
    const role = userData.role;
    const status = userData.accountStatus;

    modalMessage.innerHTML = `
        <form id="user-form" class="space-y-4">
            <input type="hidden" id="user-id" value="${userId}">
            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">First Name</label>
                    <input type="text" id="user-first-name" value="${firstName}" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" required>
                </div>
                <div>
                    <label class="block text-sm font-medium text-gray-700">Last Name</label>
                    <input type="text" id="user-last-name" value="${lastName}" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" required>
                </div>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Phone Number</label>
                <input type="text" id="user-phone" value="${phoneNumber}" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2" required>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Role</label>
                <select id="user-role" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2">
                    <option value="ADMIN" ${role === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                    <option value="INSTRUCTOR" ${role === 'INSTRUCTOR' ? 'selected' : ''}>INSTRUCTOR</option>
                    <option value="STUDENT" ${role === 'STUDENT' ? 'selected' : ''}>STUDENT</option>
                </select>
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700">Status</label>
                <select id="user-status" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm border p-2">
                    <option value="ACTIVE" ${status === 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
                    <option value="INACTIVE" ${status === 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
                    <option value="SUSPENDED" ${status === 'SUSPENDED' ? 'selected' : ''}>SUSPENDED</option>
                </select>
            </div>
        </form>
    `;

    modalActions.innerHTML = `
        <button onclick="closeModal()" class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition mr-2">Cancel</button>
        <button onclick="saveUser()" class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-gray-800 transition">Save</button>
    `;

    modal.classList.remove('hidden');
}

function closeModal() {
    document.getElementById('custom-modal').classList.add('hidden');
}

async function saveUser() {
    const id = document.getElementById('user-id').value;
    const firstName = document.getElementById('user-first-name').value;
    const lastName = document.getElementById('user-last-name').value;
    const phoneNumber = document.getElementById('user-phone').value;
    const role = document.getElementById('user-role').value;
    const status = document.getElementById('user-status').value;

    if (!firstName || !lastName || !phoneNumber) {
        alert('Please fill in all required fields.');
        return;
    }

    const userData = {
        firstName: firstName,
        lastName: lastName,
        phoneNumber: phoneNumber,
        role: role,
        accountStatus: status
    };

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            closeModal();
            fetchUsers();
        } else {
            const errorText = await response.text();
            alert('Error saving user: ' + errorText);
        }
    } catch (error) {
        console.error('API Error:', error);
        alert('Failed to save user.');
    }
}

function confirmDeleteUser(userId) {
    if (confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
        deleteUser(userId);
    }
}

async function deleteUser(userId) {
    try {
        const response = await fetch(`${API_BASE_URL}/${userId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            fetchUsers();
        } else {
            alert('Failed to delete user.');
        }
    } catch (error) {
        console.error('API Error:', error);
        alert('Error deleting user.');
    }
}
