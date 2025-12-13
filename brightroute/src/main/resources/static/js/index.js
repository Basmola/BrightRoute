 import { initializeApp } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-app.js";
        import { getAuth, signInAnonymously, signInWithCustomToken } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-auth.js";
        import { getFirestore } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-firestore.js";
        import { setLogLevel } from "https://www.gstatic.com/firebasejs/11.6.1/firebase-firestore.js";
// =======================================================
        // 8. INITIALIZATION (Conceptual: main.js) - START
        // =======================================================

        window.addEventListener('load', async () => {
            // 1. Firebase Initialization
            setLogLevel('Debug');
            if (firebaseConfig && Object.keys(firebaseConfig).length > 0) {
                app = initializeApp(firebaseConfig);
                db = getFirestore(app);
                auth = getAuth(app);

                try {
                    if (initialAuthToken) {
                        await signInWithCustomToken(auth, initialAuthToken);
                    } else {
                        await signInAnonymously(auth);
                    }
                } catch (error) {
                    console.error("Firebase Auth Error:", error);
                    await signInAnonymously(auth);
                }

                userId = auth.currentUser?.uid || crypto.randomUUID();
            } else {
                console.error("Firebase configuration is missing.");
            }

            // 2. Check for active session
            const sessionData = localStorage.getItem('currentUser');
            if (sessionData) {
                const user = JSON.parse(sessionData);
                const fullUser = registeredUsers.find(u => u.email === user.email) || user;
                const finalUser = { ...fullUser, ...user };
                finalizeLogin(finalUser);
            } else {
                const authFlow = document.getElementById('auth-flow');
                const portalFlow = document.getElementById('portal-flow');
                if (authFlow) authFlow.classList.remove('hidden');
                if (portalFlow) portalFlow.classList.add('hidden');
                switchAuthView('register-view');
            }

            if (typeof populateCourseDropdowns !== 'undefined') {
        populateCourseDropdowns();
    }
        });
        // =======================================================
        // 8. INITIALIZATION (Conceptual: main.js) - END
        // =======================================================