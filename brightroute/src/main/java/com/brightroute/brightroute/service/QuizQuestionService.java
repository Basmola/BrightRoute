package com.brightroute.brightroute.service;
import com.brightroute.brightroute.model.QuizQuestion;
import com.brightroute.brightroute.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizQuestionService {

    private final QuizQuestionRepository quizQuestionRepository;

    @Autowired
    public QuizQuestionService(QuizQuestionRepository quizQuestionRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
    }

    public List<QuizQuestion> findAllQuestions() {
        return quizQuestionRepository.findAll();
    }

    public Optional<QuizQuestion> findQuestionById(Integer id) {
        return quizQuestionRepository.findById(id);
    }

    public QuizQuestion saveQuestion(QuizQuestion question) {
        return quizQuestionRepository.save(question);
    }

    public void deleteQuestion(Integer id) {
        quizQuestionRepository.deleteById(id);
    }
}