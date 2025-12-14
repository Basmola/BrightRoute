package com.brightroute.brightroute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.brightroute.brightroute.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    java.util.List<Enrollment> findByUser_Id(Integer userId);
}