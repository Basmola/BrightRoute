package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Integer id) {
        return courseService.findCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.saveCourse(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Integer id, @RequestBody Course courseDetails) {
        return courseService.findCourseById(id)
                .map(course -> {
                    course.setCourseTitle(courseDetails.getCourseTitle());
                    course.setCourseDescription(courseDetails.getCourseDescription());
                    course.setCourseInstructor(courseDetails.getCourseInstructor());
                    course.setLevelId(courseDetails.getLevelId());
                    Course updatedCourse = courseService.saveCourse(course);
                    return ResponseEntity.ok(updatedCourse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        if (courseService.findCourseById(id).isPresent()) {
            courseService.deleteCourse(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
