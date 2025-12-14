package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.StudentQuestionsAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentQuestionsAnswerRepository extends JpaRepository<StudentQuestionsAnswer, Integer> {

}