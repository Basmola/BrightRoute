package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.QuizChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizChoiceRepository extends JpaRepository<QuizChoice, Integer> {
    // Example: List<QuizChoice> findByQuestionId(Integer questionId);
}