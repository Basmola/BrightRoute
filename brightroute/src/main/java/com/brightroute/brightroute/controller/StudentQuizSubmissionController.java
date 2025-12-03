package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.StudentQuizSubmission;
import com.brightroute.brightroute.service.StudentQuizSubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class StudentQuizSubmissionController {

    private final StudentQuizSubmissionService submissionService;

    public StudentQuizSubmissionController(StudentQuizSubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping
    public ResponseEntity<StudentQuizSubmission> createSubmission(@Valid @RequestBody StudentQuizSubmission submission) {
        return ResponseEntity.ok(submissionService.saveSubmission(submission));
    }

    @GetMapping
    public ResponseEntity<List<StudentQuizSubmission>> getAllSubmissions() {
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentQuizSubmission> getSubmissionById(@PathVariable Long id) {
        return submissionService.getSubmissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.noContent().build();
    }
}