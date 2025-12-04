package com.brightroute.brightroute.repository;

// LectureRepository.java
import com.brightroute.brightroute.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {}