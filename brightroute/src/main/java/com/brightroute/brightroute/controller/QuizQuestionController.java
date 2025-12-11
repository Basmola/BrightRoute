package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.QuizQuestion;
import com.brightroute.brightroute.service.impl.QuizQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-questions")
public class QuizQuestionController {

    private final QuizQuestionService quizQuestionService;

    @Autowired
    public QuizQuestionController(QuizQuestionService quizQuestionService) {
        this.quizQuestionService = quizQuestionService;
    }

    @GetMapping
    public List<QuizQuestion> getAllQuestions() {
        return quizQuestionService.findAllQuestions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizQuestion> getQuestionById(@PathVariable Integer id) {
        return quizQuestionService.findQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public QuizQuestion createQuestion(@RequestBody QuizQuestion question) {
        return quizQuestionService.saveQuestion(question);
    }

    // ... Add PUT and DELETE endpoints similarly to CourseController
}