package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
   boolean existsByNationalId(String nationalId); 
}
