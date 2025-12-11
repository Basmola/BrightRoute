package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Course;

public interface ICourseOperation {
    Course addCourse(Course course);
    Course updateCourse(Long courseId, Course updated);
    void deleteCourse(Long courseId);
}
