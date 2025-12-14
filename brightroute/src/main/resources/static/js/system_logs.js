document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
    fetchSystemLogs();
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

async function fetchSystemLogs() {
    try {
        const response = await fetch('/api/system-logs');
        if (response.ok) {
            const logs = await response.json();

            logs.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
            renderLogsTable(logs);
        } else {
            console.error('Failed to fetch system logs');
        }
    } catch (error) {
        console.error('Error fetching system logs:', error);
    }
}

function renderLogsTable(logs) {
    const tableBody = document.getElementById('system-logs-table-body');
    if (!tableBody) return;

    if (logs.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="px-6 py-4 text-center text-sm text-gray-500">No logs found.</td>
            </tr>
        `;
        return;
    }

    tableBody.innerHTML = logs.map(log => {
        const userDisplay = log.user
            ? `<span class="font-medium text-gray-900">${log.user.firstName} ${log.user.lastName}</span> <span class="text-gray-400 text-xs">(ID: ${log.user.id})</span>`
            : '<span class="text-gray-400 italic">System / Unknown</span>';

        const dateObj = new Date(log.timestamp);
        const formattedDate = dateObj.toLocaleDateString() + ' ' + dateObj.toLocaleTimeString();

        return `
        <tr class="hover:bg-gray-50 transition-colors">
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">#${log.logId}</td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                <span class="px-2 py-1 rounded-full text-xs font-semibold ${getActionColorClass(log.action)}">
                    ${log.action}
                </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                ${userDisplay}
            </td>
            <td class="px-6 py-4 text-sm text-gray-600 max-w-md truncate" title="${log.details || ''}">
                ${log.details || '-'}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                ${formattedDate}
            </td>
        </tr>
    `}).join('');
}

function getActionColorClass(action) {
    const upperAction = action ? action.toUpperCase() : '';
    if (upperAction.includes('LOGIN')) return 'bg-green-100 text-green-800';
    if (upperAction.includes('REGISTRATION') || upperAction.includes('REGISTER')) return 'bg-blue-100 text-blue-800';
    if (upperAction.includes('DELETE')) return 'bg-red-100 text-red-800';
    if (upperAction.includes('UPDATE') || upperAction.includes('EDIT')) return 'bg-yellow-100 text-yellow-800';
    return 'bg-gray-100 text-gray-800';
}
