package com.brightroute.brightroute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.brightroute.brightroute.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}