package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.StudentQuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentQuizSubmissionRepository extends JpaRepository<StudentQuizSubmission, Long> {
    // ممكن تضيف Queries مخصصة هنا لو محتاج
}