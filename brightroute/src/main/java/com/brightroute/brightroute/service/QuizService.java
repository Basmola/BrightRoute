package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Quiz;
import com.brightroute.brightroute.model.QuizQuestion;
import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.repository.QuizRepository;
import com.brightroute.brightroute.repository.LecturePartRepository; // Required for linking
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final LecturePartRepository lecturePartRepository; // Assuming this exists

    @Autowired
    public QuizService(QuizRepository quizRepository, 
                       LecturePartRepository lecturePartRepository) {
        this.quizRepository = quizRepository;
        this.lecturePartRepository = lecturePartRepository;
    }

    // --- CRUD Operations ---
    
    /**
     * Saves a new Quiz, ensuring it links correctly to a LecturePart.
     * Note: This assumes the LecturePart entity is already set on the Quiz object.
     */
    @Transactional
    public Quiz saveQuiz(Quiz quiz) {
        // Business logic check: Ensure the LecturePart exists and is a QUIZ type 
        // (though this check is often best done in a DTO validation).
        
        if (quiz.getLecturePart() == null || quiz.getLecturePart().getId() == null) {
            throw new IllegalArgumentException("Quiz must be linked to a valid LecturePart.");
        }
        
        // This save will cascade and save all nested QuizQuestions and their Choices.
        return quizRepository.save(quiz);
    }

    public Optional<Quiz> findQuizById(Integer quizId) {
        // Fetch the quiz. Due to FetchType.LAZY, questions will only load when accessed.
        return quizRepository.findById(quizId);
    }
    
    public List<Quiz> findAllQuizzes() {
        return quizRepository.findAll();
    }
    
    public void deleteQuiz(Integer quizId) {
        quizRepository.deleteById(quizId);
    }

    // --- Specific Quiz Logic ---

    /**
     * Finds a Quiz by the ID of its associated LecturePart.
     */
    public Optional<Quiz> findQuizByPartId(Integer partId) {
        return quizRepository.findByLecturePart_Id(partId);
    }

    /**
     * Retrieves all questions (and their choices) for a specific Quiz.
     * @param quizId The ID of the Quiz.
     * @return A List of QuizQuestion entities.
     */
    public List<QuizQuestion> getQuestionsForQuiz(Integer quizId) {
        Quiz quiz = findQuizById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found for ID: " + quizId));
        
        // Accessing the 'questions' collection triggers the lazy load from the DB
        return quiz.getQuestions();
    }
    
    // NOTE: A 'submitQuiz' method is crucial here to handle StudentQuizSubmission.
}