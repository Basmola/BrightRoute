package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.StudentQuizSubmission;
import com.brightroute.brightroute.repository.StudentQuizSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentQuizSubmissionService {

    private final StudentQuizSubmissionRepository submissionRepository;

    public StudentQuizSubmissionService(StudentQuizSubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public StudentQuizSubmission saveSubmission(StudentQuizSubmission submission) {
        return submissionRepository.save(submission);
    }

    public List<StudentQuizSubmission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Optional<StudentQuizSubmission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }

    public void deleteSubmission(Long id) {
        submissionRepository.deleteById(id);
    }
}