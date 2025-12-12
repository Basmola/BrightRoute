
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

        function submitQuiz() {
            const quizSubmitBtn = document.getElementById('quiz-submit-btn');

            quizSubmitBtn.disabled = true;
            quizSubmitBtn.textContent = 'Quiz Completed!';
            quizSubmitBtn.classList.remove('bg-secondary');
            quizSubmitBtn.classList.add('bg-gray-500', 'cursor-not-allowed');

            document.querySelectorAll('.lecture-part-locked').forEach(btn => {
                btn.classList.remove('opacity-50', 'cursor-not-allowed');
                btn.removeAttribute('disabled');
                btn.classList.remove('text-gray-300');
                btn.classList.add('text-white');
            });

            showMessage('Quiz Submitted!', 'Congratulations! You passed the quiz. Lecture content is now unlocked in the sidebar.');

            showLecturePart('part1-content');
        }
        window.submitQuiz = submitQuiz;
        // =======================================================
        // 7. LECTURE & QUIZ LOGIC (Conceptual: lecture_parts.js) - END
        // =======================================================
