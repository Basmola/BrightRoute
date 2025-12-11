package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.StudentQuizSubmission;
import com.brightroute.brightroute.model.Quiz;
import com.brightroute.brightroute.repository.StudentQuizSubmissionRepository;
import com.brightroute.brightroute.repository.QuizRepository; // Required to fetch passing score
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentQuizSubmissionService {

    private final StudentQuizSubmissionRepository submissionRepository;
    private final QuizRepository quizRepository; // ADDED: Assume injection needed for grading logic

    public StudentQuizSubmissionService(StudentQuizSubmissionRepository submissionRepository, QuizRepository quizRepository) {
        this.submissionRepository = submissionRepository;
        this.quizRepository = quizRepository;
    }

    // --------------------------
    // submitQuiz() - The primary business logic for creating a Submission
    // --------------------------
    @Transactional
    public StudentQuizSubmission submitQuiz(StudentQuizSubmission submission) {
        
        // --- NOTE: In a real system, the score would be calculated here based on answers ---
        // For now, we rely on the DTO/Client providing the score and user/quiz entities.
        
        // 1. Set submission time if not already set (relying on updatable=false)
        if (submission.getSubmittedAt() == null) {
            submission.setSubmittedAt(LocalDateTime.now());
        }
        
        // 2. Fetch passing score from the Quiz
        Quiz quiz = submission.getQuiz();
        if (quiz == null || quiz.getId() == null) {
            throw new IllegalArgumentException("Submission must be linked to a valid Quiz.");
        }
        
        // Reload the Quiz to get the passing score (assuming score is pre-calculated on the model)
        Quiz quizDetails = quizRepository.findById(quiz.getId())
            .orElseThrow(() -> new RuntimeException("Quiz not found for grading."));

        // 3. Determine passing status
        Integer score = submission.getSubmissionScore();
        if (score != null) {
            submission.setIsPassed(checkIfPassed(score, quizDetails.getQuizPassingScore()));
        } else {
             // Handle case where score is null (e.g., initial save, or failed calculation)
             submission.setIsPassed(false);
        }

        // 4. Save submission (cascades to save StudentQuestionsAnswer entities too)
        return submissionRepository.save(submission);
    }

    // --------------------------
    // checkIfPassed() - Checks against the Quiz's passing score
    // --------------------------
    public boolean checkIfPassed(Integer submittedScore, Integer passingScore) {
        if (submittedScore == null || passingScore == null) {
            return false;
        }
        return submittedScore >= passingScore;
    }

    // --------------------------
    // getDetailedResults() - Fetch all submissions (or filter by user/quiz)
    // --------------------------
    public List<StudentQuizSubmission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    // --------------------------
    // CRUD (Corrected to Integer IDs)
    // --------------------------
    
    public Optional<StudentQuizSubmission> getSubmissionById(Integer id) { // CORRECTION: Integer
        return submissionRepository.findById(id);
    }

    public void deleteSubmission(Integer id) { // CORRECTION: Integer
        submissionRepository.deleteById(id);
    }
}