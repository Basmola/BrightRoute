// =======================================================
        // 2. UI AND MODAL UTILITIES (Conceptual: ui.js) - START
        // =======================================================

        function hideModal() {
            const modal = document.getElementById('custom-modal');
            modal.classList.remove('modal-visible');
            setTimeout(() => {
                modal.classList.add('hidden');
            }, 300);
        }
        window.hideModal = hideModal;

        function showMessage(title, message, callback = null) {
            const modal = document.getElementById('custom-modal');

            document.getElementById('modal-title').textContent = title;
            document.getElementById('modal-message').innerHTML = message;

            document.getElementById('modal-actions').innerHTML = `
                <button id="modal-close-btn" class="px-4 py-2 bg-primary text-white rounded-lg hover:bg-gray-800 transition">Close</button>
            `;

            const closeButton = document.getElementById('modal-close-btn');
            closeButton.onclick = () => {
                modal.classList.remove('modal-visible');
                setTimeout(() => {
                    modal.classList.add('hidden');
                    if (callback) callback();
                }, 300);
            };

            modal.classList.remove('hidden');
            setTimeout(() => modal.classList.add('modal-visible'), 10);
        }
        window.showMessage = showMessage;

        function toggleSidebar(forceState = null) {
            const sidebar = document.getElementById('portal-sidebar');
            const backdrop = document.getElementById('sidebar-backdrop');
            const mainWrapper = document.querySelector('.main-content-wrapper');
            const toggleIcon = document.getElementById('sidebar-toggle-icon');

            const isOpen = sidebar.classList.contains('sidebar-open');
            const newState = forceState !== null ? forceState : !isOpen;

            if (newState) {
                sidebar.classList.remove('sidebar-closed-desktop');
                sidebar.classList.add('sidebar-open');
                mainWrapper.classList.remove('sidebar-closed-desktop');

                if (toggleIcon) {
                    toggleIcon.innerHTML = `<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>`;
                }

                if (window.innerWidth < 1024) {
                    backdrop.classList.remove('hidden');
                    setTimeout(() => backdrop.style.opacity = 0.5, 10);
                }
            } else {
                sidebar.classList.remove('sidebar-open');
                mainWrapper.classList.add('sidebar-closed-desktop');

                if (toggleIcon) {
                    toggleIcon.innerHTML = `<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>`;
                }

                if (window.innerWidth >= 1024) {
                    sidebar.classList.add('sidebar-closed-desktop');
                } else {
                    backdrop.style.opacity = 0;
                    setTimeout(() => backdrop.classList.add('hidden'), 300);
                }
            }
            localStorage.setItem('isSidebarOpen', newState);
        }
        window.toggleSidebar = toggleSidebar;

        function updateSidebarUserInfo(user) {
            const initials = user.firstName ? user.firstName.charAt(0).toUpperCase() : 'U';
            document.getElementById('user-initials').textContent = initials;
            document.getElementById('user-full-name').textContent = `${user.firstName} ${user.lastName}`;
        }

        function handleImageUpload(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    const imageUrl = e.target.result;
                    document.getElementById('register-image-url').value = imageUrl;

                    const previewElement = document.getElementById('image-preview');
                    if (previewElement) {
                        previewElement.style.backgroundImage = `url(${imageUrl})`;
                        previewElement.textContent = '';
                    }
                };
                reader.readAsDataURL(file);
            }
        }
        window.handleImageUpload = handleImageUpload;

        function updateProfileIcon(user) {
            const profileIcon = document.getElementById('nav-profile-icon');
            const initials = user.firstName ? user.firstName.charAt(0).toUpperCase() : 'U';
            const navGreetingText = document.getElementById('nav-greeting-text');

            if (navGreetingText) {
                navGreetingText.textContent = `Hello, ${user.firstName}!`;
            }

            if (user.imageUrl) {
                profileIcon.style.backgroundImage = `url(${user.imageUrl})`;
                profileIcon.classList.add('nav-profile-img');
                profileIcon.innerHTML = '';
            } else {
                profileIcon.style.backgroundImage = 'none';
                profileIcon.classList.remove('nav-profile-img');
                profileIcon.innerHTML = `
                    <svg class="w-6 h-6 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                `;
            }
        }
        // =======================================================
        // 2. UI AND MODAL UTILITIES (Conceptual: ui.js) - END
        // =======================================================

