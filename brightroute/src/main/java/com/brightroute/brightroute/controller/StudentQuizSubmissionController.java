package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.StudentQuizSubmission;
import com.brightroute.brightroute.service.StudentQuizSubmissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class StudentQuizSubmissionController {

    private final StudentQuizSubmissionService submissionService;

    @Autowired
    public StudentQuizSubmissionController(StudentQuizSubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    /**
     * Handles the creation and grading of a complete quiz submission.
     * This endpoint should primarily call the business logic method (submitQuiz).
     */
    @PostMapping("/submit") // Renamed endpoint for clarity
    public ResponseEntity<StudentQuizSubmission> submitQuiz(@Valid @RequestBody StudentQuizSubmission submission) {
        // CORRECTION: Calls the business logic method (submitQuiz)
        return ResponseEntity.ok(submissionService.submitQuiz(submission));
    }

    @GetMapping
    public ResponseEntity<List<StudentQuizSubmission>> getAllSubmissions() {
        // You might want a filter here, but basic implementation is fine
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentQuizSubmission> getSubmissionById(@PathVariable Integer id) { // CORRECTION: Integer
        return submissionService.getSubmissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Integer id) { // CORRECTION: Integer
        if (submissionService.getSubmissionById(id).isPresent()) {
             submissionService.deleteSubmission(id);
             return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}