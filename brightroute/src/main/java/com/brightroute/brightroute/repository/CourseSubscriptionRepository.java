package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.CourseSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseSubscriptionRepository extends JpaRepository<CourseSubscription, Integer> { 
    // CORRECTION: PK type is Integer
    
    // CORRECTION: Changed StudentId to UserId
    Optional<CourseSubscription> findByUserIdAndCourseCourseId(Integer userId, Integer courseId);
    
    // CORRECTION: Changed StudentId to UserId
    boolean existsByUserIdAndCourseCourseId(Integer userId, Integer courseId);
    
    // CORRECTION: Changed StudentId to UserId
    void deleteByUserIdAndCourseCourseId(Integer userId, Integer courseId);
}