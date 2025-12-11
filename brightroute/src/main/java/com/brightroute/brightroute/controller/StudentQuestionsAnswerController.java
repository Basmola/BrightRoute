package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.StudentQuestionsAnswer;
import com.brightroute.brightroute.service.StudentQuestionsAnswerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class StudentQuestionsAnswerController {

    private final StudentQuestionsAnswerService answerService;

    @Autowired
    public StudentQuestionsAnswerController(StudentQuestionsAnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ResponseEntity<StudentQuestionsAnswer> createAnswer(@Valid @RequestBody StudentQuestionsAnswer answer) {
        // NOTE: Answers should typically be saved in bulk via the StudentQuizSubmission service
        return ResponseEntity.ok(answerService.saveAnswer(answer));
    }

    @GetMapping
    public ResponseEntity<List<StudentQuestionsAnswer>> getAllAnswers() {
        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentQuestionsAnswer> getAnswerById(@PathVariable Integer id) { // CORRECTION: Integer
        return answerService.getAnswerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Integer id) { // CORRECTION: Integer
        if (answerService.getAnswerById(id).isPresent()) {
            answerService.deleteAnswer(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build(); // Handle case where ID doesn't exist
    }
}