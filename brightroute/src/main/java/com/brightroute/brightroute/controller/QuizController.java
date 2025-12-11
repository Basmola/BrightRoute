package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.Quiz;
import com.brightroute.brightroute.model.QuizQuestion;
import com.brightroute.brightroute.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // --- Quiz CRUD ---

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        // The client must ensure that nested QuizQuestions and their Choices are provided here.
        Quiz newQuiz = quizService.saveQuiz(quiz);
        return ResponseEntity.ok(newQuiz);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Integer id) {
        return quizService.findQuizById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.findAllQuizzes();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Integer id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    // --- Quiz Question Retrieval ---
    
    /**
     * Retrieves the questions (and their choices) for a specific Quiz.
     */
    @GetMapping("/{id}/questions")
    public ResponseEntity<List<QuizQuestion>> getQuizQuestions(@PathVariable Integer id) {
        try {
            List<QuizQuestion> questions = quizService.getQuestionsForQuiz(id);
            return ResponseEntity.ok(questions);
        } catch (RuntimeException e) {
            // Catches the "Quiz not found" exception from the service
            return ResponseEntity.notFound().build();
        }
    }
}