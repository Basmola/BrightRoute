package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.StudentQuizSubmission;
import com.brightroute.brightroute.model.Quiz;
import com.brightroute.brightroute.repository.StudentQuizSubmissionRepository;
import com.brightroute.brightroute.repository.QuizRepository;  
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentQuizSubmissionService {

    private final StudentQuizSubmissionRepository submissionRepository;
    private final QuizRepository quizRepository;  

    public StudentQuizSubmissionService(StudentQuizSubmissionRepository submissionRepository, QuizRepository quizRepository) {
        this.submissionRepository = submissionRepository;
        this.quizRepository = quizRepository;
    }

    @Transactional
    public StudentQuizSubmission submitQuiz(StudentQuizSubmission submission) {

        if (submission.getSubmittedAt() == null) {
            submission.setSubmittedAt(LocalDateTime.now());
        }

        Quiz quiz = submission.getQuiz();
        if (quiz == null || quiz.getId() == null) {
            throw new IllegalArgumentException("Submission must be linked to a valid Quiz.");
        }

        Quiz quizDetails = quizRepository.findById(quiz.getId())
            .orElseThrow(() -> new RuntimeException("Quiz not found for grading."));

        Integer score = submission.getSubmissionScore();
        if (score != null) {
            submission.setIsPassed(checkIfPassed(score, quizDetails.getQuizPassingScore()));
        } else {
              
             submission.setIsPassed(false);
        }

        return submissionRepository.save(submission);
    }

    public boolean checkIfPassed(Integer submittedScore, Integer passingScore) {
        if (submittedScore == null || passingScore == null) {
            return false;
        }
        return submittedScore >= passingScore;
    }

    public List<StudentQuizSubmission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Optional<StudentQuizSubmission> getSubmissionById(Integer id) {  
        return submissionRepository.findById(id);
    }

    public void deleteSubmission(Integer id) {  
        submissionRepository.deleteById(id);
    }
}