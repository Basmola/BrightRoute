package com.brightroute.brightroute.service.impl;
import com.brightroute.brightroute.model.QuizChoice;
import com.brightroute.brightroute.repository.QuizChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizChoiceService {

    private final QuizChoiceRepository quizChoiceRepository;

    @Autowired
    public QuizChoiceService(QuizChoiceRepository quizChoiceRepository) {
        this.quizChoiceRepository = quizChoiceRepository;
    }

    public List<QuizChoice> findAllChoices() {
        return quizChoiceRepository.findAll();
    }

    public Optional<QuizChoice> findChoiceById(Integer id) {
        return quizChoiceRepository.findById(id);
    }

    public QuizChoice saveChoice(QuizChoice choice) {
        return quizChoiceRepository.save(choice);
    }

    public void deleteChoice(Integer id) {
        quizChoiceRepository.deleteById(id);
    }
}