package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.StudentQuestionsAnswer;
import com.brightroute.brightroute.repository.StudentQuestionsAnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentQuestionsAnswerService {

    private final StudentQuestionsAnswerRepository answerRepository;

    public StudentQuestionsAnswerService(StudentQuestionsAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public StudentQuestionsAnswer saveAnswer(StudentQuestionsAnswer answer) {
        return answerRepository.save(answer);
    }

    public List<StudentQuestionsAnswer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public Optional<StudentQuestionsAnswer> getAnswerById(Integer id) {  
        return answerRepository.findById(id);
    }

    public void deleteAnswer(Integer id) {  
        answerRepository.deleteById(id);
    }
}