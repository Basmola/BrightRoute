// // =======================================================
// // 5. PROFILE LOGIC (Conceptual: profile.js) - START
// // =======================================================

// function renderProfileView() {
//     const currentProfileData = JSON.parse(localStorage.getItem('currentUser'));
//     if (!currentProfileData) return;

//     const profileContainer = document.getElementById('profile-data-container');
//     const profileForm = document.getElementById('profile-edit-form');
//     const editButton = document.getElementById('edit-profile-btn');
//     const saveButton = document.getElementById('save-profile-btn');

//     if (!profileForm.dataset.editing) profileForm.dataset.editing = 'false';

//     const editing = profileForm.dataset.editing === 'true';

//     const fullName = `${currentProfileData.firstName || ''} ${currentProfileData.lastName || ''}`;

//     document.getElementById('profile-name').textContent = fullName;
//     document.getElementById('profile-role').textContent = currentProfileData.role === 'admin' ? 'Admin Portal' : 'Student Portal';
//     const largeInitials = document.getElementById('profile-initials-large');
//     largeInitials.textContent = (currentProfileData.firstName || 'U').charAt(0).toUpperCase();

//     if (currentProfileData.imageUrl) {
//         largeInitials.style.backgroundImage = `url(${currentProfileData.imageUrl})`;
//         largeInitials.textContent = '';
//         largeInitials.classList.add('nav-profile-img');
//     } else {
//         largeInitials.style.backgroundImage = 'none';
//         largeInitials.classList.remove('nav-profile-img');
//         largeInitials.textContent = (currentProfileData.firstName || 'U').charAt(0).toUpperCase();
//     }

//     profileContainer.innerHTML = editing ? `
//                 <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
//                     <div>
//                         <label class="block text-sm font-medium text-gray-700">First Name</label>
//                         <input type="text" id="edit-first-name" value="${currentProfileData.firstName || ''}" class="mt-1 w-full p-2 border border-gray-300 rounded-lg">
//                     </div>
//                     <div>
//                         <label class="block text-sm font-medium text-gray-700">Last Name</label>
//                         <input type="text" id="edit-last-name" value="${currentProfileData.lastName || ''}" class="mt-1 w-full p-2 border border-gray-300 rounded-lg">
//                     </div>
//                     <div>
//                         <label class="block text-sm font-medium text-gray-700">Email (Cannot be changed)</label>
//                         <input type="email" value="${currentProfileData.email || ''}" disabled class="mt-1 w-full p-2 border border-gray-300 rounded-lg bg-gray-100">
//                     </div>
//                     <div>
//                         <label class="block text-sm font-medium text-gray-700">Phone Number</label>
//                         <input type="tel" id="edit-phone-number" value="${currentProfileData.phoneNumber || ''}" class="mt-1 w-full p-2 border border-gray-300 rounded-lg">
//                     </div>
//                 </div>
//             ` : `
//                 <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
//                     <div><label class="block text-sm font-medium text-gray-500">Email</label><p class="font-medium text-gray-800">${currentProfileData.email || 'N/A'}</p></div>
//                     <div><label class="block text-sm font-medium text-gray-500">Phone Number</label><p class="font-medium text-gray-800">${currentProfileData.phoneNumber || 'N/A'}</p></div>
//                     <div><label class="block text-sm font-medium text-gray-500">Last Login</label><p class="font-medium text-gray-800">Just now</p></div>
//                     <div><label class="block text-sm font-medium text-gray-500">Student ID</label><p class="font-medium text-gray-800">SCMS-1001-2025</p></div>
//                 </div>
//             `;

//     editButton.classList.toggle('hidden', editing);
//     saveButton.classList.toggle('hidden', !editing);
// }
// window.renderProfileView = renderProfileView;

// function toggleEditMode(editing) {
//     const profileForm = document.getElementById('profile-edit-form');
//     profileForm.dataset.editing = editing;
//     renderProfileView();
// }
// window.toggleEditMode = toggleEditMode;

// function saveProfileChanges() {
//     const currentProfileData = JSON.parse(localStorage.getItem('currentUser'));
//     const newFirstName = document.getElementById('edit-first-name').value.trim();
//     const newLastName = document.getElementById('edit-last-name').value.trim();
//     const newPhoneNumber = document.getElementById('edit-phone-number').value.trim();

//     if (!newFirstName || !newLastName) {
//         showMessage("Update Failed", "First Name and Last Name are required.");
//         return;
//     }

//     const userIndex = registeredUsers.findIndex(u => u.email === currentProfileData.email);
//     if (userIndex !== -1) {
//         registeredUsers[userIndex].firstName = newFirstName;
//         registeredUsers[userIndex].lastName = newLastName;
//         registeredUsers[userIndex].phoneNumber = newPhoneNumber;
//     }

//     currentProfileData.firstName = newFirstName;
//     currentProfileData.lastName = newLastName;
//     currentProfileData.phoneNumber = newPhoneNumber;
//     localStorage.setItem('currentUser', JSON.stringify(currentProfileData));

//     updateSidebarUserInfo(currentProfileData);
//     updateProfileIcon(currentProfileData);

//     showMessage("Success", "Profile details updated successfully.");
//     toggleEditMode(false);
// }
// window.saveProfileChanges = saveProfileChanges;
// // =======================================================
// // 5. PROFILE LOGIC (Conceptual: profile.js) - END
// // =======================================================
function renderProfileView() {
    const container = document.getElementById('profile-view');
    const user = getLoggedInUser();

    if (!user) return;

    container.innerHTML = `
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 max-w-4xl mx-auto relative">
            
            <div class="flex justify-between items-start mb-8 border-b border-gray-100 pb-8">
                <div class="flex items-center gap-6">
                    <!-- Profile Image Container -->
                    <div class="relative group">
                        <div id="profile-img-container" class="w-24 h-24 rounded-full bg-brand-900 text-white flex items-center justify-center text-3xl font-bold shadow-lg overflow-hidden">
                            ${user.imageUrl ? `<img src="${user.imageUrl}" id="profile-display-img" class="w-full h-full object-cover">` : (user.firstName ? user.firstName.charAt(0).toUpperCase() : 'U')}
                        </div>
                        
                        <!-- Upload Button (Visible only in edit mode) -->
                        <label for="profile-upload" id="profile-upload-label" class="hidden absolute bottom-0 right-0 bg-white rounded-full p-2 shadow-md border border-gray-200 hover:bg-gray-50 text-gray-600 cursor-pointer">
                            <i class="ph-bold ph-camera"></i>
                        </label>
                        <input type="file" id="profile-upload" class="hidden" accept="image/*" onchange="handleProfileImageSelect(event)">
                    </div>

                    <div>
                        <h2 class="text-2xl font-bold text-gray-900" id="display-name">${user.firstName} ${user.lastName}</h2>
                        <p class="text-gray-500">${user.level || user.title || user.role.toUpperCase()}</p>
                        <span class="inline-block mt-2 px-3 py-1 bg-blue-50 text-accent-DEFAULT text-xs rounded-full font-semibold border border-blue-100">
                            ${user.role}
                        </span>
                    </div>
                </div>
                <div class="flex gap-2">
                    <button id="btn-edit" onclick="toggleProfileEdit(true)" class="px-4 py-2 bg-white border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition shadow-sm flex items-center gap-2">
                        <i class="ph-bold ph-pencil-simple"></i> Edit Profile
                    </button>
                    <button id="btn-save" onclick="saveProfileChanges()" class="hidden px-4 py-2 bg-brand-900 text-white rounded-lg hover:bg-brand-800 transition shadow-sm flex items-center gap-2">
                        <i class="ph-bold ph-check"></i> Save Changes
                    </button>
                    <button id="btn-cancel" onclick="toggleProfileEdit(false)" class="hidden px-4 py-2 bg-red-50 text-red-600 border border-red-200 rounded-lg hover:bg-red-100 transition shadow-sm">
                        Cancel
                    </button>
                </div>
            </div>

            <form id="profile-form" class="space-y-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">First Name</label>
                        <input type="text" id="profile-firstname" value="${user.firstName}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                        <input type="text" id="profile-lastname" value="${user.lastName}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Email Address</label>
                        <input type="email" value="${user.email}" readonly class="w-full p-2.5 bg-gray-100 border border-gray-300 rounded-lg text-gray-500 cursor-not-allowed" title="Email cannot be changed">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
                        <input type="text" id="profile-phone" value="${user.phoneNumber || user.phone || ''}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Parent Phone</label>
                        <input type="text" id="profile-parent-phone" value="${user.parentPhoneNumber || ''}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">National ID</label>
                        <input type="text" value="${user.nationalId || ''}" readonly class="w-full p-2.5 bg-gray-100 border border-gray-300 rounded-lg text-gray-500 cursor-not-allowed">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 mb-1">Academic Level</label>
                        <input type="text" id="profile-level" value="${user.level || ''}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                    </div>
                </div>
            </form>
        </div>
    `;
}

window.toggleProfileEdit = function(isEditing) {
    const inputs = document.querySelectorAll('.profile-input');
    const btnEdit = document.getElementById('btn-edit');
    const btnSave = document.getElementById('btn-save');
    const btnCancel = document.getElementById('btn-cancel');
    const uploadLabel = document.getElementById('profile-upload-label');

    inputs.forEach(input => {
        if (isEditing) {
            input.removeAttribute('readonly');
            input.classList.remove('bg-gray-50', 'cursor-not-allowed', 'text-gray-600');
            input.classList.add('bg-white', 'text-gray-900');
        } else {
            input.setAttribute('readonly', 'true');
            input.classList.add('bg-gray-50', 'cursor-not-allowed', 'text-gray-600');
            input.classList.remove('bg-white', 'text-gray-900');
            // If cancelling, re-render to reset values
            if (btnSave.classList.contains('hidden') === false) {
                 renderProfileView();
            }
        }
    });

    if (isEditing) {
        btnEdit.classList.add('hidden');
        btnSave.classList.remove('hidden');
        btnCancel.classList.remove('hidden');
        if(uploadLabel) uploadLabel.classList.remove('hidden');
    } else {
        btnEdit.classList.remove('hidden');
        btnSave.classList.add('hidden');
        btnCancel.classList.add('hidden');
        if(uploadLabel) uploadLabel.classList.add('hidden');
    }
}

window.handleProfileImageSelect = function(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const container = document.getElementById('profile-img-container');
            // Store new image temporarily on the container data attribute
            container.dataset.newImage = e.target.result;
            
            // Update view immediately
            container.innerHTML = `<img src="${e.target.result}" id="profile-display-img" class="w-full h-full object-cover">`;
        };
        reader.readAsDataURL(file);
    }
}

window.saveProfileChanges = function() {
    const user = getLoggedInUser();
    
    // Update user object with new values from inputs
    user.firstName = document.getElementById('profile-firstname').value;
    user.lastName = document.getElementById('profile-lastname').value;
    user.phoneNumber = document.getElementById('profile-phone').value;
    user.parentPhoneNumber = document.getElementById('profile-parent-phone').value;
    user.level = document.getElementById('profile-level').value;

    // Update Image if changed
    const imgContainer = document.getElementById('profile-img-container');
    if (imgContainer && imgContainer.dataset.newImage) {
        user.imageUrl = imgContainer.dataset.newImage;
    }

    // Save updated user to local storage
    saveUserSession(user);
    
    // Update global state if available to keep consistency in current session
    if (typeof state !== 'undefined' && state.users) {
        const userIndex = state.users.findIndex(u => u.email === user.email);
        if(userIndex !== -1) {
            state.users[userIndex] = {...state.users[userIndex], ...user};
        }
    }

    // Update UI elements immediately (Sidebar)
    const sidebarName = document.getElementById('user-name');
    const sidebarAvatar = document.getElementById('sidebar-avatar');
    
    if(sidebarName) sidebarName.textContent = `${user.firstName} ${user.lastName}`;
    
    if (sidebarAvatar) {
        if (user.imageUrl) {
            sidebarAvatar.style.backgroundImage = `url(${user.imageUrl})`;
            sidebarAvatar.style.backgroundSize = 'cover';
            sidebarAvatar.style.backgroundPosition = 'center';
            sidebarAvatar.innerHTML = ''; 
        } else {
            sidebarAvatar.style.backgroundImage = 'none';
            sidebarAvatar.textContent = user.firstName.charAt(0).toUpperCase();
        }
    }

    alert('Profile updated successfully!');
    
    // Re-render to lock fields and refresh header text
    renderProfileView();
}