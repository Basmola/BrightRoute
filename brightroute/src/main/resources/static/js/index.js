//  import { initializeApp } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-app.js";
//         import { getAuth, signInAnonymously, signInWithCustomToken } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-auth.js";
//         import { getFirestore } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-firestore.js";
//         import { setLogLevel } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-firestore.js";
// // =======================================================
//         // 8. INITIALIZATION (Conceptual: main.js) - START
//         // =======================================================

//         window.addEventListener('load', async () => {
//             // 1. Firebase Initialization
//             setLogLevel('Debug');
//             if (firebaseConfig && Object.keys(firebaseConfig).length > 0) {
//                 app = initializeApp(firebaseConfig);
//                 db = getFirestore(app);
//                 auth = getAuth(app);

//                 try {
//                     if (initialAuthToken) {
//                         await signInWithCustomToken(auth, initialAuthToken);
//                     } else {
//                         await signInAnonymously(auth);
//                     }
//                 } catch (error) {
//                     console.error("Firebase Auth Error:", error);
//                     await signInAnonymously(auth);
//                 }

//                 userId = auth.currentUser?.uid || crypto.randomUUID();
//             } else {
//                 console.error("Firebase configuration is missing.");
//             }

//             // 2. Check for active session
//             const sessionData = localStorage.getItem('currentUser');
//             if (sessionData) {
//                 const user = JSON.parse(sessionData);
//                 const fullUser = registeredUsers.find(u => u.email === user.email) || user;
//                 const finalUser = { ...fullUser, ...user };
//                 finalizeLogin(finalUser);
//             } else {
//                 const authFlow = document.getElementById('auth-flow');
//                 const portalFlow = document.getElementById('portal-flow');
//                 if (authFlow) authFlow.classList.remove('hidden');
//                 if (portalFlow) portalFlow.classList.add('hidden');
//                 switchAuthView('register-view');
//             }

//             if (typeof populateCourseDropdowns !== 'undefined') {
//         populateCourseDropdowns();
//     }
//         });
//         // =======================================================
//         // 8. INITIALIZATION (Conceptual: main.js) - END
//         // =======================================================



import { initializeApp } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-app.js";

// Main Bootstrap
window.initApp = function () {
    initSidebar();

    const user = getLoggedInUser();
    const authFlow = document.getElementById('auth-flow');
    const portalFlow = document.getElementById('portal-flow');
    const studentNav = document.getElementById('nav-student');
    const adminNav = document.getElementById('nav-admin');

    if (user) {
        // Hide Auth, Show Portal
        authFlow.classList.add('hidden');
        portalFlow.classList.remove('hidden');

        // Update Sidebar User Info
        document.getElementById('sidebar-name').textContent = `${user.firstName} ${user.lastName}`;
        document.getElementById('sidebar-role').textContent = user.role.charAt(0).toUpperCase() + user.role.slice(1);
        document.getElementById('sidebar-avatar').textContent = user.firstName.charAt(0).toUpperCase();

        // Show/Hide Role specific links
        if (user.role === 'student') {
            studentNav.classList.remove('hidden');
            adminNav.classList.add('hidden');
            navigate('student-dashboard-view');
        } else if (user.role === 'admin') {
            studentNav.classList.add('hidden');
            adminNav.classList.remove('hidden');
            navigate('admin-dashboard-view');
        }
    } else {
        // Show Auth
        authFlow.classList.remove('hidden');
        portalFlow.classList.add('hidden');
        renderAuth();
    }
};

// Start
document.addEventListener('DOMContentLoaded', initApp);