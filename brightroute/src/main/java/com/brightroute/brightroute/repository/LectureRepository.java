package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// CORRECTION: Changed the Primary Key type from Long to Integer
public interface LectureRepository extends JpaRepository<Lecture, Integer> {}