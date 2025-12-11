package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.QuizChoice;
import com.brightroute.brightroute.service.QuizChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-choices")
public class QuizChoiceController {

    private final QuizChoiceService quizChoiceService;

    @Autowired
    public QuizChoiceController(QuizChoiceService quizChoiceService) {
        this.quizChoiceService = quizChoiceService;
    }

    @GetMapping
    public List<QuizChoice> getAllChoices() {
        return quizChoiceService.findAllChoices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizChoice> getChoiceById(@PathVariable Integer id) {
        return quizChoiceService.findChoiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public QuizChoice createChoice(@RequestBody QuizChoice choice) {
        return quizChoiceService.saveChoice(choice);
    }

    // ... Add PUT and DELETE endpoints similarly to CourseController
}