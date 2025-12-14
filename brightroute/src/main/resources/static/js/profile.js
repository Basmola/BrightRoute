

async function renderProfileView() {
    const container = document.getElementById('profile-view');
    const user = getLoggedInUser();

    if (!user) return;

    try {
        const response = await fetch(`http://localhost:7070/api/users/profile/${user.id}`);
        if (!response.ok) throw new Error('Failed to fetch profile');
        const profileData = await response.json();

        let imageSrc = null;
        if (profileData.userImage) {
            imageSrc = `data:image/jpeg;base64,${profileData.userImage}`;
        } else if (user.imageUrl) {
            imageSrc = user.imageUrl;
        }

        container.innerHTML = `
            <div class="bg-white rounded-xl shadow-sm border border-gray-100 p-6 max-w-4xl mx-auto relative">
                
                <div class="flex justify-between items-start mb-8 border-b border-gray-100 pb-8">
                    <div class="flex items-center gap-6">
                        <!-- Profile Image Container -->
                        <div class="relative group">
                            <div id="profile-img-container" class="w-24 h-24 rounded-full bg-brand-900 text-white flex items-center justify-center text-3xl font-bold shadow-lg overflow-hidden">
                                ${imageSrc ? `<img src="${imageSrc}" id="profile-display-img" class="w-full h-full object-cover">` : (profileData.firstName ? profileData.firstName.charAt(0).toUpperCase() : 'U')}
                            </div>
                            
                            <!-- Upload Button (Visible only in edit mode) -->
                            <label for="profile-upload" id="profile-upload-label" class="hidden absolute bottom-0 right-0 bg-white rounded-full p-2 shadow-md border border-gray-200 hover:bg-gray-50 text-gray-600 cursor-pointer">
                                <i class="ph-bold ph-camera"></i>
                            </label>
                            <input type="file" id="profile-upload" class="hidden" accept="image/*" onchange="handleProfileImageSelect(event)">
                        </div>

                        <div>
                            <h2 class="text-2xl font-bold text-gray-900" id="display-name">${profileData.firstName} ${profileData.lastName}</h2>
                            <p class="text-gray-500">${profileData.levelOfEducation || profileData.role || 'Student'}</p>
                            <span class="inline-block mt-2 px-3 py-1 bg-blue-50 text-accent-DEFAULT text-xs rounded-full font-semibold border border-blue-100">
                                ${profileData.role}
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
                            <input type="text" id="profile-firstname" value="${profileData.firstName || ''}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                            <input type="text" id="profile-lastname" value="${profileData.lastName || ''}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Email Address</label>
                            <input type="email" value="${profileData.email || ''}" readonly class="w-full p-2.5 bg-gray-100 border border-gray-300 rounded-lg text-gray-500 cursor-not-allowed" title="Email cannot be changed">
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
                            <input type="text" id="profile-phone" value="${profileData.phoneNumber || ''}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Parent Phone</label>
                            <input type="text" id="profile-parent-phone" value="${profileData.parentNumber || ''}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">National ID</label>
                            <input type="text" id="profile-national-id" value="${profileData.nationalId || ''}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                        </div>
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Academic Level</label>
                            <input type="text" id="profile-level" value="${profileData.levelOfEducation || ''}" readonly class="profile-input w-full p-2.5 bg-gray-50 border border-gray-300 rounded-lg text-gray-600 focus:outline-none focus:ring-2 focus:ring-accent-DEFAULT cursor-not-allowed">
                        </div>
                    </div>
                </form>
            </div>
        `;
    } catch (error) {
        console.error('Error fetching profile:', error);
        container.innerHTML = '<p class="text-red-500 text-center">Failed to load profile data.</p>';
    }
}

window.toggleProfileEdit = function (isEditing) {
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

            if (btnSave.classList.contains('hidden') === false) {
                renderProfileView();
            }
        }
    });

    if (isEditing) {
        btnEdit.classList.add('hidden');
        btnSave.classList.remove('hidden');
        btnCancel.classList.remove('hidden');
        if (uploadLabel) uploadLabel.classList.remove('hidden');
    } else {
        btnEdit.classList.remove('hidden');
        btnSave.classList.add('hidden');
        btnCancel.classList.add('hidden');
        if (uploadLabel) uploadLabel.classList.add('hidden');
    }
}

window.handleProfileImageSelect = function (event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const container = document.getElementById('profile-img-container');

            container.dataset.newImage = e.target.result;

            container.innerHTML = `<img src="${e.target.result}" id="profile-display-img" class="w-full h-full object-cover">`;
        };
        reader.readAsDataURL(file);
    }
}

window.saveProfileChanges = async function () {
    const user = getLoggedInUser();
    if (!user) return;

    const updatedData = {
        firstName: document.getElementById('profile-firstname').value,
        lastName: document.getElementById('profile-lastname').value,
        phoneNumber: document.getElementById('profile-phone').value,
        parentNumber: document.getElementById('profile-parent-phone').value,
        nationalId: document.getElementById('profile-national-id').value,
        levelOfEducation: document.getElementById('profile-level').value
    };

    const imgContainer = document.getElementById('profile-img-container');
    if (imgContainer && imgContainer.dataset.newImage) {

    }

    try {
        const response = await fetch(`http://localhost:7070/api/users/profile/${user.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedData)
        });

        if (response.ok) {
            alert('Profile updated successfully!');

            user.firstName = updatedData.firstName;
            user.lastName = updatedData.lastName;
            saveUserSession(user);

            const sidebarName = document.getElementById('user-name');
            if (sidebarName) sidebarName.textContent = `${updatedData.firstName} ${updatedData.lastName}`;

            renderProfileView();
        } else {
            alert('Failed to update profile.');
        }
    } catch (error) {
        console.error('Error updating profile:', error);
        alert('An error occurred while updating profile.');
    }
}
