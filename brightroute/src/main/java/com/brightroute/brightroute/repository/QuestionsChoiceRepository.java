package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.QuestionsChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsChoiceRepository extends JpaRepository<QuestionsChoice, Integer> {
    // Example: List<QuizChoice> findByQuestionId(Integer questionId);
}