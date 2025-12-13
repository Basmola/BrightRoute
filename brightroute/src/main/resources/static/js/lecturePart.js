
document.addEventListener('DOMContentLoaded', () => {
    checkAdminAuth();
});

function checkAdminAuth() {
    const userJson = localStorage.getItem('currentUser');
    if (!userJson) {
        window.location.href = 'Login&Register.html';
        return;
    }

    const user = JSON.parse(userJson);
    // Case-insensitive role check
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

// =======================================================
// 7. LECTURE & QUIZ LOGIC (Conceptual: lecture_parts.js) - START
// =======================================================

function showAccessCodeModal(lectureId) {
    currentLectureId = lectureId;
    const modal = document.getElementById('custom-modal');
    document.getElementById('modal-title').textContent = 'Access Code Required';

    document.getElementById('modal-message').innerHTML = `
                <p class="text-gray-600 mb-4">Please enter your unique access code to unlock this lecture.</p>
                <input type="text" id="access-code-input" placeholder="Enter Access Code" 
                        class="mt-1 w-full p-3 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary uppercase text-center font-mono">
            `;

    document.getElementById('modal-actions').innerHTML = `
                <button onclick="handleAccessCodeSubmit();" class="px-4 py-2 bg-secondary text-white rounded-lg hover:bg-gray-600 transition shadow-md mr-3">Validate & Unlock</button>
                <button onclick="hideModal();" class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition">Cancel</button>
            `;

    modal.classList.remove('hidden');
    setTimeout(() => modal.classList.add('modal-visible'), 10);
}
window.showAccessCodeModal = showAccessCodeModal;

function handleAccessCodeSubmit() {
    const codeInput = document.getElementById('access-code-input');
    const code = codeInput.value.toUpperCase().trim();

    if (!validAccessCodes.includes(code)) {
        showMessage('Access Denied', 'The code you entered is invalid. Please check your key.');
        return;
    }

    if (usedAccessCodes.includes(code)) {
        showMessage('Access Denied', 'This access code has already been used and is no longer valid.');
        return;
    }

    usedAccessCodes.push(code);
    hideModal();

    navigate('lecture-detail-view');

    renderQuizQuestions();

    document.querySelectorAll('.lecture-part-content').forEach(c => c.classList.add('hidden'));
    document.getElementById('quiz-intro-content').classList.remove('hidden');
    document.getElementById('quiz-content-actual').classList.add('hidden');

    showMessage('Access Granted', 'Lecture unlocked successfully! Please start the quiz to proceed to the content.');
}
window.handleAccessCodeSubmit = handleAccessCodeSubmit;

function renderQuizQuestions() {
    const quizContainer = document.getElementById('quiz-questions-container');
    if (!quizContainer) return;

    quizContainer.innerHTML = '';

    mockQuizQuestions.forEach((q, index) => {
        const qElement = document.createElement('div');
        qElement.className = 'bg-gray-100 p-4 rounded-lg';

        let optionsHtml = '';
        q.answerOptions.forEach((option, optionIndex) => {
            const optionId = `q${index}-opt${optionIndex}`;
            optionsHtml += `
                        <label class="block cursor-pointer hover:bg-gray-200 p-2 rounded-md transition text-gray-700">
                            <input type="radio" name="q${index}" value="${optionIndex}" class="text-primary mr-2"> 
                            ${option.text}
                        </label>
                    `;
        });

        qElement.innerHTML = `
                    <p class="font-medium text-gray-800 mb-2">Q${q.questionNumber}: ${q.question}</p>
                    <div class="space-y-1 mt-2">${optionsHtml}</div>
                `;
        quizContainer.appendChild(qElement);
    });
}

function startQuiz() {
    document.getElementById('quiz-intro-content').classList.add('hidden');
    document.getElementById('quiz-content-actual').classList.remove('hidden');
    const quizSubmitBtn = document.getElementById('quiz-submit-btn');
    if (quizSubmitBtn) {
        quizSubmitBtn.disabled = false;
        quizSubmitBtn.textContent = 'Submit Quiz';
        quizSubmitBtn.classList.remove('bg-gray-500', 'cursor-not-allowed');
        quizSubmitBtn.classList.add('bg-secondary');
    }
}
window.startQuiz = startQuiz;

function showLecturePart(partId) {
    document.querySelectorAll('.lecture-part-content').forEach(content => {
        content.classList.add('hidden');
    });

    document.getElementById(partId).classList.remove('hidden');

    if (partId === 'quiz-content') {
        const quizSubmitted = document.getElementById('quiz-submit-btn').disabled;

        if (quizSubmitted) {
            document.getElementById('quiz-intro-content').classList.add('hidden');
            document.getElementById('quiz-content-actual').classList.add('hidden');
            showLecturePart('part1-content');
            return;
        }

        document.getElementById('quiz-intro-content').classList.remove('hidden');
        document.getElementById('quiz-content-actual').classList.add('hidden');
    }

    document.querySelectorAll('.part-button').forEach(btn => {
        btn.classList.remove('bg-gray-600', 'text-white', 'font-bold');
        btn.classList.add('text-gray-300', 'hover:bg-gray-700');

        if (btn.getAttribute('onclick') && btn.getAttribute('onclick').includes(partId)) {
            btn.classList.add('bg-gray-600', 'text-white', 'font-bold');
            btn.classList.remove('text-gray-300', 'hover:bg-gray-700');
        }
    });
}
window.showLecturePart = showLecturePart;


// =======================================================
// وظيفة فتح المحتوى (UNLOCKING CONTENT)
// =======================================================
function unlockLectureContent() {
    // 1. إلغاء قفل أزرار التنقل في القائمة الجانبية للمحاضرة
    const lockedButtons = document.querySelectorAll('.lecture-part-locked');
    lockedButtons.forEach(btn => {
        btn.disabled = false;
        btn.classList.remove('opacity-50', 'cursor-not-allowed', 'lecture-part-locked');
        btn.classList.add('hover:bg-gray-700');
    });

    // 2. تعطيل زر إرسال الاختبار لمنع الإرسال مرة أخرى
    const quizSubmitBtn = document.getElementById('quiz-submit-btn');
    if (quizSubmitBtn) {
        quizSubmitBtn.disabled = true;
        quizSubmitBtn.textContent = 'Quiz Completed';
        quizSubmitBtn.classList.remove('bg-secondary', 'hover:bg-gray-600');
        quizSubmitBtn.classList.add('bg-gray-500', 'cursor-not-allowed');
    }
}
window.unlockLectureContent = unlockLectureContent;
// =======================================================
// وظيفة submitQuiz المُعدَّلة لفرض الإجابة على جميع الأسئلة
//// =======================================================
// وظيفة submitQuiz المُعدَّلة
// =======================================================

function submitQuiz() {
    const quizContainer = document.getElementById('quiz-questions-container');
    if (!quizContainer) {
        if (typeof showMessage !== 'undefined') showMessage('Error', 'Quiz questions were not loaded.');
        return;
    }

    // 1. جمع وتجميع أسماء الأسئلة (بدون تغيير)
    const radioInputs = quizContainer.querySelectorAll('input[type="radio"]');
    const uniqueQuestionNames = new Set();
    radioInputs.forEach(input => {
        if (input.name) {
            uniqueQuestionNames.add(input.name);
        }
    });

    // 2. التحقق من الإجابة على كل سؤال (بدون تغيير)
    let allAnswered = true;
    for (const name of uniqueQuestionNames) {
        const answered = quizContainer.querySelector(`input[name="${name}"]:checked`);
        if (!answered) {
            allAnswered = false;
            break;
        }
    }

    // 3. تنفيذ الشرط (إظهار خطأ إذا لم يتم الإجابة على كل شيء)
    if (!allAnswered) {
        if (typeof showMessage !== 'undefined') {
            showMessage('Quiz Submission Failed', 'You must answer all questions before submitting the quiz.');
        }
        return;
    }

    // 4. في حالة نجاح التحقق (جميع الأسئلة مجاب عليها):

    // أ. إلغاء قفل المحتوى (فتح أزرار القائمة الجانبية للمحاضرة)
    if (typeof unlockLectureContent !== 'undefined') {
        unlockLectureContent();
    }

    // ب. نقل المستخدم إلى الجزء الأول من المحتوى (Part 1: Core Concepts Video)
    if (typeof showLecturePart !== 'undefined') {
        showLecturePart('part1-content');
    }

    // ج. عرض رسالة النجاح
    if (typeof showMessage !== 'undefined') {
        showMessage('Quiz Submitted', 'Quiz passed successfully! The lecture content is now unlocked.');
    }
}

window.submitQuiz = submitQuiz;