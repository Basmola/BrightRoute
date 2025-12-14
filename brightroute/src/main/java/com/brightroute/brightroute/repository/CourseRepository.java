package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

@Repository
@Validated
public interface CourseRepository extends JpaRepository<Course, Integer> {

}