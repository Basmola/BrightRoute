package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.CourseSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseSubscriptionRepository extends JpaRepository<CourseSubscription, Long> {
    
    Optional<CourseSubscription> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    
    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);
}
