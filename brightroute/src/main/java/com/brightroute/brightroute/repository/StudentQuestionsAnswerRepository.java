package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.StudentQuestionsAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentQuestionsAnswerRepository extends JpaRepository<StudentQuestionsAnswer, Integer> {
    // CORRECTION: PK type changed from Long to Integer
    // Custom queries for fetching answers by submission ID would be useful here
}