package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    // Custom query methods can be added here, e.g.,
    // List<Course> findByCourseInstructor(String instructor);
}