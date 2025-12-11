package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    // Custom query to find quizzes by their link to a LecturePart
    Optional<Quiz> findByLecturePart_Id(Integer partId);
    
    // Custom query to find quizzes based on title (optional)
    List<Quiz> findByQuizTitleContainingIgnoreCase(String title);
}