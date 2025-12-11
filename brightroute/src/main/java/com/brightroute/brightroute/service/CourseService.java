package com.brightroute.brightroute.service;
import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> findCourseById(Integer id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course) {
        // Business logic can go here (e.g., validation, logging)
        return courseRepository.save(course);
    }

    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
    }
}