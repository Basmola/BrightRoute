package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.StudentQuizSubmission;
import com.brightroute.brightroute.repository.StudentQuizSubmissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// @Service
// public class StudentQuizSubmissionService {

//     private final StudentQuizSubmissionRepository submissionRepository;

//     public StudentQuizSubmissionService(StudentQuizSubmissionRepository submissionRepository) {
//         this.submissionRepository = submissionRepository;
//     }

//     public StudentQuizSubmission saveSubmission(StudentQuizSubmission submission) {
//         return submissionRepository.save(submission);
//     }

//     public List<StudentQuizSubmission> getAllSubmissions() {
//         return submissionRepository.findAll();
//     }

//     public Optional<StudentQuizSubmission> getSubmissionById(Long id) {
//         return submissionRepository.findById(id);
//     }

//     public void deleteSubmission(Long id) {
//         submissionRepository.deleteById(id);
//     }
// }

// @Service
// public class StudentQuizSubmissionService {

//     private final StudentQuizSubmissionRepository submissionRepository;

//     public StudentQuizSubmissionService(StudentQuizSubmissionRepository submissionRepository) {
//         this.submissionRepository = submissionRepository;
//     }

//     public StudentQuizSubmission saveSubmission(StudentQuizSubmission submission) {
//         // لو submittedAt مش مبعوت من ال frontend نضبطه تلقائي
//         if (submission.getSubmittedAt() == null) {
//             submission.setSubmittedAt(LocalDateTime.now());
//         }
//         return submissionRepository.save(submission);
//     }

//     public List<StudentQuizSubmission> getAllSubmissions() {
//         return submissionRepository.findAll();
//     }

//     public Optional<StudentQuizSubmission> getSubmissionById(Long id) {
//         return submissionRepository.findById(id);
//     }

//     public void deleteSubmission(Long id) {
//         submissionRepository.deleteById(id);
//     }

    
// }

@Service
public class StudentQuizSubmissionService {

    private final StudentQuizSubmissionRepository submissionRepository;

    public StudentQuizSubmissionService(StudentQuizSubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    // --------------------------
    // submitQuiz()  → الدالة الأساسية لإنشاء Submission
    // --------------------------
    public StudentQuizSubmission submitQuiz(StudentQuizSubmission submission) {

        // لو الوقت مش مبعوت من الـ frontend استخدم الوقت الحالي
        if (submission.getSubmittedAt() == null) {
            submission.setSubmittedAt(LocalDateTime.now());
        }

        // لو فيه score احسبه (لو عندك logic)
        if (submission.getScore() != null) {
            submission.setScore(calculateScore(submission.getScore()));
        }

        // تحديد هل الطالب ناجح أم لا
        submission.setIsPassed(checkIfPassed(submission.getScore()));

        return submissionRepository.save(submission);
    }

    // --------------------------
    // calculateScore()
    // --------------------------
    public Integer calculateScore(double score) {
        // مثال: rounding
        return (int) Math.round(score);
    }

    // --------------------------
    // checkIfPassed()
    // --------------------------
    public boolean checkIfPassed(double score) {
        // مثال: النجاح >= 50
        return score >= 50;
    }

    // --------------------------
    // getDetailedResults()
    // --------------------------
    public List<StudentQuizSubmission> getDetailedResults() {
        return submissionRepository.findAll();
    }

    // --------------------------
    // CRUD القديمة
    // --------------------------
    public StudentQuizSubmission saveSubmission(StudentQuizSubmission submission) {
        if (submission.getSubmittedAt() == null) {
            submission.setSubmittedAt(LocalDateTime.now());
        }
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
