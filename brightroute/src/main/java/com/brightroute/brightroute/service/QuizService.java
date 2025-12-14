package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Quiz;
import com.brightroute.brightroute.model.QuizQuestion;
import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.repository.QuizRepository;
import com.brightroute.brightroute.repository.LecturePartRepository;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final LecturePartRepository lecturePartRepository;  

    @Autowired
    public QuizService(QuizRepository quizRepository, 
                       LecturePartRepository lecturePartRepository) {
        this.quizRepository = quizRepository;
        this.lecturePartRepository = lecturePartRepository;
    }

    @Transactional
    public Quiz saveQuiz(Quiz quiz) {

        if (quiz.getLecturePart() == null || quiz.getLecturePart().getId() == null) {
            throw new IllegalArgumentException("Quiz must be linked to a valid LecturePart.");
        }

        return quizRepository.save(quiz);
    }

    public Optional<Quiz> findQuizById(Integer quizId) {
         
        return quizRepository.findById(quizId);
    }
    
    public List<Quiz> findAllQuizzes() {
        return quizRepository.findAll();
    }
    
    public void deleteQuiz(Integer quizId) {
        quizRepository.deleteById(quizId);
    }

    public Optional<Quiz> findQuizByPartId(Integer partId) {
        return quizRepository.findByLecturePart_Id(partId);
    }

    public List<QuizQuestion> getQuestionsForQuiz(Integer quizId) {
        Quiz quiz = findQuizById(quizId)
            .orElseThrow(() -> new RuntimeException("Quiz not found for ID: " + quizId));

        return quiz.getQuestions();
    }

}