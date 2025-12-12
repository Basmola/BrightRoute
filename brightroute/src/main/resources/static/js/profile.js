// =======================================================
        // 5. PROFILE LOGIC (Conceptual: profile.js) - START
        // =======================================================

        function renderProfileView() {
            const currentProfileData = JSON.parse(localStorage.getItem('currentUser'));
            if (!currentProfileData) return;

            const profileContainer = document.getElementById('profile-data-container');
            const profileForm = document.getElementById('profile-edit-form');
            const editButton = document.getElementById('edit-profile-btn');
            const saveButton = document.getElementById('save-profile-btn');

            if (!profileForm.dataset.editing) profileForm.dataset.editing = 'false';

            const editing = profileForm.dataset.editing === 'true';

            const fullName = `${currentProfileData.firstName || ''} ${currentProfileData.lastName || ''}`;

            document.getElementById('profile-name').textContent = fullName;
            document.getElementById('profile-role').textContent = currentProfileData.role === 'admin' ? 'Admin Portal' : 'Student Portal';
            const largeInitials = document.getElementById('profile-initials-large');
            largeInitials.textContent = (currentProfileData.firstName || 'U').charAt(0).toUpperCase();

            if (currentProfileData.imageUrl) {
                largeInitials.style.backgroundImage = `url(${currentProfileData.imageUrl})`;
                largeInitials.textContent = '';
                largeInitials.classList.add('nav-profile-img');
            } else {
                largeInitials.style.backgroundImage = 'none';
                largeInitials.classList.remove('nav-profile-img');
                largeInitials.textContent = (currentProfileData.firstName || 'U').charAt(0).toUpperCase();
            }

            profileContainer.innerHTML = editing ? `
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <label class="block text-sm font-medium text-gray-700">First Name</label>
                        <input type="text" id="edit-first-name" value="${currentProfileData.firstName || ''}" class="mt-1 w-full p-2 border border-gray-300 rounded-lg">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Last Name</label>
                        <input type="text" id="edit-last-name" value="${currentProfileData.lastName || ''}" class="mt-1 w-full p-2 border border-gray-300 rounded-lg">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Email (Cannot be changed)</label>
                        <input type="email" value="${currentProfileData.email || ''}" disabled class="mt-1 w-full p-2 border border-gray-300 rounded-lg bg-gray-100">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700">Phone Number</label>
                        <input type="tel" id="edit-phone-number" value="${currentProfileData.phoneNumber || ''}" class="mt-1 w-full p-2 border border-gray-300 rounded-lg">
                    </div>
                </div>
            ` : `
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div><label class="block text-sm font-medium text-gray-500">Email</label><p class="font-medium text-gray-800">${currentProfileData.email || 'N/A'}</p></div>
                    <div><label class="block text-sm font-medium text-gray-500">Phone Number</label><p class="font-medium text-gray-800">${currentProfileData.phoneNumber || 'N/A'}</p></div>
                    <div><label class="block text-sm font-medium text-gray-500">Last Login</label><p class="font-medium text-gray-800">Just now</p></div>
                    <div><label class="block text-sm font-medium text-gray-500">Student ID</label><p class="font-medium text-gray-800">SCMS-1001-2025</p></div>
                </div>
            `;

            editButton.classList.toggle('hidden', editing);
            saveButton.classList.toggle('hidden', !editing);
        }
        window.renderProfileView = renderProfileView;

        function toggleEditMode(editing) {
            const profileForm = document.getElementById('profile-edit-form');
            profileForm.dataset.editing = editing;
            renderProfileView();
        }
        window.toggleEditMode = toggleEditMode;

        function saveProfileChanges() {
            const currentProfileData = JSON.parse(localStorage.getItem('currentUser'));
            const newFirstName = document.getElementById('edit-first-name').value.trim();
            const newLastName = document.getElementById('edit-last-name').value.trim();
            const newPhoneNumber = document.getElementById('edit-phone-number').value.trim();

            if (!newFirstName || !newLastName) {
                showMessage("Update Failed", "First Name and Last Name are required.");
                return;
            }

            const userIndex = registeredUsers.findIndex(u => u.email === currentProfileData.email);
            if (userIndex !== -1) {
                registeredUsers[userIndex].firstName = newFirstName;
                registeredUsers[userIndex].lastName = newLastName;
                registeredUsers[userIndex].phoneNumber = newPhoneNumber;
            }

            currentProfileData.firstName = newFirstName;
            currentProfileData.lastName = newLastName;
            currentProfileData.phoneNumber = newPhoneNumber;
            localStorage.setItem('currentUser', JSON.stringify(currentProfileData));

            updateSidebarUserInfo(currentProfileData);
            updateProfileIcon(currentProfileData);

            showMessage("Success", "Profile details updated successfully.");
            toggleEditMode(false);
        }
        window.saveProfileChanges = saveProfileChanges;
        // =======================================================
        // 5. PROFILE LOGIC (Conceptual: profile.js) - END
        // =======================================================
