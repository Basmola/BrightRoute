package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
 
public interface LectureRepository extends JpaRepository<Lecture, Integer> {}