package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.CourseSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CourseSubscriptionRepository extends JpaRepository<CourseSubscription, Integer> {

    Optional<CourseSubscription> findByUserIdAndCourseCourseId(Integer userId, Integer courseId);

    boolean existsByUserIdAndCourseCourseId(Integer userId, Integer courseId);

    void deleteByUserIdAndCourseCourseId(Integer userId, Integer courseId);

    List<CourseSubscription> findByUserId(Integer userId);
}